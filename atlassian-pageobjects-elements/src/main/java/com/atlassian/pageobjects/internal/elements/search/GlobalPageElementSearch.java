package com.atlassian.pageobjects.internal.elements.search;

import com.atlassian.annotations.Internal;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.WebDriverElement;
import com.atlassian.pageobjects.elements.WebDriverLocators;
import com.atlassian.pageobjects.elements.search.DefaultQuery;
import com.atlassian.pageobjects.elements.search.PageElementSearch;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

/**
 * @since 2.3
 */
@Internal
@NotThreadSafe
public class GlobalPageElementSearch implements PageElementSearch
{
    @Inject
    private PageBinder pageBinder;

    @Inject
    private PageElementFinder pageElementFinder;

    @Nonnull
    @Override
    public DefaultQuery search()
    {
        WebDriverElement root = pageBinder.bind(WebDriverElement.class, WebDriverLocators.root(), TimeoutType.DEFAULT);
        return pageBinder.bind(DefaultWebDriverQuery.class, root);
    }
}
