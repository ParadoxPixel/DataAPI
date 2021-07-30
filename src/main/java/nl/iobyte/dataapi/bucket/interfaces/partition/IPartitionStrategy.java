package nl.iobyte.dataapi.bucket.interfaces.partition;

import nl.iobyte.dataapi.bucket.interfaces.IBucket;

public interface IPartitionStrategy<T> {

    /**
     * Calculates the index of the partition to use
     * @param object Object
     * @param bucket IBucket
     * @return Integer
     */
    int allocate(T object, IBucket<T> bucket);

}
