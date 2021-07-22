package nl.iobyte.dataapi.event;

import nl.iobyte.dataapi.event.annotations.EventHandler;
import nl.iobyte.dataapi.event.interfaces.IEvent;
import nl.iobyte.dataapi.event.interfaces.IEventHandler;
import nl.iobyte.dataapi.event.objects.Listener;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventDriver {

    private final Map<Class<? extends IEvent>, Listener<?>> listeners = new ConcurrentHashMap<>();

    /**
     * Register a listener from object
     * @param obj Object
     * @return EventDriver
     */
    @SuppressWarnings("unchecked")
    public EventDriver register(Object obj) {
        Parameter parameter;
        for(Method method : obj.getClass().getMethods()) {
            if(method.getParameterCount() != 1)
                continue;

            if(!method.isAnnotationPresent(EventHandler.class))
                continue;

            if((method.getModifiers() & Modifier.PUBLIC) == 0)
                continue;

            parameter = method.getParameters()[0];
            if(!IEvent.class.isAssignableFrom(parameter.getType()))
                continue;

            on((Class<? extends IEvent>) parameter.getType(), event -> {
                try {
                    method.invoke(obj, event);
                } catch (Exception ignored) { }
            });
        }

        return this;
    }

    /**
     * Register multiple listeners for object
     * @param objects Object
     * @return EventDriver
     */
    public EventDriver register(Object... objects) {
        for(Object obj : objects)
            register(obj);

        return this;
    }

    /**
     * Register a listener from class
     * @param clazz Class<?>
     * @return EventDriver
     */
    public EventDriver register(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return this;
        }

        return register(obj);
    }

    /**
     * Register multiple listeners from class
     * @param classes Class<?>[]
     * @return EventDriver
     */
    public EventDriver register(Class<?>... classes) {
        for(Class<?> clazz : classes)
            register(clazz);

        return this;
    }

    /**
     * Get listener for event
     * @param event Class<T>
     * @param <T> IEvent
     * @return Listener<T>
     */
    @SuppressWarnings("unchecked")
    public <T extends IEvent> Listener<T> on(Class<T> event) {
        Listener<T> listener = (Listener<T>) listeners.getOrDefault(event, new Listener<>());
        listeners.put(event, listener);
        return listener;
    }

    /**
     * Register handler for event
     * @param event Class<T>
     * @param handler EventHandler<T>
     * @param <T> IEvent
     * @return EventDriver
     */
    public <T extends IEvent> EventDriver on(Class<T> event, IEventHandler<T> handler) {
        on(event).addHandler(handler);
        return this;
    }

    /**
     * Called when firing an event
     * @param event IEvent
     */
    public void fire(IEvent event) {
        if(event == null)
            return;

        Class<?> c1 = event.getClass();
        Class<?> c2;
        for(Map.Entry<Class<? extends IEvent>, Listener<?>> entrySet : listeners.entrySet()) {
            c2 = entrySet.getKey();
            if(!c2.isInstance(event) && !c2.isAssignableFrom(c1))
                continue;

            try {
                entrySet.getValue().fire(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}