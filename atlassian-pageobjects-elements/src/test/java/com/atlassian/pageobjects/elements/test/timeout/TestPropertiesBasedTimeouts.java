package com.atlassian.pageobjects.elements.test.timeout;

import com.atlassian.pageobjects.elements.timeout.PropertiesBasedTimeouts;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link com.atlassian.pageobjects.elements.timeout.PropertiesBasedTimeouts}.
 *
 */
public class TestPropertiesBasedTimeouts
{

    @Test
    public void shouldReturnExistingProperties()
    {
        Properties input = fromMap(ImmutableMap.of(
                "com.atlassian.timeout.DEFAULT", "500",
                "com.atlassian.timeout.UI_ACTION", "300",
                "com.atlassian.timeout.PAGE_LOAD", "400",
                "com.atlassian.timeout.WRONG_KEY", "700")
        );
        final PropertiesBasedTimeouts tested = new PropertiesBasedTimeouts(input);
        assertEquals(500L, tested.timeoutFor(TimeoutType.DEFAULT));
        assertEquals(300L, tested.timeoutFor(TimeoutType.UI_ACTION));
        assertEquals(400L, tested.timeoutFor(TimeoutType.PAGE_LOAD));
        // default for interval is hardcoded
        assertEquals(100L, tested.timeoutFor(TimeoutType.EVALUATION_INTERVAL));
        // the rest should be default
        assertEquals(500L, tested.timeoutFor(TimeoutType.SLOW_PAGE_LOAD));
        assertEquals(500L, tested.timeoutFor(TimeoutType.DIALOG_LOAD));
        assertEquals(500L, tested.timeoutFor(TimeoutType.COMPONENT_LOAD));
        assertEquals(500L, tested.timeoutFor(TimeoutType.AJAX_ACTION));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionGivenNoDefaultTimeoutInProperties()
    {
        Properties withoutDefaultTimeout = fromMap(ImmutableMap.of(
                "com.atlassian.timeout.UI_ACTION", "300",
                "com.atlassian.timeout.PAGE_LOAD", "400",
                "com.atlassian.timeout.WRONG_KEY", "700")
        );
        new PropertiesBasedTimeouts(withoutDefaultTimeout);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionGivenDefaultTimeoutInPropertiesIsNaN()
    {
        Properties withDefaultTimeoutNaN = fromMap(ImmutableMap.of(
                "com.atlassian.timeout.DEFAULT", "not really a number, is it?",
                "com.atlassian.timeout.PAGE_LOAD", "400",
                "com.atlassian.timeout.WRONG_KEY", "700")
        );
        new PropertiesBasedTimeouts(withDefaultTimeoutNaN);
    }

    @Test
    public void shouldReturnDefaultValueForNaN()
    {
        Properties withANaN = fromMap(ImmutableMap.of(
                "com.atlassian.timeout.DEFAULT", "500",
                "com.atlassian.timeout.UI_ACTION", "oh no, it's a NaN!",
                "com.atlassian.timeout.PAGE_LOAD", "400",
                "com.atlassian.timeout.WRONG_KEY", "700")
        );
        final PropertiesBasedTimeouts tested = new PropertiesBasedTimeouts(withANaN);
        assertEquals(500L, tested.timeoutFor(TimeoutType.DEFAULT));
        assertEquals(500L, tested.timeoutFor(TimeoutType.UI_ACTION));
    }

    private Properties fromMap(Map<String,String> input)
    {
        Properties answer = new Properties();
        answer.putAll(input);
        return answer;
    }

}
