package nl.iobyte.dataapi.service;

import nl.iobyte.dataapi.service.interfaces.IService;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private final Map<Class<? extends IService>, IService> services = new ConcurrentHashMap<>();

    /**
     * Register a service
     * @param clazz Class<? extends IService>
     * @param <T> IService
     */
    @SuppressWarnings("unchecked")
    public <T extends IService> T register(Class<T> clazz) {
        if(services.containsKey(clazz))
            return (T) services.get(clazz);

        T instance;
        try {
            instance = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }

        services.put(clazz, instance);
        instance.init();
        return instance;
    }

    /**
     * Register multiple services
     * @param classes Class<? extends IService>[]
     */
    @SafeVarargs
    public final void registerBulk(Class<? extends IService>... classes) {
        for(Class<? extends IService> clazz : classes)
            register(clazz);
    }

    /**
     * Check if has service
     * @param clazz Class<? extends IService>
     * @param <T> IService
     * @return Boolean
     */
    public <T extends IService> boolean has(Class<T> clazz) {
        return services.containsKey(clazz);
    }

    /**
     * Get a service
     * @param clazz Class<? extends IService>
     * @param <T> IService
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T extends IService> T get(Class<T> clazz) {
        return (T) services.get(clazz);
    }

    /**
     * Get an optional service
     * @param clazz Class<? extends IService>
     * @param <T> IService
     * @return Optional<T>
     */
    public <T extends IService> Optional<T> getOptional(Class<T> clazz) {
        return Optional.ofNullable(get(clazz));
    }

    /**
     * Unregister a service
     * @param clazz Class<? extends IService>
     */
    public void unregister(Class<? extends IService> clazz) {
        services.remove(clazz);
    }

    /**
     * Unregister multiple services
     * @param classes Class<? extends IService>[]
     */
    @SafeVarargs
    public final void unregisterBulk(Class<? extends IService>... classes) {
        for(Class<? extends IService> clazz : classes)
            unregister(clazz);
    }

}
