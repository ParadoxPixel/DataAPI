package nl.iobyte.dataapi.bucket.objects;

import nl.iobyte.dataapi.bucket.interfaces.partition.IPartitionStrategy;
import java.util.HashSet;
import java.util.Set;

public class HashSetBucket<T> extends AbstractBucket<T> {

    public HashSetBucket(int size, IPartitionStrategy<T> strategy) {
        super(size, strategy);
    }

    public Set<T> createSet() {
        return new HashSet<>();
    }

}
