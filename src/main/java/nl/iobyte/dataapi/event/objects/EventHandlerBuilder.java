package nl.iobyte.dataapi.event.objects;

import nl.iobyte.dataapi.event.EventDriver;
import nl.iobyte.dataapi.event.interfaces.IEvent;
import nl.iobyte.dataapi.event.interfaces.IEventHandler;
import nl.iobyte.dataapi.filter.Filter;
import java.util.function.Function;

public class EventHandlerBuilder<T extends IEvent> {

    private final EventDriver driver;
    private final Class<T> clazz;
    private final Filter<T> filter = new Filter<>();

    public EventHandlerBuilder(EventDriver driver, Class<T> clazz) {
        this.driver = driver;
        this.clazz = clazz;
    }

    /**
     * Add filter to handler
     * @param f Function<T, Boolean>
     * @return EventHandlerBuilder<T>
     */
    public EventHandlerBuilder<T> filter(Function<T, Boolean> f) {
        filter.add(f);
        return this;
    }

    /**
     * Register handler
     * @param handler IEventHandler<T>
     */
    public void handler(IEventHandler<T> handler) {
        driver.on(clazz, event -> filter.check(event, result -> {
            if(!result)
                return;

            handler.fire(event);
        }));
    }

}
