package com.atlassian.webdriver.utils.element;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Factory for element conditions.
 *
 * @since 2.2
 * @see ExpectedCondition
 */
public final class ElementConditions
{

    private ElementConditions()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static ElementLocated isPresent(By locator, SearchContext context)
    {
        return new ElementLocated(locator, context);
    }

    public static ElementLocated isPresent(By locator)
    {
        return new ElementLocated(locator);
    }

    public static ElementNotLocated isNotPresent(By locator, SearchContext context)
    {
        return new ElementNotLocated(locator, context);
    }

    public static ElementNotLocated isNotPresent(By locator)
    {
        return new ElementNotLocated(locator);
    }

    public static ElementIsVisible isVisible(By locator, SearchContext context)
    {
        return new ElementIsVisible(locator, context);
    }

    public static ElementIsVisible isVisible(By locator)
    {
        return new ElementIsVisible(locator);
    }

    public static ElementNotVisible isNotVisible(By locator, SearchContext context)
    {
        return new ElementNotVisible(locator, context);
    }

    public static ElementNotVisible isNotVisible(By locator)
    {
        return new ElementNotVisible(locator);
    }


}
