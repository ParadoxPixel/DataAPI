package nl.iobyte.dataapi.bucket.set.interfaces;

import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;

public interface IBukkitFactory {

    /**
     * Get a new buckit instance from size and strategy
     * @param size     Integer
     * @param strategy IPartitionStrategy<?>
     * @param <T>      T
     * @return IBucket<T>
     */
    <T> IBucket<T> newInstance(int size, ISetPartitionStrategy<?> strategy);

}
