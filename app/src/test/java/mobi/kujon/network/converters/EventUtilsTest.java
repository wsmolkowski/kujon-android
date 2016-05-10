package mobi.kujon.network.converters;

import org.junit.Assert;
import org.junit.Test;

import static mobi.kujon.utils.EventUtils.getDate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class EventUtilsTest {

    @Test
    public void testGetDate() throws Exception {
        Assert.assertThat(getDate("2016-04-18 13:00:00"), notNullValue());
        Assert.assertThat(getDate("2016-04-18 13:00:00").getTime(), equalTo(1460977200000L));
    }
}