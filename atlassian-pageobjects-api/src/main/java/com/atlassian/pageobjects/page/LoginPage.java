package com.atlassian.pageobjects.page;

import com.atlassian.pageobjects.Page;

/**
 * A login page that logs in a user and sends the browser to the next page
 */
public interface LoginPage extends Page
{
    /**
     * Logs in a user and sends the browser to the next page
     *
     * @param username     The username to login
     * @param password      The password to login with
     * @param nextPage The next page to visit, which may involve changing the URL.  Cannot be null.
     * @param <M>      The page type
     * @return The next page, fully loaded and initialized.
     */
    <M extends Page> M login(String username, String password, Class<M> nextPage);

    /**
     * Logs in the default sysadmin user and sends the browser to the next page
     *
     * @param nextPage The next page to visit, which may involve changing the URL.  Cannot be null.
     * @param <M>      The page type
     * @return The next page, fully loaded and initialized.
     */
    <M extends Page> M loginAsSysAdmin(Class<M> nextPage);
}
