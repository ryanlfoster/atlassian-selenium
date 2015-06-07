package com.atlassian.selenium;

import com.atlassian.performance.TimeRecorder;
import junit.framework.TestCase;

import java.util.List;

public abstract class SeleniumMultiTest extends TestCase
{

    protected SeleniumAssertions assertThat;
    protected SeleniumClient client;
    protected boolean parallel;

    public abstract List<SeleniumConfiguration> getSeleniumConfigurations();

    /**
     * Calls overridden onSetup method before starting
     * the selenium client and possibly server and initiating
     * assertThat and interaction variables
     */
    public final void setUp() throws Exception
    {
        super.setUp();
        client = SeleniumStarter.getInstance().getSeleniumClient(getSeleniumConfigurations(), parallel);

        if (SeleniumStarter.getInstance().isManual())
        {
            SeleniumStarter.getInstance().start(getSeleniumConfigurations().get(0));
        }

        assertThat = new SeleniumAssertions(client, getSeleniumConfigurations().get(0), new TimeRecorder(this.getClass().getName()));
        onSetUp();
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

}
