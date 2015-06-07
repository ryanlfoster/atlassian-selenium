package com.atlassian.selenium;

import com.thoughtworks.selenium.Selenium;

/**
 * Conditions used to make assertions in {@link SeleniumAssertions#byTimeout(Condition)} methods.
 */
public interface Condition
{
    /**
     * A method to test a condition.
     * @return true if the test executed passed
     */
    boolean executeTest(Selenium selenium);

    /**
     * @return An error message to display if the test execution failed. 
     */
    String errorMessage();
}
