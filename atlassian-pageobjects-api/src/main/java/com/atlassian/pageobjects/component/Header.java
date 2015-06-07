package com.atlassian.pageobjects.component;

import com.atlassian.pageobjects.Page;

/**
 * Header component to be implemented via an override by the products
 * @since 2.0
 */
public interface Header
{
    /**
     * @return true if a user is logged in, false otherwise.
     */
    boolean isLoggedIn();

    /**
     * Logs out the user if currently logged in and sends the browser to the next page.
     *
     * @param nextPage The next page to visit, which may involve changing the URL.  Cannot be null.
     * @param <M>      The page type
     * @return The next page, fully loaded and initialized.
     */
    <M extends Page> M logout(Class<M> nextPage);

    /**
     * @return The WebSudoBanner page object
     */
    WebSudoBanner getWebSudoBanner();
}
