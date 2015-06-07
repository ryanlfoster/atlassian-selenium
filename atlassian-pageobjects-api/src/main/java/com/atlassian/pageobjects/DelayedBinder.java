package com.atlassian.pageobjects;

import com.atlassian.annotations.PublicApi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A delayed binder that gives the caller full control over the creation and lifecycle of the page object.
 */
@NotThreadSafe
@PublicApi
public interface DelayedBinder<T>
{
    /**
     * Instantiates, injects, and initialises the page object, but doesn't execute its lifecycle methods.
     * 
     * @return the binder for chaining
     */
    @Nonnull
    DelayedBinder<T> inject();

    /**
     * Builds the page object and executes its {@link @com.atlassian.pageobjects.binder.WaitUntil wait until} lifecycle
     * methods.
     *
     * @return the binder for chaining
     */
    @Nonnull
    DelayedBinder<T> waitUntil();

    /**
     * Builds, waits for, and validates the state of the page object
     * @return the binder for chaining
     */
    @Nonnull
    DelayedBinder<T> validateState();

    /**
     * Goes through the full binding, including lifecycle methods, to determine whether the page object can be bound.
     *
     * @return {@code true} if the binding was successful, in which case {@link #bind()} will return the page object
     * instance without failing
     */
    boolean canBind();

    /**
     * @return The current page object, building IT if necessary. If called before any other methods are called, it will
     * return the instantiated object but with no injections or lifecycle methods called.
     */
    @Nonnull
    T get();

    /**
     * Builds, waits for, validates the state of, and returns the page object.
     *
     * @return The fully bound page object
     */
    @Nonnull
    T bind();
}
