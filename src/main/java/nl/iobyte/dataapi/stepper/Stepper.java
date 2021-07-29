package nl.iobyte.dataapi.stepper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Stepper<T> {

    private final List<T> objects;
    private final int size;
    private final AtomicInteger cursor = new AtomicInteger(0);

    public Stepper(List<T> objects) {
        this.objects = Collections.unmodifiableList(objects);
        this.size = this.objects.size();
    }

    /**
     * Get the cursor's position
     * @return Integer
     */
    public int cursor() {
        return this.cursor.get();
    }

    /**
     * Set the cursors position
     * @param index Integer
     */
    public void setCursor(int index) {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);

        this.cursor.set(index);
    }

    /**
     * Get the current object
     * @return T
     */
    public T current() {
        return objects.get(cursor());
    }

    /**
     * Move to the next object
     * @return T
     */
    public T next() {
        return objects.get(cursor.updateAndGet(i -> {
            int n = i + 1;
            if (n >= this.size)
                return 0;

            return n;
        }));
    }

    /**
     * Move to the previous object
     * @return T
     */
    public T previous() {
        return objects.get(cursor.updateAndGet(i -> {
            if (i == 0)
                return this.size - 1;

            return i - 1;
        }));
    }

    /**
     * Get next position
     * @return Integer
     */
    public int nextPosition() {
        int n = cursor.get() + 1;
        if (n >= size)
            return 0;

        return n;
    }

    /**
     * Get previous position
     * @return Integer
     */
    public int previousPosition() {
        int i = cursor.get();
        if (i == 0)
            return size - 1;

        return i - 1;
    }

    /**
     * Get next object
     * @return T
     */
    public T peekNext() {
        return objects.get(nextPosition());
    }

    /**
     * Get previous object
     * @return T
     */
    public T peekPrevious() {
        return objects.get(previousPosition());
    }

}
