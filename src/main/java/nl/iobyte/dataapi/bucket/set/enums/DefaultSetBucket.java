package nl.iobyte.dataapi.bucket.set.enums;

import nl.iobyte.dataapi.bucket.set.interfaces.IBucket;
import nl.iobyte.dataapi.bucket.set.interfaces.IBukkitFactory;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;
import nl.iobyte.dataapi.bucket.set.objects.ConcurrentBucket;
import nl.iobyte.dataapi.bucket.set.objects.HashSetBucket;

public enum DefaultSetBucket implements IBukkitFactory {

    CONCURRENT {
        @SuppressWarnings("unchecked")
        public <T> IBucket<T> newInstance(int size, ISetPartitionStrategy<?> strategy) {
            return new ConcurrentBucket<T>(size, (ISetPartitionStrategy<T>) strategy);
        }
    },
    HASH {
        @SuppressWarnings("unchecked")
        public <T> IBucket<T> newInstance(int size, ISetPartitionStrategy<?> strategy) {
            return new HashSetBucket<T>(size, (ISetPartitionStrategy<T>) strategy);
        }
    }

}
