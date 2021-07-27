package nl.iobyte.dataapi.initializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

        Class<?>[] parameterTypes;
        Class<?> a,b;
        Constructor<?> constructor = null;
        for(Constructor<?> c : clazz.getConstructors()) {
            if(c.getParameterCount() != objects.length)
                continue;

            parameterTypes = c.getParameterTypes();
            for(int i = 0; i < objects.length; i++) {
                a = toPrimitive(parameterTypes[i]);
                if(a.isPrimitive() && objects[i] == null)
                    break;

                if(objects[i] != null) {
                    b = toPrimitive(objects[i].getClass());
                    if (!a.isAssignableFrom(b))
                        break;
                }

                if(i == (objects.length - 1))
                    constructor = c;
            }

            if(constructor != null)
                break;
        }

        if(constructor == null)
            return null;

        try {
            return (T) constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get primitive of class if boolean,short,integer,double,float,long,byte,char
     * @param clazz Class<?>
     * @return Class<?>
     */
    public static Class<?> toPrimitive(Class<?> clazz) {
        if(clazz.isPrimitive() || clazz.isInterface() || clazz.isEnum())
            return clazz;

        if(Boolean.class.equals(clazz))
            return boolean.class;

        if(Short.class.equals(clazz))
            return short.class;

        if(Integer.class.equals(clazz))
            return int.class;

        if(Double.class.equals(clazz))
            return double.class;

        if(Float.class.equals(clazz))
            return float.class;

        if(Long.class.equals(clazz))
            return long.class;

        if(Byte.class.equals(clazz))
            return byte.class;

        if(Character.class.equals(clazz))
            return char.class;

        return clazz;
    }

}
