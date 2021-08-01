package nl.iobyte.dataapi.bucket.set.interfaces.partition;

import java.util.Collection;
import java.util.Set;

public interface ISetBucketPartition<T> extends Set<T> {

    /**
     * Gets the index of the partition
     * @return Integer
     */
    int getIndex();

    /**
     * {@inheritDoc}
     * @deprecated Partitions do not support this method.
     */
    @Override
    @Deprecated
    default boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * @deprecated Partitions do not support this method.
     */
    @Override
    @Deprecated
    default boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

}
