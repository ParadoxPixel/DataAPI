package nl.iobyte.dataapi.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtil {

    /**
     * Get constructor from parameters
     * @param clazz Class<?>
     * @param objects Object[]
     * @return Constructor<?>
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Object... objects) {
        if(objects == null)
            objects = new Object[0];

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

        return constructor;
    }

    /**
     * Get method from array that matches name and parameters
     * @param methods Method[]
     * @param name String
     * @param objects Object[]
     * @return Method
     */
    public static Method getMethod(Method[] methods, String name, Object[] objects) {
        if(objects == null)
            objects = new Object[0];

        Class<?> a,b;
        Class<?>[] parameterTypes;
        for(Method m : methods) {
            if(!m.getName().equals(name))
                continue;

            if(m.getParameterCount() != objects.length)
                continue;

            if(objects.length == 0)
                return m;

            parameterTypes = m.getParameterTypes();
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
                    return m;
            }
        }

        return null;
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
