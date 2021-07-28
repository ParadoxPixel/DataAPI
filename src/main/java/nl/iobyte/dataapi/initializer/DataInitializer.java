package nl.iobyte.dataapi.initializer;

import nl.iobyte.dataapi.reflection.ReflectionUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class DataInitializer {

    /**
     * Get instance of class from empty constructor
     * @param clazz Class<T>
     * @param <T> T
     * @return T
     */
    public static <T> T newInstance(Class<T> clazz) {
        if(clazz == null)
            return null;

        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get new instance and set value of fields
     * @param clazz Class<T>
     * @param fields Map<String, Object>
     * @param <T> T
     * @return T
     */
    public static <T> T newInstance(Class<T> clazz, Map<String, Object> fields) {
        T instance = newInstance(clazz);
        if(instance == null)
            return null;

        Field field;
        for(Map.Entry<String, Object> entry : fields.entrySet()) {
            try {
                field = clazz.getField(entry.getKey());
                field.setAccessible(true);
                field.set(instance, entry.getValue());
            } catch (Exception ignored) { }
        }

        return instance;
    }

    /**
     * Get instance of class from constructor
     * @param clazz Class<T>
     * @param objects Object[]
     * @param <T> T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Object... objects) {
        if(clazz == null)
            return null;

        Constructor<?> constructor = ReflectionUtil.getConstructor(clazz, objects);
        if(constructor == null)
            return null;

        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Call static method of class
     * @param clazz Class<?>
     * @param name String
     * @param objects Object[]
     * @param <T> T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T callMethod(Class<?> clazz, String name, Object... objects) {
        Method method = ReflectionUtil.getMethod(clazz.getDeclaredMethods(), name, objects);
        if(method == null)
            return null;

        try {
            method.setAccessible(true);
            return (T) method.invoke(null, objects);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Call method of object
     * @param obj Object
     * @param name String
     * @param objects Object[]
     * @param <T> T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T callMethod(Object obj, String name, Object... objects) {
        Method method = ReflectionUtil.getMethod(obj.getClass().getDeclaredMethods(), name, objects);
        if(method == null)
            return null;

        try {
            method.setAccessible(true);
            return (T) method.invoke(obj, objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
