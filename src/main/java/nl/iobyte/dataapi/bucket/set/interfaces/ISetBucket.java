package nl.iobyte.dataapi.bucket.set.interfaces;

import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetBucketPartition;
import nl.iobyte.dataapi.stepper.Stepper;
import java.util.List;
import java.util.Set;

public interface ISetBucket<T> extends Set<T> {

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
    ISetBucketPartition<T> getPartition(int i);

    /**
     * Get partitions
     * @return List<IBucketPartition <T>>
     */
    List<ISetBucketPartition<T>> getPartitions();

    /**
     * Get cycle for bucket
     * @return Stepper<IBucketPartition <T>>
     */
    Stepper<ISetBucketPartition<T>> asStepper();

}
