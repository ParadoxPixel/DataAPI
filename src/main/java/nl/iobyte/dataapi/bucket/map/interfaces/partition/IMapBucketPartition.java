package nl.iobyte.dataapi.bucket.map.interfaces.partition;

import java.util.Map;

public interface IMapBucketPartition<T,R> extends Map<T,R> {

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
    default R put(T t, R r) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * @deprecated Partitions do not support this method.
     */
    @Override
    @Deprecated
    default void putAll(Map<? extends T, ? extends R> c) {
        throw new UnsupportedOperationException();
    }

}
