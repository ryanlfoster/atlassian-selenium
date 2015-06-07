package com.atlassian.pageobjects;

import com.atlassian.annotations.PublicApi;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.ObjectArrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Predicates, functions and other utilities for use with {@link PageBinder} and other page-object-related APIs.
 *
 * @since 2.3
 */
@PublicApi
public final class PageObjects
{
    private PageObjects()
    {
        throw new AssertionError("Do not instantiate " + getClass().getSimpleName());
    }

    /**
     * Version of {@link #bindTo(PageBinder, Class, Class, Object...)} for where there's no explicit input type.
     *
     * @param binder page binder
     * @param pageObjectClass target page object class
     * @param extraArguments extra arguments to use when binding each page object
     * @param <E> input type
     * @param <PO> wrapping object type
     *
     * @return page binding function
     *
     * @see #bindTo(PageBinder, Class, Object...)
     */
    @Nonnull
    public static <E, PO> Function<E, PO> bindTo(@Nonnull final PageBinder binder,
                                                 @Nonnull final Class<PO> pageObjectClass,
                                                 @Nonnull final Object... extraArguments)
    {
        return bindTo(binder, null, pageObjectClass, extraArguments);
    }

    /**
     * Binds 'wrapper' page objects that take one constructor parameter (and optionally any number of "fixed"
     * {@code extraArguments}. Most useful for simple page objects wrapping page elements, e.g. table rows.
     *
     * <p/>
     * Note: the wrapping type needs to have a constructor that accepts a single instance of the input type and all
     * the extra parameters as provided by {@code extraArguments}.
     *
     * @param binder page binder
     * @param inputType source class to use this method more conveniently
     * @param pageObjectClass target page object class
     * @param extraArguments extra arguments to use when binding each page object
     * @param <E> input type
     * @param <PO> wrapping object type
     *
     * @return page binding function
     *
     * @see PageBinder#bind(Class, Object...)
     */
    @Nonnull
    public static <E, PO> Function<E, PO> bindTo(@Nonnull final PageBinder binder,
                                                 @Nullable final Class<E> inputType,
                                                 @Nonnull final Class<PO> pageObjectClass,
                                                 @Nonnull final Object... extraArguments)
    {
        checkNotNull(binder, "binder");
        checkNotNull(pageObjectClass, "pageObjectClass");
        checkNotNull(extraArguments, "extraArguments");

        return new Function<E, PO>()
        {
            @Override
            public PO apply(E element)
            {
                return binder.bind(pageObjectClass, ObjectArrays.concat(element, extraArguments));
            }
        };
    }

    /**
     * Transforms a list of objects into a list of page objects wrapping those objects. Most commonly the input list
     * will contain page elements of some sort.
     *
     * @param binder page binder
     * @param pageElements a list of page elements to transform
     * @param pageObjectClass target page object class
     * @param extraArguments extra arguments to use when binding each page object
     * @param <E> input object type
     * @param <EE> wrapping object type
     *
     * @return a list of page objects wrapping the inputs
     *
     * @see #bindTo(PageBinder, Class, Object...)
     */
    @Nonnull
    public static <E, EE> Iterable<EE> bind(@Nonnull PageBinder binder,
                                            @Nonnull Iterable<E> pageElements,
                                            @Nonnull Class<EE> pageObjectClass,
                                            @Nonnull final Object... extraArguments)
    {
        return Iterables.transform(pageElements, bindTo(binder, pageObjectClass, extraArguments));
    }
}
