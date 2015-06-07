package com.atlassian.webdriver.rule;

import com.atlassian.webdriver.testing.annotation.WindowSize;
import com.atlassian.webdriver.testing.rule.WindowSizeRule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.atlassian.webdriver.testing.rule.WindowSizeRule}.
 *
 * @since 2.1
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWindowSizeRule
{

    @Mock private Statement mockTest;
    @Mock private WebDriver webDriver;
    @Mock private WebDriver.Options wedDriverOptions;
    @Mock private WebDriver.Window wedDriverWindow;
    @Mock private WindowSize mockWindowSize;

    @Before
    public void stubWebDriverOptions()
    {
        when(webDriver.manage()).thenReturn(wedDriverOptions);
        when(wedDriverOptions.window()).thenReturn(wedDriverWindow);
    }

    @Test
    public void shouldMaximizeWindowIfAnnotationNotPresent()
    {
        final Description description = Description.createTestDescription(TestWindowSizeRule.class, "testMethod");
        new SafeStatementInvoker(createRule().apply(mockTest, description)).invokeSafely();
        verify(wedDriverWindow).maximize();
    }

    @Test
    public void shouldMaximizeWindowIfMaximizeIsTrueOnAnnotation()
    {
        when(mockWindowSize.annotationType()).thenReturn((Class)WindowSize.class); // generics fail :P
        when(mockWindowSize.maximize()).thenReturn(true);
        when(mockWindowSize.width()).thenReturn(1024); // should be ignored
        when(mockWindowSize.height()).thenReturn(768); // should be ignored
        final Description description = Description.createTestDescription(TestWindowSizeRule.class, "testMethod", mockWindowSize);
        new SafeStatementInvoker(createRule().apply(mockTest, description)).invokeSafely();
        verify(wedDriverWindow).maximize();
        verifyNoMoreInteractions(wedDriverWindow);
    }

    @Test
    public void shouldSetWindowSizeIfMaximizeIsFalseOnAnnotation()
    {
        when(mockWindowSize.annotationType()).thenReturn((Class)WindowSize.class); // generics fail :P
        when(mockWindowSize.maximize()).thenReturn(false);
        when(mockWindowSize.width()).thenReturn(1024);
        when(mockWindowSize.height()).thenReturn(768);
        final Description description = Description.createTestDescription(TestWindowSizeRule.class, "testMethod", mockWindowSize);
        new SafeStatementInvoker(createRule().apply(mockTest, description)).invokeSafely();
        verify(wedDriverWindow).setPosition(new Point(0, 0));
        verify(wedDriverWindow, times(2)).setSize(new Dimension(1024, 768));
        verifyNoMoreInteractions(wedDriverWindow);
    }

    @Test
    public void shouldSetWindowSizeIfMaximizeIsFalseOnClassLevelAnnotation()
    {
        final Description description = Description.createTestDescription(ClassAnnotatedWithWindowSize.class, "testMethod");
        new SafeStatementInvoker(createRule().apply(mockTest, description)).invokeSafely();
        verify(wedDriverWindow).setPosition(new Point(0, 0));
        verify(wedDriverWindow , times(2)).setSize(new Dimension(1024, 768));
        verifyNoMoreInteractions(wedDriverWindow);
    }

    @Test
    public void shouldPreferMethodLevelAnnotationOverClassLevel()
    {
        when(mockWindowSize.annotationType()).thenReturn((Class)WindowSize.class); // generics fail :P
        when(mockWindowSize.maximize()).thenReturn(true); // this should take precedence over @WindowSize in ClassAnnotatedWithWindowSize
        final Description description = Description.createTestDescription(ClassAnnotatedWithWindowSize.class, "testMethod", mockWindowSize);
        new SafeStatementInvoker(createRule().apply(mockTest, description)).invokeSafely();
        verify(wedDriverWindow).maximize();
        verifyNoMoreInteractions(wedDriverWindow);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfHeightInvalid() throws Throwable
    {
        when(mockWindowSize.annotationType()).thenReturn((Class)WindowSize.class); // generics fail :P
        when(mockWindowSize.maximize()).thenReturn(false);
        when(mockWindowSize.width()).thenReturn(1024);
        when(mockWindowSize.height()).thenReturn(0);
        final Description description = Description.createTestDescription(TestWindowSizeRule.class, "testMethod", mockWindowSize);
        createRule().apply(mockTest, description).evaluate();
    }


    private WindowSizeRule createRule()
    {
        return new WindowSizeRule(webDriver);
    }

    @WindowSize(width = 1024, height = 768)
    public static class ClassAnnotatedWithWindowSize
    {

    }

}
