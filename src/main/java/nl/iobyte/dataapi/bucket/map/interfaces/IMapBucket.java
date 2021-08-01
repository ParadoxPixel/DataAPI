package nl.iobyte.dataapi.bucket.map.interfaces;

import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapBucketPartition;
import nl.iobyte.dataapi.stepper.Stepper;
import java.util.List;
import java.util.Map;

public interface IMapBucket<T,R> extends Map<T,R> {

    /**
     * Get amount of partitions
     * @return Integer
     */
    int getPartitionCount();

    /**
     * Get partition with index
     * @param i Integer
     * @return IBucketPartition<T>
     */
    IMapBucketPartition<T,R> getPartition(int i);

    /**
     * Get partitions
     * @return List<IBucketPartition <T>>
     */
    List<IMapBucketPartition<T,R>> getPartitions();

    /**
     * Get cycle for bucket
     * @return Stepper<IBucketPartition <T>>
     */
    Stepper<IMapBucketPartition<T,R>> asStepper();

}
