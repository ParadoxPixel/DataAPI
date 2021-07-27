package nl.iobyte.dataapi.initializer;

public class DataInitializer {

    /**
     * Get instance of class from empty constructor
     * @param clazz Class<T>
     * @param <T> T
     * @return T
     */
    public static <T> T getInstance(Class<T> clazz) {
        if(clazz == null)
            return null;

        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}
