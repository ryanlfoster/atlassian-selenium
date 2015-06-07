package com.atlassian.pageobjects;

/**
 * The implementation for a PageObject
 */
public interface Page
{
    /**
     * @return The URI, including query string, relative to the base url
     */
    String getUrl();
}