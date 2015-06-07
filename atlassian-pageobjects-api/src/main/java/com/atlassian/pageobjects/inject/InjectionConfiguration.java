package com.atlassian.pageobjects.inject;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.Nonnull;

/**
 * <p/>
 * 'Builder'-style API for configuring {@link com.atlassian.pageobjects.inject.ConfigurableInjectionContext}s.
 *
 * <p/>
 * The modifications to configuration done via calling the 'add'-style methods are committed by calling
 *
 * @since 1.2
 */
@ExperimentalApi
public interface InjectionConfiguration
{

    /**
     * Add mapping from interface to a concrete implementation.
     *
     * @param interfaceType specifies the interface
     * @param implementationType specifies the implementation of <tt>interfaceType</tt>
     * @param <I> type parameter of the interface
     * @return this configuration module
     */
    @Nonnull
    public <I> InjectionConfiguration addImplementation(@Nonnull Class<I> interfaceType, @Nonnull Class<? extends I> implementationType);


    /**
     * Add mapping from a type to a singleton instance of this type that shall be used for injection in given context.
     *
     * @param type component type
     * @param instance implementing singleton instance
     * @param <C> type component of the component
     * @param <I> type parameter of the implementing instance
     * @return this configuration module
     */
    @Nonnull
    public <C,I extends C> InjectionConfiguration addSingleton(@Nonnull Class<C> type, @Nonnull I instance);

    // we might want to extend it in the future to cover scopes et al.


    /**
     * <p/>
     * Finish the configuration and retrieve the resulting injection context instance.
     *
     * <p/>
     * The resulting instance might be the same instance that this configuration object was originally retrieved from,
     * or a new instance with updated configuration - depending on the mutability of the injection context
     * implementation.
     *
     * @return the resulting injection context
     */
    @Nonnull
    public ConfigurableInjectionContext finish();
}
