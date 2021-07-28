package nl.iobyte.dataapi.chance;

import nl.iobyte.dataapi.chance.interfaces.IChanceProvider;
import nl.iobyte.dataapi.chance.objects.NullEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChanceMap<T> {

    private final AtomicInteger total = new AtomicInteger(0);
    private final TreeMap<Integer, T> values = new TreeMap<>();
    private final Map<T, Integer> indexes = new HashMap<>();

    public int getTotalChance() {
        return total.get();
    }

    /**
     * Add chance to map
     * @param value T
     * @param chance Integer
     */
    public void add(T value, int chance) {
        //Add chance of not exists
        if(!indexes.containsKey(value)) {
            int key = total.addAndGet(chance);
            values.put(key, value);
            indexes.put(value, key);
            return;
        }

        int old = indexes.get(value);

        //Remap data above and including old key
        AtomicInteger i = new AtomicInteger();
        values.tailMap(old, true).forEach((a, b) -> {
            values.remove(a);

            i.set(a + chance);
            indexes.put(b, i.get());
            values.put(i.get(), b);
        });
    }

    /**
     * Check if value exists
     * @param value T
     * @return Boolean
     */
    public boolean has(T value) {
        return indexes.containsKey(value);
    }

    /**
     * Check if total chance exists
     * @param i Integer
     * @return Boolean
     */
    public boolean has(int i) {
        return values.containsKey(i);
    }

    /**
     * Get total chance from value
     * @param value T
     * @return Integer
     */
    public int get(T value) {
        return indexes.get(value);
    }

    /**
     * Get value from total chance
     * @param i Integer
     * @return T
     */
    public T get(int i) {
        return values.get(i);
    }

    /**
     * Get value from chance provider
     * @param provider IChanceProvider
     * @return T
     */
    public T get(IChanceProvider provider) {
        return Optional.ofNullable(
                values.ceilingEntry(
                        provider.get(getTotalChance())
                )
        ).orElse(new NullEntry<>())
                .getValue();
    }

    /**
     * Remove value and remap existing values
     * @param value T
     */
    public void remove(T value) {
        int key = indexes.remove(value);
        values.remove(key);

        //Get change from entry
        int difference;
        try {
            int before = values.floorKey(key);
            difference = key - before;
        } catch (Exception ignored) {
            difference = key;
        }

        //Remove from total
        total.addAndGet(difference * -1);

        //Remap values, time complexity of O(N - M) where M is values bellow key and N is total values
        AtomicInteger i = new AtomicInteger();
        int finalDifference = difference;
        values.tailMap(key).forEach((a, b) -> {
            values.remove(a);

            i.set(a - finalDifference);
            indexes.put(b, i.get());
            values.put(i.get(), b);
        });
    }

}
