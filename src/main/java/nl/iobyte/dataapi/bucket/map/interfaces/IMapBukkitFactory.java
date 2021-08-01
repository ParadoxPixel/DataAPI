package nl.iobyte.dataapi.bucket.map.interfaces;

import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapPartitionStrategy;

public interface IMapBukkitFactory {

    /**
     * Get a new bucket instance from size and strategy
     * @param size     Integer
     * @param strategy IPartitionStrategy<?,?>
     * @param <T>      T
     * @param <R>      R
     * @return IBucket<T>
     */
    <T,R> IMapBucket<T,R> newInstance(int size, IMapPartitionStrategy<?,?> strategy);

}
