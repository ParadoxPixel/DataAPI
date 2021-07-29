package nl.iobyte.dataapi.bucket.objects;

import nl.iobyte.dataapi.bucket.interfaces.IPartitionStrategy;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBucket<T> extends AbstractBucket<T> {

    public ConcurrentBucket(int size, IPartitionStrategy<T> strategy) {
        super(size, strategy);
    }

    public Set<T> createSet() {
        return ConcurrentHashMap.newKeySet();
    }

}
