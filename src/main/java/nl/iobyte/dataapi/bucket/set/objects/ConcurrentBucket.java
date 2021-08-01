package nl.iobyte.dataapi.bucket.set.objects;

import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBucket<T> extends AbstractBucket<T> {

    public ConcurrentBucket(int size, ISetPartitionStrategy<T> strategy) {
        super(size, strategy);
    }

    public Set<T> createSet() {
        return ConcurrentHashMap.newKeySet();
    }

}
