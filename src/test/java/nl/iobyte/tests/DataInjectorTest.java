package nl.iobyte.tests;

import nl.iobyte.dataapi.injector.DataInjector;
import nl.iobyte.dataapi.injector.annotations.Inject;
import nl.iobyte.dataapi.event.interfaces.IEvent;
import org.junit.Assert;
import org.junit.Test;

public class DataInjectorTest {

    @Test
    public void test() {
        DataInjector dataInjector = new DataInjector();

        //Object
        dataInjector.register(String.class, () -> "Some value");
        Assert.assertTrue("Object not found in register", dataInjector.has(String.class));

        //Interface
        dataInjector.register(IEvent.class, clazz -> new IEvent() {});
        Assert.assertTrue("Interface not found in register", dataInjector.has(IEvent.class));

        //Injection
        DependencyTest test = dataInjector.inject(DependencyTest.class);
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
