package nl.iobyte.dataapi.bucket.interfaces;

import java.util.Collection;
import java.util.Set;

public interface IBucketPartition<T> extends Set<T> {

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
