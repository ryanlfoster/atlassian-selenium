package com.atlassian.pageobjects.inject;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.Nonnull;

/**
 * Simple interface for framework components capable of injection of components as described by JSR-330.
 *
 * @since 2.1
 */
@ExperimentalApi
public interface InjectionContext
{

    /**
     * Get an instance of given <tt>type</tt> from context.
     *
     * @param type type of the requested instance
     * @param <T> type param
     * @return an instance of requested type. An exception may be raised if the context is unable to instantiate
     * given <tt>type</tt>.
     * @throws IllegalArgumentException if instantiating given class according to JSR-330 rules was impossible
     */
    @Nonnull
    <T> T getInstance(@Nonnull Class<T> type);

    /**
     * Execute injection of static fields on given <tt>targetClass</tt>.
     *
     * @param targetClass class to inject into
     */
    public void injectStatic(@Nonnull Class<?> targetClass);


    /**
     * Execute injection of fields on given <tt>targetInstance</tt>
     *
     * @param targetInstance instance to inject into
     * @deprecated in 2.3 for removal in 3.0. Use {@link #inject(Object)} instead
     */
    @Deprecated
    public void injectMembers(@Nonnull Object targetInstance);

    /**
     * Execute injection of fields on {@code targetInstance}
     *
     * @param target instance to inject into
     */
    @Nonnull
    public <T> T inject(@Nonnull T target);
}
