package nl.iobyte.tests;

import nl.iobyte.dataapi.bucket.enums.DefaultBucket;
import nl.iobyte.dataapi.bucket.enums.PartitionStrategies;
import nl.iobyte.dataapi.bucket.interfaces.IBucket;
import nl.iobyte.dataapi.bucket.interfaces.partition.IBucketPartition;
import org.junit.Assert;
import org.junit.Test;

public class BucketTest {

    @Test
    public void test() {
        int partitions = 5;
        IBucket<String> bucket = DefaultBucket.HASH.newInstance(
                partitions,
                PartitionStrategies.LOWEST_SIZE
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

}
