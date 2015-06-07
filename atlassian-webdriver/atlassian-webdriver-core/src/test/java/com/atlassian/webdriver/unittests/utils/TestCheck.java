package com.atlassian.webdriver.unittests.utils;

import com.atlassian.webdriver.utils.Check;
import com.google.common.collect.ImmutableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

/**
 * @since 2.1.0
 */
public class TestCheck
{
    @Mock WebElement displayedWebElement;
    @Mock WebElement nonDisplayedWebElement;
    @Mock SearchContext singleElementSearchContext;
    @Mock SearchContext noElementsInSearchContext;
    @Mock SearchContext multiElementsSearchContext;

    @Before
    public void setupMocks()
    {
        MockitoAnnotations.initMocks(this);
        when(displayedWebElement.isDisplayed()).thenReturn(true);
        when(nonDisplayedWebElement.isDisplayed()).thenReturn(false);
        when(singleElementSearchContext.findElement(any(By.class))).thenReturn(displayedWebElement);
        when(noElementsInSearchContext.findElement(any(By.class))).thenThrow(new NoSuchElementException("Element does not exists in page"));
        when(noElementsInSearchContext.findElements(any(By.class))).thenReturn(ImmutableList.<WebElement>of());
    }

    @After
    public void resetMocks()
    {
        reset(displayedWebElement,nonDisplayedWebElement,singleElementSearchContext,
                noElementsInSearchContext,multiElementsSearchContext);
    }

    @Test
    public void testCheckElementExists()
    {
        assertTrue(Check.elementExists(By.id("test"), singleElementSearchContext));
    }

    @Test
    public void testCheckElementsNotExists()
    {
        assertFalse(Check.elementExists(By.id("test"), noElementsInSearchContext));
    }

    @Test
    public void testCheckElementIsVisible()
    {
        assertTrue(Check.elementIsVisible(By.id("test"), singleElementSearchContext));
    }

    @Test
    public void testCheckElementsAreVisble()
    {
        when(multiElementsSearchContext.findElements(any(By.class)))
                .thenReturn(ImmutableList.<WebElement>of(displayedWebElement, displayedWebElement));
        assertTrue(Check.elementsAreVisible(By.id("test"), multiElementsSearchContext));
    }

    @Test
    public void testCheckNoElementsFoundAreNotVisible()
    {
        assertFalse(Check.elementsAreVisible(By.id("test"), noElementsInSearchContext));
    }

    @Test
    public void testCheckElementsWithDifferentDisplaysAreNotVisible()
    {
        when(multiElementsSearchContext.findElements(any(By.class)))
            .thenReturn(ImmutableList.<WebElement>of(displayedWebElement, nonDisplayedWebElement));
        assertFalse(Check.elementsAreVisible(By.id("test"), multiElementsSearchContext));
    }

    @Test
    public void testHasClassOnSingleClassName()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("test-class");
        assertTrue(Check.hasClass("test-class", displayedWebElement));
    }

    @Test
    public void testHasClassWithMultipleClassNames()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("class-a test-class class-b");
        assertTrue(Check.hasClass("class-a", displayedWebElement));
        assertTrue(Check.hasClass("test-class", displayedWebElement));
        assertTrue(Check.hasClass("class-b", displayedWebElement));
    }

    @Test
    public void testNoClassFound()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("");
        assertFalse(Check.hasClass("test-class", displayedWebElement));
    }

    @Test
    public void testClassIsNullIsFalse()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn(null);
        assertFalse(Check.hasClass("test-class", displayedWebElement));
    }

    @Test
    public void testCaseInsestiveMatchesAreValid()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("TEST-CLASS");
        assertTrue(Check.hasClass("test-class", displayedWebElement));
    }

    @Test
    public void testOverlappedClassNameDoesNotMatch()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("test-class-a");
        assertFalse(Check.hasClass("test-class", displayedWebElement));
    }

    @Test
    public void testMultiOverlappedClassNameDoesNotMatch()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("test-class-a test-class-b test-test-class");
        assertFalse(Check.hasClass("test-class", displayedWebElement));
    }

    @Test
    public void testMultiClassesWithNoMatch()
    {
        when(displayedWebElement.getAttribute("class")).thenReturn("test-a test-b test-c");
        assertFalse(Check.hasClass("test-class", displayedWebElement));
    }


}
