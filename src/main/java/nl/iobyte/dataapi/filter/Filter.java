package nl.iobyte.dataapi.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Filter<T> {

    private final List<Function<T, Boolean>> filters = new ArrayList<>();

    /**
     * Add rule to filter
     * @param f Function<T, Boolean>
     * @return Filter<T>
     */
    public Filter<T> add(Function<T, Boolean> f) {
        filters.add(f);
        return this;
    }

    /**
     * Check if object passes filters
     * @param obj T
     * @return Boolean
     */
    public boolean check(T obj) {
        for(Function<T, Boolean> f : filters)
            if(!f.apply(obj))
                return false;

        return true;
    }

    /**
     * Check if object passes filters and call consumer
     * @param obj T
     * @param c Consumer<Boolean>
     */
    public void check(T obj, Consumer<Boolean> c) {
        c.accept(check(obj));
    }

}
