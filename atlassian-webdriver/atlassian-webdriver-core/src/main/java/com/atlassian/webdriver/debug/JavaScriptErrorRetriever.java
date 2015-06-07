package com.atlassian.webdriver.debug;

/**
 * Abstraction of the underlying error retrieval mechanism.  The standard implementation is
 * {@link DefaultErrorRetriever}; this can be overridden for unit testing of other classes
 * that use the interface, or to perform custom post-processing of the results.
 * @see com.atlassian.webdriver.testing.rule.JavaScriptErrorsRule
 * @since 2.3
 */
public interface JavaScriptErrorRetriever
{
    /**
     * Return true if the current driver supports retrieval of Javascript errors.
     */
    boolean isErrorRetrievalSupported();
    
    /**
     * Attempt to retrieve Javascript errors from the current driver.  If this is not
     * possible, return an empty Iterable.
     */
    Iterable<JavaScriptErrorInfo> getErrors();
}
