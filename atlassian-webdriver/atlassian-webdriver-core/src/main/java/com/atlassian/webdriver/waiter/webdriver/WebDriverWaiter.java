package com.atlassian.webdriver.waiter.webdriver;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.waiter.Waiter;
import com.atlassian.webdriver.waiter.WaiterQuery;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

/**
 *
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public class WebDriverWaiter implements Waiter
{
    // Default wait time is 30 seconds
    private static final long DEFAULT_INTERVAL_TIME = 30;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private AtlassianWebDriver driver;

    @Inject
    public WebDriverWaiter(AtlassianWebDriver driver)
    {
        this.driver = driver;
    }

    /**
     * Creates a {@link com.atlassian.webdriver.waiter.WaiterQuery} that uses the default interval {@link #DEFAULT_INTERVAL_TIME}
     * @return
     */
    public WaiterQuery until()
    {
        return until(DEFAULT_INTERVAL_TIME, DEFAULT_TIME_UNIT);
    }

    /**
     * Creates a {@link com.atlassian.webdriver.waiter.WaiterQuery} which allows the timeout interval
     * for the waiter to be set.
     * @param timeout in seconds for the waiter to run for before failing.
     * @return a {@link com.atlassian.webdriver.waiter.WaiterQuery} with the timeout interval set to the provided interval
     */
    public WaiterQuery until(long timeout)
    {
        return until(timeout, TimeUnit.SECONDS);
    }

    /**
     * Creates a {@link com.atlassian.webdriver.waiter.WaiterQuery} which setgs the timeout interval
     * to a more specific value in seconds or milliseconds.
     * @param time the maximum time to wait
     * @param unit the time unit of the <code>time</code> argument
     * @return a {@link com.atlassian.webdriver.waiter.WaiterQuery} with the timeout interval set to the provided interval
     */
    public WaiterQuery until(long time, TimeUnit unit)
    {
        long millisTimeout;

        switch(unit)
        {
            case SECONDS:
                millisTimeout = time * 1000;
                break;
            case MILLISECONDS:
                millisTimeout = time;
                break;
            case MINUTES:
                millisTimeout = 60 * 1000 * time;
                break;
            default:
                throw new UnsupportedOperationException("The provided TimeUnit: " + unit + " is not supported");
        }

        return new WebDriverWaiterQuery(new WebDriverQueryBuilder(driver, millisTimeout));
    }
}
