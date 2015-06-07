package com.atlassian.webdriver.debug;

/**
 * Abstraction of an error message we have retrieved from the browser.
 * @see JavaScriptErrorRetriever
 * @since 2.3
 */
public interface JavaScriptErrorInfo
{
    /**
     * Returns the full error line as it should be displayed.
     */
    String getDescription();

    /**
     * Returns only the message portion of the error (for comparison to errorsToIgnore).
     */
    String getMessage();
}
