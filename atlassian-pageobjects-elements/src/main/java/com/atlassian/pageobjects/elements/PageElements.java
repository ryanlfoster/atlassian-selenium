package com.atlassian.pageobjects.elements;

import com.atlassian.annotations.PublicApi;
import com.atlassian.webdriver.Elements;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Predicates and functions for easier usage of {@link PageElement page elements}.
 *
 * @since 2.3
 */
@PublicApi
public final class PageElements
{
    public static final String BODY = Elements.TAG_BODY;
    public static final String TR = Elements.TAG_TR;
    public static final String TD = Elements.TAG_TD;

    public static final String DATA_PREFIX = "data-";

    private PageElements()
    {
        throw new AssertionError("Do not instantiate " + getClass().getSimpleName());
    }

    @Nonnull
    public static Function<PageElement, String> getText()
    {
        return new Function<PageElement, String>()
        {
            @Override
            public String apply(PageElement input)
            {
                return StringUtils.stripToNull(input.getText());
            }
        };
    }

    @Nonnull
    public static Predicate<PageElement> isVisible()
    {
        return new Predicate<PageElement>()
        {
            @Override
            public boolean apply(PageElement input)
            {
                return input.isVisible();
            }
        };
    }

    @Nonnull
    public static Predicate<PageElement> hasClass(@Nonnull final String className)
    {
        checkNotNull(className, "className");

        return new Predicate<PageElement>()
        {
            @Override
            public boolean apply(PageElement input)
            {
                return input.hasClass(className);
            }
        };
    }

    @Nonnull
    public static Predicate<PageElement> hasAttribute(@Nonnull final String attribute)
    {
        checkNotNull(attribute, "attribute");

        return new Predicate<PageElement>()
        {
            @Override
            public boolean apply(PageElement input)
            {
                return input.getAttribute(attribute) != null;
            }
        };
    }

    @Nonnull
    public static Predicate<PageElement> hasAttribute(@Nonnull final String attribute, @Nonnull final String value)
    {
        checkNotNull(attribute, "attribute");
        checkNotNull(value, "value");

        return new Predicate<PageElement>()
        {
            @Override
            public boolean apply(PageElement input)
            {
                return input.hasAttribute(attribute, value);
            }
        };
    }

    @Nonnull
    public static Predicate<PageElement> hasDataAttribute(@Nonnull final String attribute)
    {
        checkNotNull(attribute, "attribute");

        return hasAttribute(DATA_PREFIX + attribute);
    }

    @Nonnull
    public static Predicate<PageElement> hasDataAttribute(@Nonnull final String attribute, @Nonnull final String value)
    {
        checkNotNull(attribute, "attribute");

        return hasAttribute(DATA_PREFIX + attribute, value);
    }

    @Nonnull
    public static Predicate<PageElement> hasValue(@Nonnull final String value)
    {
        checkNotNull(value, "value");

        return new Predicate<PageElement>()
        {
            @Override
            public boolean apply(PageElement input)
            {
                return value.equals(input.getValue());
            }
        };
    }

    @Nonnull
    public static Function<PageElement, Iterable<String>> getCssClasses()
    {
        return new Function<PageElement, Iterable<String>>()
        {
            @Override
            public Set<String> apply(PageElement input)
            {
                return input.getCssClasses();
            }
        };
    }

    @Nonnull
    public static Function<PageElement, String> getAttribute(@Nonnull final String attributeName)
    {
        checkNotNull(attributeName, "attributeName");

        return new Function<PageElement, String>()
        {
            @Override
            public String apply(PageElement input)
            {
                return input.getAttribute(attributeName);
            }
        };
    }

    @Nonnull
    public static Function<PageElement, String> getDataAttribute(@Nonnull final String attributeName)
    {
        checkNotNull(attributeName, "attributeName");

        return new Function<PageElement, String>()
        {
            @Override
            public String apply(PageElement input)
            {
                return input.getAttribute(DATA_PREFIX + attributeName);
            }
        };
    }
}
