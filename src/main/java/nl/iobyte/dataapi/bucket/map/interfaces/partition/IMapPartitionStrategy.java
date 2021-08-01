package nl.iobyte.dataapi.bucket.map.interfaces.partition;

import nl.iobyte.dataapi.bucket.map.interfaces.IMapBucket;

public interface IMapPartitionStrategy<T,R> {

    /**
     * Calculates the index of the partition to use
     * @param key T
     * @param value R
     * @param bucket IMapBucket
     * @return Integer
     */
    int allocate(T key, R value, IMapBucket<T,R> bucket);

}
