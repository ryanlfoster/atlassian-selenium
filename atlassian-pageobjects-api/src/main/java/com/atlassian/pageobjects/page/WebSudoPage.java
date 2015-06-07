package com.atlassian.pageobjects.page;

import com.atlassian.pageobjects.Page;

/**
 * The page that confirms the administrator password to start a secure administrator session
 */
public interface WebSudoPage extends Page
{
    /**
     * Confirm the sys admin password and navigate to given page
     * @param targetPage Page to navigate to after password confirm
     * @param <T> Page
     * @return Instance of target page
     */
    <T extends Page> T confirm(Class<T> targetPage);

    /**
     * Confirm with given password and navigate to given page
     * @param password The password to confirm
     * @param targetPage Page to navigate to after password confirm
     * @param <T> Page
     * @return Instance of target page
     */
    <T extends Page> T confirm(String password, Class<T> targetPage);
}
