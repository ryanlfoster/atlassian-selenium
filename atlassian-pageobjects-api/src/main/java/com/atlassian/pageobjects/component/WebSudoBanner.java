package com.atlassian.pageobjects.component;

import com.atlassian.pageobjects.Page;

/**
 * WebSudoBanner component interface so that products can add WebSudoBanner functionality to PageObjects
 * @since 2.1.0
 */
public interface WebSudoBanner
{
    /**
     * Checks the websudo banner is showing
     * @return boolean indicating whether the websudo banner is showing
     */
    boolean isShowing();

    /**
     * Retrieves the websudo message from the banner.
     * @return The message in the websudo banner. Returns null if there is no message.
     */
    String getMessage();

    /**
     * Drops the websudo privilege if the websudo banner is displayed otherwise just navigates
     * to the next page.
     * @param nextPage the page to navigate to after websudo privileges have been dropped.
     * Cannot be null.
     * @param <P> The page class type.
     * @return The nextPage pageObject
     */
    <P extends Page> P dropWebSudo(Class<P> nextPage);
}
