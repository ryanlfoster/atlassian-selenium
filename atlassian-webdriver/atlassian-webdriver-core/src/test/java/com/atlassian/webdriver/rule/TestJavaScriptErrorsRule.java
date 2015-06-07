package com.atlassian.webdriver.rule;

import com.atlassian.webdriver.debug.JavaScriptErrorInfo;
import com.atlassian.webdriver.debug.JavaScriptErrorRetriever;
import com.atlassian.webdriver.testing.rule.JavaScriptErrorsRule;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;

import java.util.List;

import static com.google.common.collect.Iterables.transform;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Test case for {@link com.atlassian.webdriver.testing.rule.JavaScriptErrorsRule}.
 *
 * @since 2.3
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJavaScriptErrorsRule
{
    @Mock private Logger mockLogger;

    private boolean errorRetrievalSupported = true;
    private List<String> errorsFound;

    @Before
    public void setUp()
    {
        errorsFound = Lists.newArrayList();
    }

    @Test
    public void testOutputsSpecialMessageWhenCannotRetrieveConsoleErrorsOnTestFailed() throws Exception
    {
        errorRetrievalSupported = false;

        final WebDriver driver = mock(WebDriver.class);
        final JavaScriptErrorsRule rule = createRule(driver);

        rule.finished(Description.createTestDescription(TestJavaScriptErrorsRule.class, "testMethod"));
        verify(mockLogger).info("Unable to provide console output. Console output is currently only supported on Firefox.");
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void testLogsWhenNoErrorsFound()
    {
        final FirefoxDriver driver = mock(FirefoxDriver.class);
        final JavaScriptErrorsRule rule = createRule(driver);

        rule.finished(Description.createTestDescription(TestJavaScriptErrorsRule.class, "testMethod"));
        verify(mockLogger).info("----- Test '{}' finished with 0 JS errors. ", "testMethod");
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void testLogsWhenErrorsFound()
    {
        final FirefoxDriver driver = mock(FirefoxDriver.class);
        final JavaScriptErrorsRule rule = createRule(driver);

        errorsFound.add("first");
        errorsFound.add("second");

        rule.finished(Description.createTestDescription(TestJavaScriptErrorsRule.class, "testMethod"));
        verify(mockLogger).warn("----- Test '{}' finished with {} JS error(s). ", "testMethod", 2);
        verify(mockLogger).warn("----- START CONSOLE OUTPUT DUMP\n\n{}\n", "error: first\nerror: second");
        verify(mockLogger).warn("----- END CONSOLE OUTPUT DUMP");
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void testCanBeOverriddenToFailTestWhenErrorsFound()
    {
        final FirefoxDriver driver = mock(FirefoxDriver.class);
        final JavaScriptErrorsRule rule = createRule(driver)
                .failOnJavaScriptErrors(true);

        errorsFound.add("TypeError: $ is not a function");

        try
        {
            rule.finished(Description.createTestDescription(TestJavaScriptErrorsRule.class, "testMethod"));
            assertThat("Expected an exception to be thrown, but wasn't", true, equalTo(false));
        }
        catch (RuntimeException e)
        {
            assertThat(e.getMessage(), containsString("Test failed due to javascript errors being detected"));
        }
        verify(mockLogger).warn("----- Test '{}' finished with {} JS error(s). ", "testMethod", 1);
    }

    private JavaScriptErrorsRule createRule(final WebDriver driver)
    {
        return new JavaScriptErrorsRule(driver)
                .errorRetriever(new MockErrorRetriever())
                .logger(mockLogger);
    }

    private class MockErrorRetriever implements JavaScriptErrorRetriever
    {
        @Override
        public boolean isErrorRetrievalSupported()
        {
            return errorRetrievalSupported;
        }
        
        @Override
        public Iterable<JavaScriptErrorInfo> getErrors()
        {
            return transform(errorsFound, new Function<String, JavaScriptErrorInfo>()
                {
                    public JavaScriptErrorInfo apply(final String message)
                    {
                        return new JavaScriptErrorInfo()
                        {
                            public String getDescription()
                            {
                                return "error: " + message;
                            }

                            public String getMessage()
                            {
                                return message;
                            }
                        };
                    }
                });
        }
    }
}
