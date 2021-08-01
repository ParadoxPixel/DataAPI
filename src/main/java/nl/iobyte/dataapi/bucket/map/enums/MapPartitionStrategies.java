package nl.iobyte.dataapi.bucket.map.enums;

import nl.iobyte.dataapi.bucket.map.interfaces.IMapBucket;
import nl.iobyte.dataapi.bucket.map.interfaces.partition.IGenericMapPartitionStrategy;
import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapBucketPartition;
import java.util.concurrent.ThreadLocalRandom;

public enum MapPartitionStrategies implements IGenericMapPartitionStrategy {

    RANDOM {
        public int allocate(IMapBucket<?,?> bucket) {
            return ThreadLocalRandom.current().nextInt(bucket.getPartitionCount());
        }
    },
    LOWEST_SIZE {
        public int allocate(IMapBucket<?,?> bucket) {
            int index = -1;
            int lowestSize = Integer.MAX_VALUE;

            int size, i;
            for (IMapBucketPartition<?,?> partition : bucket.getPartitions()) {
                size = partition.size();
                i = partition.getIndex();
                if (size == 0)
                    return i;

                if (size < lowestSize) {
                    lowestSize = size;
                    index = i;
                }
            }

            if (index == -1)
                return 0;

            return index;
        }
    }

}
