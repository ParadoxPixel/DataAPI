package nl.iobyte.tests;

import nl.iobyte.dataapi.initializer.DataInitializer;
import nl.iobyte.dataapi.injector.interfaces.InterfaceSupplier;
import org.junit.Assert;
import org.junit.Test;

public class InitializerTest {

    @Test
    public void testEmpty() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class);
        Assert.assertNotNull("getInstance without objects can not return null", test);
    }

    @Test
    public void testObjects() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class, "String #1", 1, 1.0, false, null);
        Assert.assertNotNull("getInstance with objects can not return null", test);
    }

    public static class InstanceTest {

        public InstanceTest() { }

        public InstanceTest(String str1, int i, double d, boolean b, InterfaceSupplier<?> event) { }

    }

}
