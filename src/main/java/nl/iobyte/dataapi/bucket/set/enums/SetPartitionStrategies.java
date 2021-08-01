package nl.iobyte.dataapi.bucket.set.enums;

import nl.iobyte.dataapi.bucket.set.interfaces.IBucket;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.IBucketPartition;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.IGenericSetPartitionStrategy;
import java.util.concurrent.ThreadLocalRandom;

public enum SetPartitionStrategies implements IGenericSetPartitionStrategy {

    RANDOM {
        public int allocate(IBucket<?> bucket) {
            return ThreadLocalRandom.current().nextInt(bucket.getPartitionCount());
        }
    },
    LOWEST_SIZE {
        public int allocate(IBucket<?> bucket) {
            int index = -1;
            int lowestSize = Integer.MAX_VALUE;

            int size, i;
            for (IBucketPartition<?> partition : bucket.getPartitions()) {
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
