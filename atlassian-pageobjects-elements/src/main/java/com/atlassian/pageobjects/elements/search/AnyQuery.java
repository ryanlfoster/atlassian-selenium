package com.atlassian.pageobjects.elements.search;

import com.atlassian.annotations.PublicApi;

/**
 * {@link SearchQuery} that can have any results.
 *
 * @see PageElementSearch
 * @see SearchQuery
 *
 * @since 2.3
 */
@PublicApi
public interface AnyQuery<E> extends SearchQuery<E, AnyQuery<E>>
{
}
