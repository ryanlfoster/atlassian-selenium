package com.atlassian.pageobjects.internal.elements.search;

import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.WebDriverElement;
import com.atlassian.pageobjects.elements.search.DefaultQuery;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultWebDriverQuery extends WebDriverElementQuery<PageElement> implements DefaultQuery
{
    public DefaultWebDriverQuery(@Nonnull WebDriverElement root)
    {
        super(defaultSupplier(checkNotNull(root, "root")), PageElement.class, root.getDefaultTimeout());
    }

    private static Supplier<Iterable<PageElement>> defaultSupplier(PageElement pageElement)
    {
        return Suppliers.<Iterable<PageElement>>ofInstance(ImmutableList.of(pageElement));
    }
}
