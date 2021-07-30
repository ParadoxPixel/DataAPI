package nl.iobyte.dataapi.bucket.interfaces;

import nl.iobyte.dataapi.bucket.interfaces.partition.IBucketPartition;
import nl.iobyte.dataapi.stepper.Stepper;
import java.util.List;
import java.util.Set;

public interface IBucket<T> extends Set<T> {

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
    IBucketPartition<T> getPartition(int i);

    /**
     * Get partitions
     * @return List<IBucketPartition <T>>
     */
    List<IBucketPartition<T>> getPartitions();

    /**
     * Get cycle for bucket
     * @return Stepper<IBucketPartition <T>>
     */
    Stepper<IBucketPartition<T>> asStepper();

}
