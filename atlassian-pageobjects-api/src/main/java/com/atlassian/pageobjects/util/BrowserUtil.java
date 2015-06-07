package com.atlassian.pageobjects.util;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.BrowserAware;
import com.google.common.base.Supplier;

/**
 * Utilities for manipulating the current browser.
 *
 * @deprecated it is now possible to inject {@link BrowserAware}, or even {@link Browser} directly into your page
 * objects/tests. Scheduled for removal in 3.0
 */
public class BrowserUtil
{
    private static Browser currentBrowser;

    public static void setCurrentBrowser(Browser browser)
    {
        currentBrowser = browser;
    }

    public static Browser getCurrentBrowser()
    {
        return currentBrowser;
    }

    public static Supplier<Browser> currentBrowserSupplier()
    {
        return new Supplier<Browser>()
        {
            @Override
            public Browser get()
            {
                return getCurrentBrowser();
            }
        };
    }
}
