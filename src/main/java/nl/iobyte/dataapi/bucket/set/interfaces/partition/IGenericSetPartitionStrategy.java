package nl.iobyte.dataapi.bucket.set.interfaces.partition;

import nl.iobyte.dataapi.bucket.set.interfaces.ISetBucket;

public interface IGenericSetPartitionStrategy extends ISetPartitionStrategy<Object> {

    /**
     * Calculates the index of the partition to use
     * @param bucket IBucket
     * @return Integer
     */
    int allocate(ISetBucket<?> bucket);

    /**
     * Casts to type T
     * @param <T> T
     * @return T
     */
    @SuppressWarnings("unchecked")
    default <T> ISetPartitionStrategy<T> cast() {
        return (ISetPartitionStrategy<T>) this;
    }

    /**
     * Calculates the index of the partition to use
     * @param object Object
     * @param bucket IBucket
     * @return Integer
     */
    @Deprecated
    default int allocate(Object object, ISetBucket<Object> bucket) {
        return allocate(bucket);
    }

}