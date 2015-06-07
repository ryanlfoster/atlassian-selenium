package com.atlassian.webdriver.rule;

import com.atlassian.webdriver.testing.rule.LogPageSourceRule;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.atlassian.webdriver.testing.rule.LogPageSourceRule}.
 *
 * @since 2.2
 */
@RunWith(MockitoJUnitRunner.class)
public class TestLogPageSourceRule
{
    private static final String LOG_PAGE_SOURCE = "atlassian.test.logSourceOnFailure";

    @Mock private WebDriver mockWebDriver;

    @Mock private Logger mockLogger;

    @After
    public void resetLogPageSourceProperty()
    {
        System.setProperty(LOG_PAGE_SOURCE, "");
    }

    @Test
    public void shouldLogSourceOnFailureWhenEnabled()
    {
        enableLogPageSource();
        when(mockWebDriver.getPageSource()).thenReturn("The Source");
        final LogPageSourceRule rule = createRule();
        rule.failed(new RuntimeException(), Description.createTestDescription(TestLogPageSourceRule.class, "testMethod"));
        verify(mockLogger).info("----- Test '{}' Failed. ", "testMethod");
        verify(mockLogger).info("----- START PAGE SOURCE DUMP\n\n\n{}\n\n\n", "The Source");
        verify(mockLogger).info("----- END PAGE SOURCE DUMP");
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void shouldNotLogIfLoggingDisabled()
    {
        resetLogPageSourceProperty();
        createRule().failed(new RuntimeException(), Description.createTestDescription(TestLogPageSourceRule.class, "testMethod"));
        verifyZeroInteractions(mockLogger);
    }

    private void enableLogPageSource()
    {
        System.setProperty(LOG_PAGE_SOURCE, "true");
    }

    private LogPageSourceRule createRule()
    {
        return new LogPageSourceRule(mockWebDriver, mockLogger);
    }
}
