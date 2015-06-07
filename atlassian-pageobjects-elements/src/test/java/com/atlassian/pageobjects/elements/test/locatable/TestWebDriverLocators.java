package com.atlassian.pageobjects.elements.test.locatable;

import com.atlassian.pageobjects.elements.WebDriverLocatable;
import com.atlassian.pageobjects.elements.WebDriverLocators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.atlassian.pageobjects.elements.WebDriverLocatable.LocateTimeout.zero;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestWebDriverLocators
{

    @Mock
    private WebDriver webDriver;

    @Mock
    private WebElement webElement;

    @Test
    public void testRoot()
    {
        WebDriverLocatable root = WebDriverLocators.root();
        assertNull(root.getParent());
        assertNull(root.getLocator());
        assertTrue(root.isPresent(webDriver, zero()));
        assertSame(webDriver, root.waitUntilLocated(webDriver, zero()));
    }

    @Test
    public void testSingleLocatorUsesProvidedPollInterval()
    {
        when(webDriver.findElement(By.id("id")))
                .thenThrow(new NoSuchElementException("No"))
                .thenThrow(new NoSuchElementException("No"))
                .thenReturn(webElement);

        WebDriverLocatable locatable = WebDriverLocators.single(By.id("id"));
        assertNotNull(locatable.getParent());

        assertNotNull(locatable.waitUntilLocated(webDriver,
                WebDriverLocatable.LocateTimeout.builder().timeout(10).pollInterval(1).build()));
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleLocatorFailsIfAfterTimeoutExpires()
    {
        when(webDriver.findElement(By.id("id"))).thenThrow(new NoSuchElementException("No"));

        WebDriverLocatable locatable = WebDriverLocators.single(By.id("id"));
        assertNotNull(locatable.getParent());

        final long start = System.currentTimeMillis();
        try
        {
            locatable.waitUntilLocated(webDriver,
                    WebDriverLocatable.LocateTimeout.builder().timeout(5).pollInterval(1).build());
        }
        finally
        {
            // should try for at least 5ms or more
            assertThat(System.currentTimeMillis() - start, greaterThanOrEqualTo(5L));
        }
    }
}
