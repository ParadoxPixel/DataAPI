package nl.iobyte.tests;

import nl.iobyte.dataapi.namespace.NamespaceMap;
import org.junit.Assert;
import org.junit.Test;
import java.util.Set;

public class NamespaceTest {

    @Test
    public void test() {
        NamespaceMap<String> map = new NamespaceMap<>();

        map.set("value.at.path.1", "Test #1");
        map.set("value.at.path.2", "Test #2");
        map.set("value.at.part.2", "Test #3");
        Assert.assertEquals("Size should be 3", 3, map.size());

        Set<String> values = map.get("value.at.p*.2");
        Assert.assertNotNull("Set should not be null", values);
        Assert.assertEquals("Returned set should be of length 2", 2, values.size());

        values = map.get("value.at.**");
        Assert.assertNotNull("Set should not be null", values);
        Assert.assertEquals("Returned set should be of length 3", 3, values.size());

        values = map.remove("value.at.path.*");
        Assert.assertNotNull("Set should not be null", values);
        Assert.assertEquals("Returned set should be of length 2", 2, values.size());

        Assert.assertEquals("Size should be 1", 1, map.size());

        map.remove("value.at.**");
        Assert.assertEquals("Size should be 0", 0, map.size());
    }

}
