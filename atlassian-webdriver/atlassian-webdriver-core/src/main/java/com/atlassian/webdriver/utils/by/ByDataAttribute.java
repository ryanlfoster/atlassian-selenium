package com.atlassian.webdriver.utils.by;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Extensions of {@link org.openqa.selenium.By} to locate elements by HTML5 data-* attribute.
 *
 * @since v2.1
 */
public final class ByDataAttribute
{
    private abstract static class ByData extends By
    {
        /** Override to avoid a stack overflow in {@link Object#toString} and {@link By#hashCode}. */
        @Override
        public String toString()
        {
            return "ByData";
        }
    }

    private ByDataAttribute()
    {
        throw new AssertionError("Don't instantiate me");
    }


    public static By byData(final String attributeName)
    {
        return new ByData()
        {
            @Override
            public List<WebElement> findElements(SearchContext context)
            {
                return context.findElements(By.cssSelector(String.format("*[data-%s]", attributeName)));
            }
        };
    }

    public static By byData(final String attributeName, final String attributeValue)
    {
        return new ByData()
        {
            @Override
            public List<WebElement> findElements(SearchContext context)
            {
                return context.findElements(By.cssSelector(String.format("*[data-%s=\"%s\"]", attributeName, attributeValue)));
            }
        };
    }

    public static By byTagAndData(final String tagName, final String dataAttributeName)
    {
        return new ByData()
        {
            @Override
            public List<WebElement> findElements(SearchContext context)
            {
                return context.findElements(By.cssSelector(String.format("%s[data-%s]", tagName, dataAttributeName)));
            }
        };
    }

    public static By byTagAndData(final String tagName, final String dataAttributeName, final String dataAttributeValue)
    {
        return new ByData()
        {
            @Override
            public List<WebElement> findElements(SearchContext context)
            {
                return context.findElements(By.cssSelector(String.format("%s[data-%s=\"%s\"]", tagName,
                        dataAttributeName, dataAttributeValue)));
            }
        };
    }
}
