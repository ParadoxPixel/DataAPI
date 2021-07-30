package nl.iobyte.tests;

import nl.iobyte.dataapi.namespace.NamespaceMap;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class NamespaceTest {

    @Test
    public void test() {
        NamespaceMap<String> map = new NamespaceMap<>();

        map.set("value.at.path.1", "Test #1");
        map.set("value.at.path.2", "Test #2");
        map.set("value.at.part.2", "Test #3");
        Assert.assertEquals("Size should be 3", 3, map.size());

        List<String> values = map.get("value.at.p*.2");
        Assert.assertNotNull("List should not be null", values);
        Assert.assertEquals("Returned list should be of length 2", 2, values.size());

        values = map.remove("value.at.path.*");
        Assert.assertNotNull("List should not be null", values);
        Assert.assertEquals("Returned list should be of length 2", 2, values.size());

        Assert.assertEquals("Size should be 2", 1, map.size());
    }

}
