package nl.iobyte.dataapi.bucket.enums;

import nl.iobyte.dataapi.bucket.interfaces.IBucket;
import nl.iobyte.dataapi.bucket.interfaces.partition.IBucketPartition;
import nl.iobyte.dataapi.bucket.interfaces.partition.IGenericPartitionStrategy;
import java.util.concurrent.ThreadLocalRandom;

public enum PartitionStrategies implements IGenericPartitionStrategy {

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
