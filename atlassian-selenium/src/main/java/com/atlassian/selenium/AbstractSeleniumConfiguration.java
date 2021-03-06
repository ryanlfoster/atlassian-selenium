package com.atlassian.selenium;

/**
 * An abstract implementation of the SeleniumConfigurationClass that
 * has methods that return default values for some of the configuration
 * variables
 * @since v3.12
 */
public abstract class AbstractSeleniumConfiguration implements SeleniumConfiguration {

    public long getActionWait()
    {
        return 400;
    }

    public long getPageLoadWait()
    {
        return 50000;
    }

    public long getConditionCheckInterval()
    {
        return 100;
    }

    public String getFirefoxProfileTemplate() 
    {
        return null;
    }
    
    public boolean getSingleWindowMode()
    {
    	return false;
    }


    /**
     * By default return null so reports aren't generated.
     * @return
     */
    public String getPerformanceReportLocation()
    {
        return null;
    }

    /**
     * By default return false so only events with explicit keys
     * are recorded (if a report location is specified).
     * @return
     */
    public boolean getShowAutoGeneratedPerformanceEvents()
    {
        return false;
    }
    
}
