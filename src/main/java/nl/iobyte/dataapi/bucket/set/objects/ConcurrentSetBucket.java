package nl.iobyte.dataapi.bucket.set.objects;

import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentSetBucket<T> extends AbstractSetBucket<T> {

    public ConcurrentSetBucket(int size, ISetPartitionStrategy<T> strategy) {
        super(size, strategy);
    }

    public Set<T> createSet() {
        return ConcurrentHashMap.newKeySet();
    }

}
