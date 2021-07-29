package nl.iobyte.dataapi.bucket.enums;

import nl.iobyte.dataapi.bucket.interfaces.IBucket;
import nl.iobyte.dataapi.bucket.interfaces.IBukkitFactory;
import nl.iobyte.dataapi.bucket.interfaces.IPartitionStrategy;
import nl.iobyte.dataapi.bucket.objects.ConcurrentBucket;
import nl.iobyte.dataapi.bucket.objects.HashSetBucket;

public enum DefaultBucket implements IBukkitFactory {

    CONCURRENT {
        @SuppressWarnings("unchecked")
        public <T> IBucket<T> newInstance(int size, IPartitionStrategy<?> strategy) {
            return new ConcurrentBucket<T>(size, (IPartitionStrategy<T>) strategy);
        }
    },
    HASH {
        @SuppressWarnings("unchecked")
        public <T> IBucket<T> newInstance(int size, IPartitionStrategy<?> strategy) {
            return new HashSetBucket<T>(size, (IPartitionStrategy<T>) strategy);
        }
    }

}
