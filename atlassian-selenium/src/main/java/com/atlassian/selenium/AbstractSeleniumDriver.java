package com.atlassian.selenium;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for utilities using {@link SeleniumClient} for higher-level operations.
 *
 * @since v1.21
 */
public abstract class AbstractSeleniumDriver
{
    protected final SeleniumClient client;


    protected AbstractSeleniumDriver(final SeleniumClient client)
    {
        this.client = checkNotNull(client, "client");
    }
}
