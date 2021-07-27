package nl.iobyte.tests;

import nl.iobyte.dataapi.injector.Injector;
import nl.iobyte.dataapi.injector.annotations.Inject;
import nl.iobyte.dataapi.event.interfaces.IEvent;
import org.junit.Assert;
import org.junit.Test;

public class InjectorTest {

    @Test
    public void test() {
        Injector injector = new Injector();

        //Object
        injector.register(String.class, () -> "Some value");
        Assert.assertTrue("Object not found in register", injector.has(String.class));

        //Interface
        injector.register(IEvent.class, clazz -> new IEvent() {});
        Assert.assertTrue("Interface not found in register", injector.has(IEvent.class));

        //Injection
        DependencyTest test = injector.inject(DependencyTest.class);
        Assert.assertNotNull("Inject must return instance", test);
        Assert.assertNotNull("Object injection failed", test.testString);
        Assert.assertNotNull("Interface injection failed", test.testInterface);
    }

    public static class DependencyTest {

        @Inject
        public String testString;

        @Inject
        public IEvent testInterface;

    }

}
