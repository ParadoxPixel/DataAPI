package nl.iobyte.dataapi.data;

import nl.iobyte.dataapi.data.interfaces.IData;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataService<T, R extends IData<T>> {

    private final Map<T, R> entries = new ConcurrentHashMap<>();

    /**
     * Get all entries
     * @return Map<T, R>
     */
    public Map<T, R> getEntries() {
        return entries;
    }

    /**
     * Add entry and return old
     * @param entry R
     * @return R
     */
    public R add(R entry) {
        return entries.put(entry.getID(), entry);
    }

    /**
     * Check if has entry
     * @param key T
     * @return Boolean
     */
    public boolean has(T key) {
        return entries.containsKey(key);
    }

    /**
     * Get entry
     * @param key T
     * @return R
     */
    public R get(T key) {
        return entries.get(key);
    }

    /**
     * Get optional entry
     * @param key T
     * @return Optional<R>
     */
    public Optional<R> getOptional(T key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Remove entry and return value
     * @param key T
     * @return R
     */
    public R remove(T key) {
        return entries.remove(key);
    }

}
