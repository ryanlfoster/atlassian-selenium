package com.atlassian.pageobjects.elements.test.query;

import com.atlassian.pageobjects.elements.WebDriverLocatable;
import com.atlassian.pageobjects.elements.WebDriverLocators;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.pageobjects.elements.query.webdriver.WebDriverQueryFactory;
import com.atlassian.pageobjects.elements.timeout.MapBasedTimeouts;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.atlassian.pageobjects.elements.timeout.Timeouts;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static com.atlassian.pageobjects.elements.query.Poller.by;
import static com.atlassian.pageobjects.elements.query.Poller.byDefaultTimeout;
import static com.atlassian.pageobjects.elements.query.Poller.now;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.atlassian.pageobjects.elements.query.webdriver.WebDriverQueryFactory}.
 *
 */
public class TestWebDriverQueryFactory
{

    private Timeouts timeouts = new MapBasedTimeouts(ImmutableMap.<TimeoutType, Long>of(
            TimeoutType.DEFAULT, 500L,
            TimeoutType.EVALUATION_INTERVAL, 100L,
            TimeoutType.COMPONENT_LOAD, 200L
    ));

    @Test
    public void shouldSetUpQueryWithCustomDefaultTimeout()
    {
        WebDriverLocatable locatable = singleTestLocatable();
        AtlassianWebDriver mockElementPresentDriver = mock(AtlassianWebDriver.class);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(locatable, timeouts, mockElementPresentDriver);

        TimedQuery<Boolean> result = tested.isPresent(TimeoutType.COMPONENT_LOAD);
        assertEquals(100L, result.interval());
        assertEquals(200L, result.defaultTimeout());
    }

    @Test
    public void shouldReturnValidIsPresentQuery()
    {
        WebElement mockElement = newMockElement();
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenReturn(mockElement);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        TimedQuery<Boolean> result = tested.isPresent();
        assertEquals(100L, result.interval());
        assertEquals(500L, result.defaultTimeout());
        Poller.waitUntil(result, is(true), now());
    }

    @Test
    public void shouldReturnFalseIsPresentQuery()
    {
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenThrow(new NoSuchElementException("Cause you've got issues"));
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        TimedQuery<Boolean> result = tested.isPresent();
        assertEquals(100L, result.interval());
        assertEquals(500L, result.defaultTimeout());
        Poller.waitUntil(result, is(false), by(1000));
    }

    @Test
    public void shouldReturnIsVisibleQueryThatIsTrueNow()
    {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.isDisplayed()).thenReturn(true);
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenReturn(mockElement);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        TimedQuery<Boolean> result = tested.isVisible();
        assertEquals(100L, result.interval());
        assertEquals(500L, result.defaultTimeout());
        Poller.waitUntil(result, is(true), now());
    }

    @Test
    public void shouldReturnIsVisibleQueryThatIsTrueInAWhile()
    {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.isDisplayed()).thenReturn(false, false, false, false, true);
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenReturn(mockElement);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        TimedQuery<Boolean> result = tested.isVisible();
        assertEquals(100L, result.interval());
        assertEquals(500L, result.defaultTimeout());
        Poller.waitUntil(result, is(false), now());
        Poller.waitUntil(result, is(true), by(1000));
    }

    @Test
    public void shouldReturnIsVisibleQueryThatIsFalse()
    {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.isDisplayed()).thenReturn(false);
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenReturn(mockElement);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        TimedQuery<Boolean> result = tested.isVisible();
        assertEquals(100L, result.interval());
        assertEquals(500L, result.defaultTimeout());
        Poller.waitUntil(result, is(false), by(1000));
    }

    @Test
    public void hasClassQueryShouldReturnTrueForSimpleMatch()
    {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.getAttribute("class")).thenReturn("oneclass secondclass someotherclasssss");
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenReturn(mockElement);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        Poller.waitUntil(tested.hasClass("oneclass"), is(true), now());
        Poller.waitUntil(tested.hasClass("secondclass"), is(true), now());
        Poller.waitUntil(tested.hasClass("someotherclasssss"), is(true), now());
        Poller.waitUntil(tested.hasClass("blahblah"), is(false), byDefaultTimeout());
    }

    @Test
    public void hasClassQueryShouldBeCaseInsensitive()
    {
        WebElement mockElement = mock(WebElement.class);
        when(mockElement.getAttribute("class")).thenReturn("oneclass secOndclAss soMeotherclasSsss");
        AtlassianWebDriver mockDriver = mock(AtlassianWebDriver.class);
        when(mockDriver.findElement(any(By.class))).thenReturn(mockElement);
        WebDriverQueryFactory tested = new WebDriverQueryFactory(singleTestLocatable(), timeouts, mockDriver);

        Poller.waitUntil(tested.hasClass("Oneclass"), is(true), now());
        Poller.waitUntil(tested.hasClass("SECondclAss"), is(true), now());
        Poller.waitUntil(tested.hasClass("someotherclaSSSSS"), is(true), now());
    }

    private WebDriverLocatable singleTestLocatable()
    {
        return WebDriverLocators.single(By.id("test"));
    }

    private WebElement newMockElement()
    {
        return mock(WebElement.class);
    }
}
