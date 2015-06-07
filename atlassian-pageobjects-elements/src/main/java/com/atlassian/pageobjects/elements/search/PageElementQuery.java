package com.atlassian.pageobjects.elements.search;

import com.atlassian.annotations.PublicApi;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.google.common.base.Predicate;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;

/**
 * {@link SearchQuery}, whose element type is an instance of {@link PageElement}. Allows for nesting the search
 * ({@link #by(By)}) and mapping the underlying page elements into elements of specialized type ({@link #as(Class)}, or
 * with a customized timeout ({@link #withTimeout(TimeoutType)}.
 *
 * @since 2.3
 */
@PublicApi
public interface PageElementQuery<E extends PageElement> extends SearchQuery<E, PageElementQuery<E>>
{
    /**
     * Search withing all results for child elements matching {@code by}. This is technically a flat map that will
     * expand each current result element into a set of child elements matching {@code by}.
     *
     * @param by locator to search by
     * @return new query with results matching {@code by}
     */
    @Nonnull
    PageElementQuery<E> by(@Nonnull By by);

    /**
     * Search child elements using {@code by} and applying the {@code filter} at the same time. This is equivalent to
     * calling {@link #by(By)} and {@link #filter(Predicate)} in succession.
     *
     * @param by locator to search by
     * @param filter filter for the results of the search
     * @return new query with results matching {@code by} and filtered using {@code filter}
     */
    @Nonnull
    PageElementQuery<E> by(@Nonnull By by, @Nonnull Predicate<? super PageElement> filter);

    /**
     * Apply a custom {@code timeoutType} to all resulting elements.
     *
     * @param timeoutType new timeout type
     * @return a query equivalent to this query, but with a custom timeout
     */
    @Nonnull
    PageElementQuery<E> withTimeout(@Nonnull TimeoutType timeoutType);

    /**
     * Map current results to a more specialized {@code pageElementClass} (e.g. {@code CheckboxElement}).
     *
     * @param pageElementClass the page element class to apply
     * @param <PE> the new element type parameter
     * @return new query equivalent to this query, but with different element type, corresponding to
     * {@code pageElementClass}
     */
    @Nonnull
    <PE extends E> PageElementQuery<PE> as(@Nonnull Class<PE> pageElementClass);
}
