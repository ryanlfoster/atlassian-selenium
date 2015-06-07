package com.atlassian.pageobjects.elements.query.webdriver;

import com.atlassian.annotations.Internal;
import com.atlassian.webdriver.Elements;
import com.atlassian.webdriver.utils.Check;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.atlassian.webdriver.Elements.ATTRIBUTE_CLASS;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Collection of functions for implementing timed queries in WebDriver.
 */
@Internal
public final class WebDriverQueryFunctions
{
    private WebDriverQueryFunctions()
    {
        throw new AssertionError("Don't instantiate me");
    }

    private static abstract class AbstractWebElementFunction<T> implements Function<WebElement,T>
    {
        private final String name;

        public AbstractWebElementFunction(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    private static abstract class AbstractWebElementPredicate extends AbstractWebElementFunction<Boolean>
    {

        public AbstractWebElementPredicate(String name)
        {
            super(name);
        }
    }

    public static Supplier<Boolean> isPresent(final SearchContext searchContext, final By locator)
    {
        return new Supplier<Boolean>()
        {
            public Boolean get()
            {
                return Check.elementExists(locator, searchContext);
            }

            @Override
            public String toString()
            {
                return "isPresent";
            }
        };

    }

    public static Function<WebElement, Boolean> isPresent()
    {
        return new AbstractWebElementPredicate("isPresent")
        {
            public Boolean apply(WebElement from)
            {
                // if we're here, the element was found
                return true;
            }
        };
    }

    public static Function<WebElement, Boolean> isVisible()
    {
        return new AbstractWebElementPredicate("isVisible")
        {
            public Boolean apply(WebElement from)
            {
                return from.isDisplayed();
            }
        };
    }

    public static Function<WebElement, Boolean> isEnabled()
    {
        return new AbstractWebElementPredicate("isEnabled")
        {
            public Boolean apply(WebElement from)
            {
                return from.isEnabled();
            }
        };
    }

    public static Function<WebElement, Boolean> isSelected()
    {
        return new AbstractWebElementPredicate("isSelected")
        {
            public Boolean apply(WebElement from)
            {
                return from.isSelected();
            }
        };
    }

    public static Function<WebElement, String> getTagName()
    {
        return new Function<WebElement, String>()
        {
            public String apply(WebElement from)
            {
                return from.getTagName();
            }
        };
    }

    public static Function<WebElement, String> getText()
    {
        return new Function<WebElement, String>()
        {
            public String apply(WebElement from)
            {
                return from.getText();
            }
        };
    }

    public static Function<WebElement, String> getValue()
    {
        return new Function<WebElement, String>()
        {
            public String apply(WebElement from)
            {
                return from.getAttribute("value");
            }
        };
    }

    public static Function<WebElement, Point> getLocation()
    {
        return new Function<WebElement, Point>()
        {
            public Point apply(WebElement from)
            {
                return from.getLocation();
            }
        };
    }

    public static Function<WebElement, Dimension> getSize()
    {
        return new Function<WebElement, Dimension>()
        {
            public Dimension apply(WebElement from)
            {
                return from.getSize();
            }
        };
    }

    public static Function<WebElement, String> getAttribute(final String attributeName)
    {
        checkNotNull(attributeName);
        return new Function<WebElement, String>()
        {
            public String apply(WebElement from)
            {
                return from.getAttribute(attributeName);
            }
        };
    }

    public static Function<WebElement, Boolean> hasAttribute(final String attributeName, final String expectedValue)
    {
        checkNotNull(attributeName);
        checkNotNull(expectedValue);
        return new AbstractWebElementPredicate("hasAttribute")
        {
            public Boolean apply(WebElement from)
            {
                return expectedValue.equals(from.getAttribute(attributeName));
            }
        };
    }

    @Nonnull
    public static Function<WebElement, Set<String>> getCssClasses()
    {
        return new AbstractWebElementFunction<Set<String>>("getCssClasses")
        {
            public Set<String> apply(WebElement from)
            {
                return Elements.getCssClasses(from.getAttribute(ATTRIBUTE_CLASS));
            }
        };
    }

    public static Function<WebElement, Boolean> hasClass(final String className)
    {
        checkNotNull(className);
        return new AbstractWebElementPredicate("hasClass")
        {
            public Boolean apply(WebElement from)
            {
                return Check.hasClass(className, from);
            }
        };
    }

    public static Function<WebElement, Boolean> hasText(final String text)
    {
        checkNotNull(text);
        return new AbstractWebElementPredicate("hasText")
        {
            public Boolean apply(WebElement from)
            {
                // in case of text contains is the most common case
                return from.getText().contains(text);
            }
        };
    }

    public static Function<WebElement, Boolean> hasValue(final String value)
    {
        checkNotNull(value);
        return new AbstractWebElementPredicate("hasValue")
        {
            public Boolean apply(WebElement from)
            {
                return value.equals(from.getAttribute("value"));
            }
        };
    }
}
