package nl.iobyte.dataapi.registry;

import nl.iobyte.dataapi.initializer.DataInitializer;
import java.util.*;
import java.util.function.Consumer;

public class DataRegistry<R> {

    private final Map<Class<? extends R>, R> entries = new LinkedHashMap<>();
    private final List<Consumer<R>> consumers = new ArrayList<>();

    /**
     * Get registered entries
     * @return Collection<R>
     */
    public Collection<R> getEntries() {
        return entries.values();
    }

    /**
     * Add consumer to registry
     * @param consumer Consumer<R>
     */
    public void addHandler(Consumer<R> consumer) {
        consumers.add(consumer);
    }

    /**
     * Register an entry
     * @param clazz Class<? extends R>
     * @param <T> IService
     */
    @SuppressWarnings("unchecked")
    public <T extends R> T register(Class<T> clazz) {
        if(entries.containsKey(clazz))
            return (T) entries.get(clazz);

        T instance = DataInitializer.newInstance(clazz);
        if(instance == null)
            return null;

        entries.put(clazz, instance);
        for(Consumer<R> consumer : consumers)
            consumer.accept(instance);

        return instance;
    }

    /**
     * Register multiple entries
     * @param classes Class<? extends R>[]
     */
    @SafeVarargs
    public final void registerBulk(Class<? extends R>... classes) {
        for(Class<? extends R> clazz : classes)
            if(register(clazz) == null)
                System.out.println("Failed to register class: "+clazz.getCanonicalName());
    }

    /**
     * Register an entry from instance
     * @param obj T
     * @param <T> IService
     */
    @SuppressWarnings("unchecked")
    public <T extends R> T register(T obj) {
        if(entries.containsKey(obj.getClass()))
            return (T) entries.get(obj.getClass());

        entries.put((Class<? extends R>) obj.getClass(), obj);
        for(Consumer<R> consumer : consumers)
            consumer.accept(obj);

        return obj;
    }

    /**
     * Register multiple entries
     * @param objects T[]
     */
    @SafeVarargs
    public final <T extends R> void registerBulk(T... objects) {
        for(T obj : objects)
            register(obj);
    }

    /**
     * Check if has entry
     * @param clazz Class<? extends IService>
     * @param <T> IService
     * @return Boolean
     */
    public <T extends R> boolean has(Class<T> clazz) {
        return entries.containsKey(clazz);
    }

    /**
     * Get an entry
     * @param clazz Class<? extends R>
     * @param <T> IService
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T extends R> T get(Class<T> clazz) {
        return (T) entries.get(clazz);
    }

    /**
     * Get an optional entry
     * @param clazz Class<? extends R>
     * @param <T> IService
     * @return Optional<T>
     */
    public <T extends R> Optional<T> getOptional(Class<T> clazz) {
        return Optional.ofNullable(get(clazz));
    }

    /**
     * Unregister an entry
     * @param clazz Class<? extends R>
     */
    public void unregister(Class<? extends R> clazz) {
        entries.remove(clazz);
    }

    /**
     * Unregister multiple entries
     * @param classes Class<? extends R>[]
     */
    @SafeVarargs
    public final void unregisterBulk(Class<? extends R>... classes) {
        for(Class<? extends R> clazz : classes)
            unregister(clazz);
    }

}
