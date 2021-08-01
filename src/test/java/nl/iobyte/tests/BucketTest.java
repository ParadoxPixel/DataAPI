package nl.iobyte.tests;

import nl.iobyte.dataapi.bucket.map.enums.DefaultMapBucket;
import nl.iobyte.dataapi.bucket.map.enums.MapPartitionStrategies;
import nl.iobyte.dataapi.bucket.map.interfaces.IMapBucket;
import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapBucketPartition;
import nl.iobyte.dataapi.bucket.set.enums.DefaultSetBucket;
import nl.iobyte.dataapi.bucket.set.enums.SetPartitionStrategies;
import nl.iobyte.dataapi.bucket.set.interfaces.IBucket;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.IBucketPartition;
import org.junit.Assert;
import org.junit.Test;

public class BucketTest {

    @Test
    public void testSet() {
        int partitions = 5;
        IBucket<String> bucket = DefaultSetBucket.HASH.newInstance(
                partitions,
                SetPartitionStrategies.LOWEST_SIZE
        );

        Assert.assertEquals("Partition count should match", partitions, bucket.getPartitionCount());

        for (int i = 0; i < 20; i++)
            bucket.add("Entry #" + i);

        Assert.assertEquals("Bucket size should be 20", 20, bucket.size());

        int i = 0;
        for (IBucketPartition<String> partition : bucket.getPartitions())
            i += partition.size();

        Assert.assertEquals("Contents of partitions should add up to 20", 20, i);
        Assert.assertNotNull("Next partition in cycle should not be null", bucket.asStepper().next());
    }

    @Test
    public void testMap() {
        int partitions = 5;
        IMapBucket<String,Integer> bucket = DefaultMapBucket.HASH.newInstance(
                partitions,
                MapPartitionStrategies.LOWEST_SIZE
        );

        Assert.assertEquals("Partition count should match", partitions, bucket.getPartitionCount());

        for (int i = 0; i < 20; i++)
            bucket.put("Entry #" + i, i);

        Assert.assertEquals("MapBucket size should be 20", 20, bucket.size());

        int i = 0;
        for (IMapBucketPartition<String,Integer> partition : bucket.getPartitions())
            i += partition.size();

        Assert.assertEquals("Contents of partitions should add up to 20", 20, i);
        Assert.assertNotNull("Next partition in cycle should not be null", bucket.asStepper().next());
    }

}
