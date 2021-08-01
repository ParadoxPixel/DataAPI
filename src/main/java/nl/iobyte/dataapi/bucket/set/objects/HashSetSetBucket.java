package nl.iobyte.dataapi.bucket.set.objects;

import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;
import java.util.HashSet;
import java.util.Set;

public class HashSetSetBucket<T> extends AbstractSetBucket<T> {

    public HashSetSetBucket(int size, ISetPartitionStrategy<T> strategy) {
        super(size, strategy);
    }

    public Set<T> createSet() {
        return new HashSet<>();
    }

}
