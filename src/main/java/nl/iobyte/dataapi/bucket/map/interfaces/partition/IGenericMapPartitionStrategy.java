package nl.iobyte.dataapi.bucket.map.interfaces.partition;

import nl.iobyte.dataapi.bucket.map.interfaces.IMapBucket;

public interface IGenericMapPartitionStrategy extends IMapPartitionStrategy<Object,Object> {

    /**
     * Calculates the index of the partition to use
     * @param bucket IMapBucket
     * @return Integer
     */
    int allocate(IMapBucket<?,?> bucket);

    /**
     * Casts to type T,R
     * @param <T> T
     * @param <R> R
     * @return T
     */
    @SuppressWarnings("unchecked")
    default <T,R> IMapPartitionStrategy<T,R> cast() {
        return (IMapPartitionStrategy<T,R>) this;
    }

    /**
     * Calculates the index of the partition to use
     * @param key Object
     * @param value Object
     * @param bucket IBucket
     * @return Integer
     */
    @Deprecated
    default int allocate(Object key, Object value, IMapBucket<Object,Object> bucket) {
        return allocate(bucket);
    }

}