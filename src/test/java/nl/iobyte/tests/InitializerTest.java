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
        Assert.assertNotNull("getInstance without objects should not return null", test);
    }

    @Test
    public void testMap() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class, new HashMap<>(){{
            put("str", "Some String");
        }});
        Assert.assertNotNull("getInstance with map should not return null", test);
        Assert.assertNotNull("Initializing field failed", test.str);
    }

    @Test
    public void testObjects() {
        InstanceTest test = DataInitializer.newInstance(InstanceTest.class, "String #1", 1, 1.0, false, null);
        Assert.assertNotNull("getInstance with objects should not return null", test);
    }

    @Test
    public void testMethod() {
        String str = DataInitializer.callMethod(InstanceTest.class, "testString");
        Assert.assertNotNull("callMethod should not return null", str);

        //No error case since it's a void
        DataInitializer.callMethod(
                DataInitializer.newInstance(InstanceTest.class),
                "test"
        );

        str = DataInitializer.callMethod(InstanceTest.class, "testString", "Something");//Shouldn't exist
        Assert.assertNull("callMethod should return null", str);

        str = DataInitializer.callMethod(
                DataInitializer.newInstance(InstanceTest.class),
                "test",
                "Something"
        );
        Assert.assertNotNull("callMethod with objects should not return null", str);
    }

    public static class InstanceTest {

        public String str;

        public InstanceTest() { }

        public InstanceTest(String str, int i, double d, boolean b, InterfaceSupplier<?> event) { }

        public static String testString() {
            return "test";
        }

        private void test() {

        }

        private String test(String str) {
            return str;
        }

    }

}
