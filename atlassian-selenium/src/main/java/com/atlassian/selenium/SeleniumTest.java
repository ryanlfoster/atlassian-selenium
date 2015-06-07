package com.atlassian.selenium;

import com.atlassian.performance.junit.PerformanceTest;
import com.atlassian.performance.TimeRecorder;
import junit.framework.TestCase;

/**
 * A base class for selenium tests
 *
 * @since v3.12
 */
public abstract class SeleniumTest extends TestCase implements PerformanceTest
{
    protected SeleniumAssertions assertThat;
    protected SeleniumClient client;
    protected SeleniumConfiguration config;
    protected TimeRecorder recorder;

    public abstract SeleniumConfiguration getSeleniumConfiguration();

    /**
     * Calls overridden onSetup method before starting
     * the selenium client and possibly server and initiating
     * assertThat and interaction variables
     */
    public final void setUp() throws Exception
    {
        super.setUp();
        config = getSeleniumConfiguration();

        if (SeleniumStarter.getInstance().isManual())
        {
            SeleniumStarter.getInstance().start(config);
        }

        client = getSeleniumClient();
        recorder = new TimeRecorder(this.getClass().getName());

        assertThat = new SeleniumAssertions(client, config, recorder);
        onSetUp();
    }

    /**
     * Gets the SeleniumClient. Override this method if you would like to return your
     * own implementation of {@link SeleniumClient}.
     * @return New or current selenium client
     */
    protected SeleniumClient getSeleniumClient()
    {
        return SeleniumStarter.getInstance().getSeleniumClient(getSeleniumConfiguration());
    }

    /**
     * To be overridden in the case of test-specific setup activities
     */
    protected void onSetUp() throws Exception
    {
    }

    /**
     * Calls overridden onTearDown method before shutting down
     * the selenium client and possibly server
     */
    public final void tearDown() throws Exception
    {
        super.tearDown();
        onTearDown();
        
        if (SeleniumStarter.getInstance().isManual())
        {
            SeleniumStarter.getInstance().stop();
        }
    }

    /**
     * To be overridden in the case of test-specific tear-down activities
     */
    protected  void onTearDown() throws Exception
    {
    }

    public TimeRecorder getRecorder()
    {
        return recorder;
    }
}
