package nl.iobyte.tests;

import nl.iobyte.dataapi.initializer.DataInitializer;
import org.junit.Assert;
import org.junit.Test;

public class InitializerTest {

    @Test
    public void test() {
        InstanceTest test = DataInitializer.getInstance(InstanceTest.class);
        Assert.assertNotNull("getInstance can not return null", test);
    }

    public static class InstanceTest { }

}
