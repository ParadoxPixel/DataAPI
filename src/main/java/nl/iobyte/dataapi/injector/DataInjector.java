package nl.iobyte.dataapi.injector;

import nl.iobyte.dataapi.initializer.DataInitializer;
import nl.iobyte.dataapi.injector.annotations.Inject;
import nl.iobyte.dataapi.injector.interfaces.InterfaceSupplier;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class DataInjector {

    private final Map<Class<?>, Supplier<?>> entries = new ConcurrentHashMap<>();
    private final Map<Class<?>, InterfaceSupplier<?>> interfaces = new ConcurrentHashMap<>();

    /**
     * Register dependency
     * @param clazz Class<T>
     * @param supplier Supplier<T>
     * @param <T> T
     */
    public <T> void register(Class<T> clazz, Supplier<T> supplier) {
        entries.put(clazz, supplier);
    }

    /**
     * Register dependency
     * @param clazz Class<T>
     * @param supplier Supplier<T>
     * @param <T> T
     */
    public <T> void register(Class<T> clazz, InterfaceSupplier<T> supplier) {
        interfaces.put(clazz, supplier);
    }

    /**
     * Get supplier for dependency
     * @param clazz Class<T>
     * @param <T> T
     * @return Supplier<T>
     */
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> get(Class<T> clazz) {
        return (Supplier<T>) entries.get(clazz);
    }

    /**
     * Get optional supplier for dependency
     * @param clazz Class<T>
     * @param <T> T
     * @return Optional<Supplier<T>>
     */
    public <T> Optional<Supplier<T>> getOptional(Class<T> clazz) {
        return Optional.ofNullable(get(clazz));
    }

    /**
     * Get value from class
     * @param clazz Class<T>
     * @param <T> T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz) {
        AtomicReference<T> instance = new AtomicReference<>();
        getOptional(clazz).ifPresentOrElse(v -> instance.set(v.get()), () -> {
            for(Map.Entry<Class<?>, InterfaceSupplier<?>> entry : interfaces.entrySet()) {
                if(!entry.getKey().isAssignableFrom(clazz))
                    continue;

                Object v = entry.getValue().get(clazz);
                if(v == null)
                    continue;

                instance.set((T) v);
            }
        });

        return instance.get();
    }

    /**
     * Check if class is present in injector
     * @param clazz Class<?>
     * @return Boolean
     */
    public boolean has(Class<?> clazz) {
        if(entries.containsKey(clazz))
            return true;

        return interfaces.containsKey(clazz);
    }

    /**
     * Unregister dependency
     * @param clazz Class<?>
     */
    public void unregister(Class<?> clazz) {
        entries.remove(clazz);
    }

    /**
     * Inject dependencies into object
     * @param obj Object
     */
    public void inject(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(!field.isAnnotationPresent(Inject.class))
                continue;

            Object v = getValue(field.getType());
            if(v == null)
                continue;

            try {
                field.setAccessible(true);
                field.set(obj, v);
            } catch (Exception ignored) { }
        }
    }

    /**
     * Get new instance of class after injecting dependencies
     * @param clazz Class<T>
     * @param <T> T
     * @return T
     */
    public <T> T inject(Class<T> clazz) {
        T instance = DataInitializer.newInstance(clazz);
        if(instance == null)
            return null;

        inject(instance);
        return instance;
    }

}
