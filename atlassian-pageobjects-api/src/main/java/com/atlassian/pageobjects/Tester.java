package com.atlassian.pageobjects;

/**
 * Abstracts the technology that can drive the browser.
 */
public interface Tester
{
    /**
     * Goes to a URL by changing the browser's location
     * @param url The url to change to
     */
    void gotoUrl(String url);
}
