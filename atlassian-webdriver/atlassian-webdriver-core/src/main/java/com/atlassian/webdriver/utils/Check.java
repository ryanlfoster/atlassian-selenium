package com.atlassian.webdriver.utils;

import com.atlassian.annotations.PublicApi;
import com.atlassian.webdriver.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utilities for doing simple checks on a page.
 *
 * @since 2.0
 */
@PublicApi
public final class Check
{

    private Check()
    {
        throw new AssertionError(Check.class.getName() + " is not supposed to be instantiated");
    }

    /**
     * Checks that an element that matches the by param exists within another element.
     */
    public static boolean elementExists(By by, SearchContext el)
    {
        try
        {
            el.findElement(by);
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
        return true;
    }

    public static boolean elementIsVisible(By by, SearchContext context)
    {
        try
        {
            WebElement lookFor = context.findElement(by);
            return isVisible(lookFor);
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }

    public static boolean elementsAreVisible(By by, SearchContext context)
    {
        List<WebElement> elements = context.findElements(by);

        if (elements.size() > 0)
        {
            for (WebElement lookFor : elements)
            {
                if (!isVisible(lookFor))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private static boolean isVisible(WebElement webElement)
    {
        try
        {
            return webElement.isDisplayed();
        }
        catch (StaleElementReferenceException e)
        {
            // element got stale since it's been retrieve, just return false naively
            return false;
        }
    }

    /**
     * Checks to see if a specified element contains a specific class of not. The check is case-insensitive.
     *
     * @param className CSS class name to check for
     * @param element element to check
     * @return <code>true</code>, if <tt>element</tt> has CSS class with given <tt>className</tt>
     *
     * @see Elements#getCssClasses(String)
     * @see Elements#hasCssClass(String, String)
     */
    public static boolean hasClass(@Nonnull String className, @Nonnull WebElement element)
    {
        checkNotNull(element, "element");

        return Elements.hasCssClass(className, element.getAttribute(Elements.ATTRIBUTE_CLASS));
    }
}