package com.atlassian.pageobjects.internal.elements.search;

import com.atlassian.pageobjects.elements.search.AnyQuery;
import com.google.common.base.Supplier;

import javax.annotation.Nonnull;

public final class DefaultAnyQuery<E> extends AbstractSearchQuery<E, AnyQuery<E>> implements AnyQuery<E>
{
    public DefaultAnyQuery(@Nonnull Supplier<Iterable<E>> querySupplier)
    {
        super(querySupplier);
    }

    @Nonnull
    @Override
    protected DefaultAnyQuery<E> newInstance(@Nonnull Supplier<Iterable<E>> supplier)
    {
        return new DefaultAnyQuery<E>(supplier);
    }

    @Nonnull
    @Override
    protected <F> AnyQuery<F> newAnyQueryInstance(@Nonnull Supplier<Iterable<F>> supplier)
    {
        return new DefaultAnyQuery<F>(supplier);
    }
}
