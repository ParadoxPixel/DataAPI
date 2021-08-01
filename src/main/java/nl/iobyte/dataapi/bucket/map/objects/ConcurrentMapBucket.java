package nl.iobyte.dataapi.bucket.map.objects;

import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapPartitionStrategy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapBucket<T,R> extends AbstractMapBucket<T,R> {

    public ConcurrentMapBucket(int size, IMapPartitionStrategy<T,R> strategy) {
        super(size, strategy);
    }

    public Map<T,R> createMap() {
        return new ConcurrentHashMap<>();
    }

}
