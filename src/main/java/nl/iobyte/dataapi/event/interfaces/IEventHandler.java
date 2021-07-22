package nl.iobyte.dataapi.event.interfaces;

public interface IEventHandler<T> {

    /**
     * Called when the event is fired
     * @param event T
     */
    void fire(T event);

}
