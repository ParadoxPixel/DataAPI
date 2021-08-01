package nl.iobyte.dataapi.bucket.map.objects;

import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapPartitionStrategy;
import java.util.HashMap;
import java.util.Map;

public class HashMapBucket<T,R> extends AbstractMapBucket<T,R> {

    public HashMapBucket(int size, IMapPartitionStrategy<T,R> strategy) {
        super(size, strategy);
    }

    public Map<T,R> createMap() {
        return new HashMap<>();
    }

}
