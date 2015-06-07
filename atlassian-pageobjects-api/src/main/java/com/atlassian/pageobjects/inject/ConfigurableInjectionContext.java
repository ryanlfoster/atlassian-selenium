package com.atlassian.pageobjects.inject;

import com.atlassian.annotations.ExperimentalApi;

import javax.annotation.Nonnull;

/**
 * Injection context that can be configured.
 *
 * @since 1.2
 */
@ExperimentalApi
public interface ConfigurableInjectionContext extends InjectionContext
{

    /**
     * Get an injection configuration object that may be used to add/override objects in this injection context.
     *
     * @return injection configuration associated with this context
     * @see InjectionConfiguration
     */
    @Nonnull
    InjectionConfiguration configure();
}
