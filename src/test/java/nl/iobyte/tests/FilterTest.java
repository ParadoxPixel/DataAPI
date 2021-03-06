package nl.iobyte.tests;

import nl.iobyte.dataapi.filter.Filter;
import nl.iobyte.dataapi.filter.annotations.FilterCheck;
import org.junit.Assert;
import org.junit.Test;
import java.util.Objects;

public class FilterTest {

    @Test
    public void test() {
        Filter<String> filter = Filter.of(String.class)
                .add(Objects::nonNull)
                .add(str -> !str.isEmpty());

        Assert.assertTrue("Filter should return true", filter.check("Test"));
        Assert.assertFalse("Filter should return false", filter.check(null));
        Assert.assertFalse("Filter should return false", filter.check(""));

        filter = Filter.of(String.class)
                .add(TestFilters.class);

        Assert.assertTrue("Filter should return true", filter.check("Test"));
        Assert.assertFalse("Filter should return false", filter.check(null));
        Assert.assertFalse("Filter should return false", filter.check(""));
    }

    public static class TestFilters {

        @FilterCheck
        public boolean nonNull(String str) {
            return str != null && !str.isEmpty();
        }

    }

}
