package nl.iobyte.dataapi.bucket.map.objects;

import nl.iobyte.dataapi.bucket.map.interfaces.IMapBucket;
import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapBucketPartition;
import nl.iobyte.dataapi.bucket.map.interfaces.partition.IMapPartitionStrategy;
import nl.iobyte.dataapi.stepper.Stepper;
import java.util.*;

public abstract class AbstractMapBucket<T,R> implements IMapBucket<T,R> {

    private final int size;
    private final IMapPartitionStrategy<T,R> strategy;

    private final Map<T,R> contents;
    private final List<Map<T,R>> partitions;
    private final List<IMapBucketPartition<T,R>> partitionView;
    private final Stepper<IMapBucketPartition<T,R>> stepper;

    public AbstractMapBucket(int size, IMapPartitionStrategy<T,R> strategy) {
        this.size = size;
        this.strategy = strategy;

        //Set<T>
        List<Map<T,R>> partitions = new ArrayList<>();
        for (int i = 0; i < size; i++)
            partitions.add(createMap());

        this.contents = createMap();
        this.partitions = Collections.unmodifiableList(partitions);

        //IBucketPartition<T>
        List<IMapBucketPartition<T,R>> partitionView = new ArrayList<>();
        for (int i = 0; i < size; i++)
            partitionView.add(new SetView(i, this.partitions.get(i)));

        this.partitionView = Collections.unmodifiableList(partitionView);
        this.stepper = new Stepper<>(this.partitionView);
    }

    /**
     * Create map for type T,R
     * @return Map<T,R>
     */
    public abstract Map<T,R> createMap();

    /**
     * {@inheritDoc}
     * @return Integer
     */
    public int getPartitionCount() {
        return size;
    }

    /**
     * {@inheritDoc}
     * @param i Integer
     * @return IBucketPartition<T,R>
     */
    public IMapBucketPartition<T,R> getPartition(int i) {
        return partitionView.get(i);
    }

    /**
     * {@inheritDoc}
     * @return List<IBucketPartition <T,R>>
     */
    public List<IMapBucketPartition<T,R>> getPartitions() {
        return partitionView;
    }

    /**
     * Get cycle for bucket
     * @return Cycle<IBucketPartition < T,R>>
     */
    public Stepper<IMapBucketPartition<T,R>> asStepper() {
        return stepper;
    }

    /**
     * {@inheritDoc}
     * @return Set<Entry<T,R>>
     */
    public Set<Entry<T,R>> entrySet() {
        return Collections.unmodifiableSet(contents.entrySet());
    }

    /**
     * {@inheritDoc}
     * @return Set<T>
     */
    public Set<T> keySet() {
        return contents.keySet();
    }

    /**
     * {@inheritDoc}
     * @return Collection<R>
     */
    public Collection<R> values() {
        return contents.values();
    }

    /**
     * {@inheritDoc}
     * @param key T
     * @param value R
     * @return R
     */
    @Override
    public R put(T key, R value) {
        if(value == null)
            throw new NullPointerException("MapBucket doesn't accept null values");

        R old = remove(key);
        contents.put(key, value);
        partitions.get(strategy.allocate(key, value, this)).put(key, value);
        return old;
    }

    /**
     * {@inheritDoc}
     * @param m Map<? extends T, ? extends R>
     */
    @Override
    public void putAll(Map<? extends T, ? extends R> m) {
        for(Map.Entry<? extends T, ? extends R> entry : m.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    @Override
    public R get(Object key) {
        return contents.get(key);
    }

    @Override
    public R remove(Object o) {
        R old = contents.remove(o);
        if(old == null)
            return null;

        for(Map<T,R> partition : this.partitions) {
            old = partition.remove(o);
            if(old != null) {
                return old;
            }
        }

        return null;
    }

    @Override
    public void clear() {
        for(Map<T,R> partition : this.partitions)
            partition.clear();

        contents.clear();
    }

    /**
     * {@inheritDoc}
     * @return Integer
     */
    public int size() {
        return contents.size();
    }

    /**
     * {@inheritDoc}
     * @return Boolean
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    /**
     * {@inheritDoc}
     * @return Boolean
     */
    public boolean containsKey(Object o) {
        return contents.containsKey(o);
    }

    /**
     * {@inheritDoc}
     * @return Boolean
     */
    public boolean containsValue(Object value) {
        return contents.containsValue(value);
    }

    /**
     * Class used to wrap the backing sets returned by {@link #getPartition(int)}.
     * Prevents add, and propagates remove objects back to the parent bucket.
     */
    private final class SetView extends AbstractMap<T,R> implements IMapBucketPartition<T,R> {

        private final Map<T,R> backing;
        private final int index;

        private SetView(int index, Map<T,R> backing) {
            this.backing = backing;
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }

        public R remove(Object o) {
            R old = backing.remove(o);
            if(old == null)
                return null;

            AbstractMapBucket.this.contents.remove(o);
            return old;
        }

        public void clear() {
            AbstractMapBucket.this.contents.clear();
            backing.clear();
        }

        @Override
        public Set<Entry<T, R>> entrySet() {
            return Collections.unmodifiableSet(backing.entrySet());
        }

        public int size() {
            return backing.size();
        }

        public boolean isEmpty() {
            return backing.isEmpty();
        }

        public int hashCode() {
            return backing.hashCode();
        }

    }

}
