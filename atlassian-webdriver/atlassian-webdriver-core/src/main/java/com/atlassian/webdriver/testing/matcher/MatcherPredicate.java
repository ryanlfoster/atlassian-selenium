package com.atlassian.webdriver.testing.matcher;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

/**
 * Adapter from Hamcrest {@link Matcher} to a Guava {@link Predicate}.
 *
 * @since 2.3
 */
public final class MatcherPredicate<T> implements Predicate<T>
{
    @Nonnull
    public static <T> Predicate<T> forMatcher(@Nonnull Matcher<T> matcher)
    {
        return new MatcherPredicate<T>(matcher);
    }

    @Nonnull
    public static <T> Predicate<T> withMatcher(@Nonnull Matcher<T> matcher)
    {
        return new MatcherPredicate<T>(matcher);
    }

    private final Matcher<T> matcher;

    private MatcherPredicate(Matcher<T> matcher)
    {
        this.matcher = matcher;
    }

    @Override
    public boolean apply(T input)
    {
        return matcher.matches(input);
    }
}
