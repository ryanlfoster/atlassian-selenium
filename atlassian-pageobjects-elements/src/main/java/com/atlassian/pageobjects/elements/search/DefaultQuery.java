package com.atlassian.pageobjects.elements.search;

import com.atlassian.annotations.PublicApi;
import com.atlassian.pageobjects.elements.PageElement;

/**
 * Default query returned by {@link PageElementSearch}, which is an alias for {@link PageElementQuery} with element
 * type {@link PageElement}.
 *
 * @since 2.3
 */
@PublicApi
public interface DefaultQuery extends PageElementQuery<PageElement>
{
}
