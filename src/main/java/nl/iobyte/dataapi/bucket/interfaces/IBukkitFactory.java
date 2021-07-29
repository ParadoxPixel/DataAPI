package nl.iobyte.dataapi.bucket.interfaces;

public interface IBukkitFactory {

    /**
     * Get a new buckit instance from size and strategy
     * @param size     Integer
     * @param strategy IPartitionStrategy<?>
     * @param <T>      T
     * @return IBucket<T>
     */
    <T> IBucket<T> newInstance(int size, IPartitionStrategy<?> strategy);

}
