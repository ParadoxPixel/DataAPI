package nl.iobyte.dataapi.dependency;

import nl.iobyte.dataapi.dependency.annotations.Inject;
import nl.iobyte.dataapi.dependency.interfaces.InterfaceSupplier;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class DependencyInjector {

    private final Map<Class<?>, Supplier<?>> dependencies = new ConcurrentHashMap<>();
    private final Map<Class<?>, InterfaceSupplier<?>> interfaces = new ConcurrentHashMap<>();

    /**
     * Register dependency
     * @param clazz Class<T>
     * @param supplier Supplier<T>
     * @param <T> T
     */
    public <T> void register(Class<T> clazz, Supplier<T> supplier) {
        dependencies.put(clazz, supplier);
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
        return (Supplier<T>) dependencies.get(clazz);
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
     * Unregister dependency
     * @param clazz Class<?>
     */
    public void unregister(Class<?> clazz) {
        dependencies.remove(clazz);
    }

    /**
     * Inject dependencies into object
     * @param obj Object
     */
    public void inject(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(!field.isAnnotationPresent(Inject.class))
                continue;

            getOptional(field.getType()).ifPresentOrElse(v -> {
                try {
                    field.setAccessible(true);
                    field.set(obj, v.get());
                } catch (Exception ignored) { }
            }, () -> {
                for(Map.Entry<Class<?>, InterfaceSupplier<?>> entry : interfaces.entrySet()) {
                    if(!entry.getKey().isAssignableFrom(field.getType()))
                        continue;

                    Object v = entry.getValue().get(field.getType());
                    if(v == null)
                        continue;

                    try {
                        field.setAccessible(true);
                        field.set(obj, v);
                    } catch (Exception ignored) { }
                }
            });
        }
    }

    /**
     * Get new instance of class after injecting dependencies
     * @param clazz Class<T>
     * @param <T> T
     * @return T
     */
    public <T> T inject(Class<T> clazz) {
        T instance;
        try {
            instance = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }

        inject(instance);
        return instance;
    }

}
