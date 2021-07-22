package nl.iobyte.dataapi.event.objects;

import nl.iobyte.dataapi.event.interfaces.IEventHandler;
import java.util.LinkedHashSet;
import java.util.Set;

public class Listener<T> {

    private final Set<IEventHandler<T>> executors = new LinkedHashSet<>();

    /**
     * Add handler for event
     * @param executor IEventHandler<T>
     * @return Listener<T>
     */
    public Listener<T> addHandler(IEventHandler<T> executor) {
        executors.add(executor);
        return this;
    }

    /**
     * Called when event is fired
     * @param obj T
     */
    @SuppressWarnings("unchecked")
    public void fire(Object obj) {
        T event = (T) obj;
        for (IEventHandler<T> executor : executors) {
            if(executor == null)
                return;

            try {
                executor.fire(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
