package nl.iobyte.tests;

import nl.iobyte.dataapi.registry.DataRegistry;
import org.junit.Assert;
import org.junit.Test;

public class DataRegistryTest {

    @Test
    public void test() {
        DataRegistry<IService> registry = new DataRegistry<>();
        registry.addHandler(IService::onEnable);

        Assert.assertNotNull("Should return instance of service", registry.register(ServiceTest.class));

        ServiceTest test = registry.get(ServiceTest.class);
        Assert.assertNotNull("Failed to register service", test);
        test.onDisable();
    }

    public interface IService {

        default void onEnable() {
            System.out.println("Running onEnable");
        }

        default void onDisable() {}

    }

    public static class ServiceTest implements IService {

        public void onDisable() {
            System.out.println("Running onDisable");
        }

    }

}
