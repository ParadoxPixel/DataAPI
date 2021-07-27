package nl.iobyte.dataapi.injector.interfaces;

public interface InterfaceSupplier<T> {

    /**
     * Go from clazz to instance
     * @param clazz Class<? extends T>
     * @return T
     */
    T supply(Class<? extends T> clazz);

    /**
     * Go from clazz to instance
     * @param clazz Class<?>
     * @return T
     */
    @SuppressWarnings("unchecked")
    default T get(Class<?> clazz) {
        return supply((Class<? extends T>) clazz);
    }

}
