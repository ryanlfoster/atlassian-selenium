package com.atlassian.pageobjects.elements.search;


import com.atlassian.annotations.PublicApi;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.PageElements;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a generic query for any type of object. The query provides API to:
 * <ul>
 *     <li>map and filter the results ({@link #filter(Predicate)}, {@link #map(Function)}, {@link #flatMap(Function)},
 *     {@link #bindTo(Class, Object...)})</li>
 *     <li>obtain query results (e.g. {@link #get()}, {@link #now()}, {@link #first()}, {@link #hasResult()},
 *     {@link #timed()})</li>
 * </ul>
 *
 * @see PageElementSearch
 * @see AnyQuery
 * @see PageElementQuery
 * @see PageElements
 *
 * @since 2.3
 */
@PublicApi
public interface SearchQuery<E, Q extends SearchQuery<E, Q>> extends Supplier<Iterable<E>>
{
    /**
     * Filter the underlying results using {@code predicate}.
     *
     * @param predicate predicate to filter the results
     * @return new query with the applied filter
     */
    @Nonnull
    Q filter(@Nonnull Predicate<? super E> predicate);

    /**
     * Map the query results using {@code mapper}.
     *
     * @param mapper a function to map the results
     * @param <F> the new result type
     * @return new query with mapped results
     */
    @Nonnull
    <F> AnyQuery<F> map(@Nonnull Function<? super E, F> mapper);

    /**
     * Flat map the results using {@code mapper}.
     *
     * @param mapper the function to use to flat map
     * @param <F> the new result type
     * @return new query with mapped results.
     */
    @Nonnull
    <F> AnyQuery<F> flatMap(@Nonnull Function<? super E, Iterable<F>> mapper);

    /**
     * Map the results by binding into a page object. The resulting page objects needs to provide a compatible
     * constructor.
     *
     * @param pageObjectClass page object class
     * @param extraArgs extra arguments to the bind method
     * @param <F> the new result type
     * @return new query with mapped results
     * @see com.atlassian.pageobjects.PageObjects#bind(PageBinder, Iterable, Class, Object...)
     */
    @Nonnull
    <F> AnyQuery<F> bindTo(@Nonnull Class<F> pageObjectClass, @Nonnull Object... extraArgs);


    /**
     * @return first result , or {@code null} if there is no results
     */
    @Nullable
    E first();

    /**
     * Equivalent to {@link #get()}.
     *
     * @return all results, as they are now
     */
    @Nonnull
    Iterable<E> now();

    /**
     * @return timed condition to check whether this query has any result
     */
    @Nonnull
    TimedCondition hasResult();

    /**
     * @return timed query for all results
     */
    @Nonnull
    TimedQuery<Iterable<E>> timed();
}
