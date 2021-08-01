package nl.iobyte.dataapi.bucket.set.enums;

import nl.iobyte.dataapi.bucket.set.interfaces.ISetBucket;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.IGenericSetPartitionStrategy;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetBucketPartition;
import java.util.concurrent.ThreadLocalRandom;

public enum SetPartitionStrategies implements IGenericSetPartitionStrategy {

    RANDOM {
        public int allocate(ISetBucket<?> bucket) {
            return ThreadLocalRandom.current().nextInt(bucket.getPartitionCount());
        }
    },
    LOWEST_SIZE {
        public int allocate(ISetBucket<?> bucket) {
            int index = -1;
            int lowestSize = Integer.MAX_VALUE;

            int size, i;
            for (ISetBucketPartition<?> partition : bucket.getPartitions()) {
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
