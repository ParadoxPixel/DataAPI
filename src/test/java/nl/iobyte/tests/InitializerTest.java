package nl.iobyte.tests;

import nl.iobyte.dataapi.initializer.DataInitializer;
import nl.iobyte.dataapi.injector.interfaces.InterfaceSupplier;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class InitializerTest {

    @Test
    public void testEmpty() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class);
        Assert.assertNotNull("getInstance without objects can not return null", test);
    }

    @Test
    public void testMap() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class, new HashMap<>(){{
            put("str", "Some String");
        }});
        Assert.assertNotNull("getInstance with map can not return null", test);
        Assert.assertNotNull("Initializing field failed", test.str);
    }

    @Test
    public void testObjects() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class, "String #1", 1, 1.0, false, null);
        Assert.assertNotNull("getInstance with objects can not return null", test);
    }

    public static class InstanceTest {

        public String str;

        public InstanceTest() { }

        public InstanceTest(String str, int i, double d, boolean b, InterfaceSupplier<?> event) { }

    }

}
