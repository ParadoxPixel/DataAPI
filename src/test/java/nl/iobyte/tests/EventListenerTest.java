package nl.iobyte.tests;

import nl.iobyte.dataapi.event.EventDriver;
import nl.iobyte.dataapi.event.interfaces.IEvent;
import org.junit.Assert;
import org.junit.Test;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventListenerTest {

    @Test
    public void test() {
        AtomicBoolean goodTriggered = new AtomicBoolean(false);
        AtomicBoolean badTriggered = new AtomicBoolean(false);

        EventDriver eventDriver = new EventDriver();

        //Simple handler
        eventDriver.on(FirstTestEvent.class, a -> goodTriggered.set(true));
        eventDriver.on(SecondTestEvent.class, b -> badTriggered.set(true));

        //Complex handler
        eventDriver.on(FirstTestEvent.class)
                .filter(Objects::isNull)
                .handler(a -> badTriggered.set(true));

        eventDriver.fire(new FirstTestEvent());

        Assert.assertTrue(goodTriggered.get());
        Assert.assertFalse(badTriggered.get());
    }

    public static class FirstTestEvent implements IEvent {
        // no data
    }

    public static class SecondTestEvent implements IEvent {
        // no data
    }

}
