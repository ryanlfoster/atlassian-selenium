package com.atlassian.pageobjects.internal.elements.search;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.PageObjects;
import com.atlassian.pageobjects.elements.query.Conditions;
import com.atlassian.pageobjects.elements.query.Queries;
import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.pageobjects.elements.search.AnyQuery;
import com.atlassian.pageobjects.elements.search.SearchQuery;
import com.atlassian.pageobjects.elements.timeout.Timeouts;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getFirst;

public abstract class AbstractSearchQuery<E, Q extends SearchQuery<E, Q>> implements SearchQuery<E, Q>
{
    @Inject
    protected Timeouts timeouts;

    @Inject
    protected PageBinder pageBinder;

    protected final Supplier<Iterable<E>> querySupplier;

    protected AbstractSearchQuery(@Nonnull Supplier<Iterable<E>> querySupplier)
    {
        this.querySupplier = checkNotNull(querySupplier, "querySupplier");
    }

    @Nonnull
    @Override
    public final Q filter(@Nonnull final Predicate<? super E> predicate)
    {
        return newInstance(new Supplier<Iterable<E>>()
        {
            @Override
            public Iterable<E> get()
            {
                return Iterables.filter(querySupplier.get(), predicate);
            }
        });
    }

    @Nonnull
    @Override
    public final <F> AnyQuery<F> map(@Nonnull final Function<? super E, F> mapper)
    {
        return newAnyQueryInstance(mapSupplier(mapper));
    }

    @Nonnull
    @Override
    public final <F> AnyQuery<F> flatMap(@Nonnull final Function<? super E, Iterable<F>> mapper)
    {
        return newAnyQueryInstance(flatMapSupplier(mapper));
    }

    @Nonnull
    @Override
    public final <F> AnyQuery<F> bindTo(@Nonnull Class<F> pageObjectClass, @Nonnull Object... extraArgs)
    {
        return map(PageObjects.bindTo(pageBinder, pageObjectClass, extraArgs));
    }

    @Override
    public final Iterable<E> get()
    {
        return getResultNow();
    }

    @Nullable
    @Override
    public final E first()
    {
        return getFirst(getResultNow(), null);
    }

    @Nonnull
    @Override
    public final Iterable<E> now()
    {
        return getResultNow();
    }

    @Nonnull
    @Override
    public final TimedCondition hasResult()
    {
        return Conditions.forSupplier(timeouts, new Supplier<Boolean>()
        {
            @Override
            public Boolean get()
            {
                return hasResultNow();
            }
        });
    }

    @Nonnull
    @Override
    public final TimedQuery<Iterable<E>> timed()
    {
        return Queries.forSupplier(timeouts, new Supplier<Iterable<E>>()
        {
            @Override
            public Iterable<E> get()
            {
                return getResultNow();
            }
        });
    }

    @Nonnull
    protected abstract Q newInstance(@Nonnull Supplier<Iterable<E>> supplier);

    @Nonnull
    protected abstract <F> AnyQuery<F> newAnyQueryInstance(@Nonnull Supplier<Iterable<F>> supplier);

    protected final <F> Supplier<Iterable<F>> flatMapSupplier(@Nonnull final Function<? super E, Iterable<F>> mapper)
    {
        return new Supplier<Iterable<F>>()
        {
            @Override
            public Iterable<F> get()
            {
                return FluentIterable.from(querySupplier.get()).transformAndConcat(mapper);
            }
        };
    }

    protected final <F> Supplier<Iterable<F>> mapSupplier(@Nonnull final Function<? super E, F> mapper)
    {
        return new Supplier<Iterable<F>>()
        {
            @Override
            public Iterable<F> get()
            {
                return Iterables.transform(querySupplier.get(), mapper);
            }
        };
    }

    private Iterable<E> getResultNow()
    {
        return querySupplier.get();
    }

    private boolean hasResultNow()
    {
        return !Iterables.isEmpty(getResultNow());
    }
}
