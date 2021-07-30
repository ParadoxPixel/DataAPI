package nl.iobyte.dataapi.bucket.interfaces.partition;

import nl.iobyte.dataapi.bucket.interfaces.IBucket;

public interface IGenericPartitionStrategy extends IPartitionStrategy<Object> {

    /**
     * Calculates the index of the partition to use
     * @param bucket IBucket
     * @return Integer
     */
    int allocate(IBucket<?> bucket);

    /**
     * Casts to type T
     * @param <T> T
     * @return T
     */
    @SuppressWarnings("unchecked")
    default <T> IPartitionStrategy<T> cast() {
        return (IPartitionStrategy<T>) this;
    }

    /**
     * Calculates the index of the partition to use
     * @param object Object
     * @param bucket IBucket
     * @return Integer
     */
    @Deprecated
    default int allocate(Object object, IBucket<Object> bucket) {
        return allocate(bucket);
    }

}