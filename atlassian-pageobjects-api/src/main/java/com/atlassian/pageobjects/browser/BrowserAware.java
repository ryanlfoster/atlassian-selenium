package com.atlassian.pageobjects.browser;

/**
 * A browser-aware component.
 *
 * @since 2.1
 */
public interface BrowserAware
{

    /**
     * Get browser associated with this component.
     *
     * @return browser
     */
    Browser getBrowser();
}
