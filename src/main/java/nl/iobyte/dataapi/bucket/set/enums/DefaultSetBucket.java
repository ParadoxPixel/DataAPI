package nl.iobyte.dataapi.bucket.set.enums;

import nl.iobyte.dataapi.bucket.set.interfaces.IBukkitFactory;
import nl.iobyte.dataapi.bucket.set.interfaces.ISetBucket;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;
import nl.iobyte.dataapi.bucket.set.objects.ConcurrentSetBucket;
import nl.iobyte.dataapi.bucket.set.objects.HashSetSetBucket;

public enum DefaultSetBucket implements IBukkitFactory {

    CONCURRENT {
        @SuppressWarnings("unchecked")
        public <T> ISetBucket<T> newInstance(int size, ISetPartitionStrategy<?> strategy) {
            return new ConcurrentSetBucket<T>(size, (ISetPartitionStrategy<T>) strategy);
        }
    },
    HASH {
        @SuppressWarnings("unchecked")
        public <T> ISetBucket<T> newInstance(int size, ISetPartitionStrategy<?> strategy) {
            return new HashSetSetBucket<T>(size, (ISetPartitionStrategy<T>) strategy);
        }
    }

}
