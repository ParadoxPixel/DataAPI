package nl.iobyte.dataapi.bucket.map.enums;

import nl.iobyte.dataapi.bucket.map.interfaces.IMapBucket;
import nl.iobyte.dataapi.bucket.map.interfaces.IMapBukkitFactory;
import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapPartitionStrategy;
import nl.iobyte.dataapi.bucket.map.objects.ConcurrentMapBucket;
import nl.iobyte.dataapi.bucket.map.objects.HashMapBucket;

public enum DefaultMapBucket implements IMapBukkitFactory {

    CONCURRENT {
        @SuppressWarnings("unchecked")
        public <T,R> IMapBucket<T,R> newInstance(int size, IMapPartitionStrategy<?,?> strategy) {
            return new ConcurrentMapBucket<>(size, (IMapPartitionStrategy<T, R>) strategy);
        }
    },
    HASH {
        @SuppressWarnings("unchecked")
        public <T,R> IMapBucket<T,R> newInstance(int size, IMapPartitionStrategy<?,?> strategy) {
            return new HashMapBucket<>(size, (IMapPartitionStrategy<T, R>) strategy);
        }
    }

}
