package nl.iobyte.dataapi.bucket.set.interfaces.partition;

import nl.iobyte.dataapi.bucket.set.interfaces.ISetBucket;

public interface ISetPartitionStrategy<T> {

    /**
     * Calculates the index of the partition to use
     * @param object Object
     * @param bucket IBucket
     * @return Integer
     */
    int allocate(T object, ISetBucket<T> bucket);

}
