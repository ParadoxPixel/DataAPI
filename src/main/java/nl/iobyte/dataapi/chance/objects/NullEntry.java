package nl.iobyte.dataapi.chance.objects;

import java.util.Map;

/**
 * Empty Map.Entry implementation
 */
public class NullEntry<T> implements Map.Entry<Integer, T> {

    public Integer getKey() {
        return null;
    }

    public T getValue() {
        return null;
    }

    public T setValue(T value) {
        return null;
    }

}
