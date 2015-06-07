package com.atlassian.pageobjects.elements.testing;

import com.atlassian.annotations.PublicApi;
import com.atlassian.pageobjects.elements.CheckboxElement;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.webdriver.Elements;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static com.atlassian.pageobjects.elements.PageElements.DATA_PREFIX;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Common matchers for {@link PageElement}s.
 *
 * @since 2.3
 */
@PublicApi
public final class PageElementMatchers
{
    private PageElementMatchers()
    {
        throw new AssertionError("Do not instantiate " + PageElementMatchers.class.getName());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <PE extends PageElement, PEE extends PE> Matcher<PEE> asMatcherOf(@Nonnull Class<PEE> targetType,
                                                                                    @Nonnull Matcher<PE> original)
    {
        checkNotNull(targetType, "targetType");
        checkNotNull(original, "original");

        return (Matcher) original;
    }

    @Nonnull
    public static Matcher<CheckboxElement> isChecked()
    {
        return new FeatureMatcher<CheckboxElement, Boolean>(is(true), "is checked", "is checked")
        {
            @Override
            protected Boolean featureValueOf(CheckboxElement checkbox)
            {
                return checkbox.isChecked();
            }
        };
    }

    @Nonnull
    public static Matcher<PageElement> withAttribute(@Nonnull final String name, @Nullable String expectedValue)
    {
        return withAttributeThat(name, is(expectedValue));
    }

    @Nonnull
    public static Matcher<PageElement> withAttributeThat(@Nonnull final String name,
                                                         @Nonnull Matcher<String> valueMatcher)
    {
        checkNotNull(name, "name");
        checkNotNull(valueMatcher, "valueMatcher");
        String featureDescription = format("attribute '%s'", name);

        return new FeatureMatcher<PageElement, String>(valueMatcher, featureDescription, featureDescription)
        {
            @Override
            protected String featureValueOf(PageElement actual)
            {
                return actual.getAttribute(name);
            }
        };
    }

    @Nonnull
    public static Matcher<PageElement> withClass(@Nonnull String expectedClass)
    {
        return withClassThat(is(expectedClass));
    }

    @Nonnull
    public static Matcher<PageElement> withClassThat(@Nonnull Matcher<String> classMatcher)
    {
        checkNotNull(classMatcher, "classMatcher");

        return new FeatureMatcher<PageElement, Set<String>>(hasItem(classMatcher), "CSS class", "CSS class")
        {
            @Override
            protected Set<String> featureValueOf(PageElement actual)
            {
                return actual.getCssClasses();
            }
        };
    }

    @Nonnull
    public static Matcher<PageElement> withDataAttribute(@Nonnull final String name, @Nullable String expectedValue)
    {
        return withDataAttributeThat(name, is(expectedValue));
    }

    @Nonnull
    public static Matcher<PageElement> withDataAttributeThat(@Nonnull final String name,
                                                             @Nonnull Matcher<String> valueMatcher)
    {
        checkNotNull(name, "name");

        return withAttributeThat(DATA_PREFIX + name, valueMatcher);
    }

    @Nonnull
    public static Matcher<PageElement> withId(@Nullable String expectedId)
    {
        return withIdThat(is(expectedId));
    }

    @Nonnull
    public static Matcher<PageElement> withIdThat(@Nonnull Matcher<String> idMatcher)
    {
        return withAttributeThat(Elements.ATTRIBUTE_ID, idMatcher);
    }

    @Nonnull
    public static Matcher<PageElement> withText(@Nullable String expectedText)
    {
        return withTextThat(is(expectedText));
    }

    @Nonnull
    public static Matcher<PageElement> withTextThat(@Nonnull Matcher<String> textMatcher)
    {
        checkNotNull(textMatcher, "textMatcher");

        return new FeatureMatcher<PageElement, String>(textMatcher, "text", "text")
        {
            @Override
            protected String featureValueOf(PageElement actual)
            {
                return actual.getText();
            }
        };
    }
}
