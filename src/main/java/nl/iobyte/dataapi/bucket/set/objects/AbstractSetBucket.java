package nl.iobyte.dataapi.bucket.set.objects;

import nl.iobyte.dataapi.bucket.set.interfaces.ISetBucket;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetBucketPartition;
import nl.iobyte.dataapi.bucket.set.interfaces.partition.ISetPartitionStrategy;
import nl.iobyte.dataapi.stepper.Stepper;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractSetBucket<T> extends AbstractSet<T> implements ISetBucket<T> {

    private final int size;
    private final ISetPartitionStrategy<T> strategy;

    private final Set<T> contents;
    private final List<Set<T>> partitions;
    private final List<ISetBucketPartition<T>> partitionView;
    private final Stepper<ISetBucketPartition<T>> stepper;

    public AbstractSetBucket(int size, ISetPartitionStrategy<T> strategy) {
        this.size = size;
        this.strategy = strategy;

        //Set<T>
        List<Set<T>> partitions = new ArrayList<>();
        for (int i = 0; i < size; i++)
            partitions.add(createSet());

        this.contents = createSet();
        this.partitions = Collections.unmodifiableList(partitions);

        //IBucketPartition<T>
        List<ISetBucketPartition<T>> partitionView = new ArrayList<>();
        for (int i = 0; i < size; i++)
            partitionView.add(new SetView(i, this.partitions.get(i)));

        this.partitionView = Collections.unmodifiableList(partitionView);
        this.stepper = new Stepper<>(this.partitionView);
    }

    /**
     * Create set for type T
     * @return Set<T>
     */
    public abstract Set<T> createSet();

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
     * @return IBucketPartition<T>
     */
    public ISetBucketPartition<T> getPartition(int i) {
        return partitionView.get(i);
    }

    /**
     * {@inheritDoc}
     * @return List<IBucketPartition < T>>
     */
    public List<ISetBucketPartition<T>> getPartitions() {
        return partitionView;
    }

    /**
     * Get cycle for bucket
     * @return Cycle<IBucketPartition < T>>
     */
    public Stepper<ISetBucketPartition<T>> asStepper() {
        return stepper;
    }

    @Override
    public boolean add(T obj) {
        if (obj == null)
            return false;

        if (!contents.add(obj))
            return false;

        partitions.get(strategy.allocate(obj, this)).add(obj);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contents.remove(o))
            return false;

        for (Set<T> partition : this.partitions)
            if (partition.remove(o))
                return true;

        return false;
    }

    @Override
    public void clear() {
        for (Set<T> partition : this.partitions)
            partition.clear();

        contents.clear();
    }

    /**
     * {@inheritDoc}
     * @return Iterator<T>
     */
    public Iterator<T> iterator() {
        return new BucketIterator();
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
    public boolean contains(Object o) {
        return contents.contains(o);
    }

    /**
     * Class used to wrap the result of {@link ISetBucket}'s {@link #iterator()} method.
     * <p>
     * This wrapping overrides the #remove method, and ensures that when removed,
     * elements are also removed from their backing partition.
     */
    private final class BucketIterator implements Iterator<T> {
        private final Iterator<T> delegate = AbstractSetBucket.this.contents.iterator();
        private T current;

        @Override
        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        @Override
        public T next() {
            current = delegate.next();
            return current;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            delegate.remove();
            for (Set<T> partition : AbstractSetBucket.this.partitions) {
                partition.remove(current);
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            delegate.forEachRemaining(action);
        }
    }


    /**
     * Class used to wrap the backing sets returned by {@link #getPartition(int)}.
     * Prevents add, and propagates remove objects back to the parent bucket.
     */
    private final class SetView extends AbstractSet<T> implements ISetBucketPartition<T> {
        private final Set<T> backing;
        private final int index;

        private SetView(int index, Set<T> backing) {
            this.backing = backing;
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public Iterator<T> iterator() {
            return new SetViewIterator(backing.iterator());
        }

        public boolean remove(Object o) {
            if (!backing.remove(o))
                return false;

            AbstractSetBucket.this.contents.remove(o);
            return true;
        }

        public void clear() {
            AbstractSetBucket.this.contents.removeAll(backing);
            backing.clear();
        }

        public int size() {
            return backing.size();
        }

        public boolean isEmpty() {
            return backing.isEmpty();
        }

        public boolean contains(Object o) {
            return backing.contains(o);
        }

        public Object[] toArray() {
            return backing.toArray();
        }

        public <R> R[] toArray(R[] a) {
            return backing.toArray(a);
        }

        public boolean containsAll(Collection<?> c) {
            return backing.containsAll(c);
        }

        public int hashCode() {
            return backing.hashCode();
        }

    }

    /**
     * Wrapping around {@link SetView}'s iterators, to propagate calls from the
     * #remove method to the parent bucket.
     */
    private final class SetViewIterator implements Iterator<T> {
        private final Iterator<T> delegate;
        private T current;

        private SetViewIterator(Iterator<T> delegate) {
            this.delegate = delegate;
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public T next() {
            current = delegate.next();
            return current;
        }

        public void remove() {
            if (current == null)
                return;

            delegate.remove();
            AbstractSetBucket.this.contents.remove(current);
        }

        public void forEachRemaining(Consumer<? super T> action) {
            delegate.forEachRemaining(action);
        }

    }

}
