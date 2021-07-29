package nl.iobyte.dataapi.filter;

import nl.iobyte.dataapi.filter.annotations.FilterCheck;
import nl.iobyte.dataapi.initializer.DataInitializer;
import nl.iobyte.dataapi.reflection.ReflectionUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Filter<T> {

    private final List<Function<T, Boolean>> filters = new ArrayList<>();
    private final Class<T> clazz;

    Filter(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Get filter of type T
     * @param clazz Class<T>
     * @param <T> T
     * @return Filter<T>
     */
    public static <T> Filter<T> of(Class<T> clazz) {
        return new Filter<>(clazz);
    }

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
     * Load filters from object
     * @param obj Object
     */
    public Filter<T> add(Object obj) {
        Parameter parameter;
        for(Method method : obj.getClass().getDeclaredMethods()) {
            if(method.getParameterCount() != 1)
                continue;

            if((method.getModifiers() & Modifier.PUBLIC) == 0)
                continue;

            if(!method.isAnnotationPresent(FilterCheck.class))
                continue;

            parameter = method.getParameters()[0];
            if(this.clazz != ReflectionUtil.toPrimitive(parameter.getType()))
                continue;

            if(boolean.class != ReflectionUtil.toPrimitive(method.getReturnType()))
                continue;

            add(param -> {
                try {
                    return (boolean) method.invoke(obj, param);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }

        return this;
    }

    /**
     * Load filters from class
     * @param clazz Class<?>
     */
    public Filter<T> add(Class<?> clazz) {
        Object obj = DataInitializer.newInstance(clazz);
        if(obj != null)
            add(obj);

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
