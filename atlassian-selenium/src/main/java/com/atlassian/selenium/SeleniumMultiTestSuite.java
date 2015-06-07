package com.atlassian.selenium;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * A skeleton test suite for Selenium tests. The run method starts the selenium server (if the
 * selenium configuration class has been set to allow this.
 *
 * @since v3.12
 */
public abstract class SeleniumMultiTestSuite extends TestSuite
{

    /**
     * To be implemented by users of this class.
     * @return an implementation of the SeleniumConfiguration interface containing the appropriate
     * selenium configuration information.
     */
    protected abstract List<SeleniumConfiguration> getSeleniumConfigurations();


    /**
     * A special run methods that, depending on the SeleniumConfiguration, will start the selenium
     * server and client.
     * @param testResult Test results class to be passed to parent
     */
    public final void run(TestResult testResult){
        SeleniumStarter.getInstance().start(getSeleniumConfigurations());
        SeleniumStarter.getInstance().setManual(false);
        super.run(testResult);
        SeleniumStarter.getInstance().stop();
    }


}