package com.atlassian.webdriver.utils.element;

import com.google.common.collect.Iterables;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Hamcrest matchers for web elements
 *
 * @since v2.2
 */
public final class WebElementMatchers
{

    private WebElementMatchers()
    {
        throw new AssertionError("Don't instantiate me");
    }


    public static <T> Matcher<Iterable<T>> containsAtLeast(final Matcher<T> elementMatcher, final int numberOfMatchingItems)
    {
        return new TypeSafeMatcher<Iterable<T>>()
        {
            @Override
            public boolean matchesSafely(Iterable<T> elements)
            {
                if (Iterables.size(elements) < numberOfMatchingItems)
                {
                    return false;
                }
                int matchCount = 0;
                for (T item : elements)
                {
                    if (elementMatcher.matches(item))
                    {
                        matchCount++;
                    }
                }
                return matchCount >= numberOfMatchingItems;
            }

            public void describeTo(Description description)
            {
                description.appendText("Contains at least ").appendValue(numberOfMatchingItems)
                        .appendText(" items matching ").appendDescriptionOf(elementMatcher);
            }
        };
    }

    /**
     * @param expectedText expected text
     * @return matcher for the {@code WebElement} text
     * @since 2.3
     */
    @Nonnull
    public static Matcher<WebElement> withText(@Nullable String expectedText) {
        return withTextThat(Matchers.is(expectedText));
    }

    /**
     * @param textMatcher text matcher
     * @return matcher for the {@code WebElement} text
     * @since 2.3
     */
    @Nonnull
    public static Matcher<WebElement> withTextThat(@Nonnull Matcher<String> textMatcher) {
        return new FeatureMatcher<WebElement, String>(textMatcher, "text", "text") {

            @Override
            protected String featureValueOf(WebElement actual)
            {
                return actual.getText();
            }
        };
    }

    public static Matcher<WebElement> tagNameEqual(final String expectedTagName)
    {
        checkNotNull(expectedTagName);
        return new TypeSafeMatcher<WebElement>()
        {
            @Override
            public boolean matchesSafely(WebElement item)
            {
                return expectedTagName.equals(item.getTagName());
            }

            public void describeTo(Description description)
            {
                description.appendText("Tag name should be equal to ").appendValue(expectedTagName);
            }
        };
    }
}
