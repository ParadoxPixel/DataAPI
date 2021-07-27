package nl.iobyte.tests;

import nl.iobyte.dataapi.event.EventDriver;
import nl.iobyte.dataapi.event.interfaces.IEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class EventListenerTest {

    @Test
    public void test() {
        AtomicBoolean goodTriggered = new AtomicBoolean(false);
        AtomicBoolean badTriggered = new AtomicBoolean(false);

        EventDriver eventDriver = new EventDriver();

        eventDriver.on(FirstTestEvent.class)
                .addHandler((e) -> goodTriggered.set(true));

        eventDriver.on(SecondTestEvent.class)
                .addHandler(b -> badTriggered.set(true));

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
