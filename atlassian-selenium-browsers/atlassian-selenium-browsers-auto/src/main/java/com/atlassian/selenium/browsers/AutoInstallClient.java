package com.atlassian.selenium.browsers;

import com.atlassian.selenium.SeleniumAssertions;
import com.atlassian.selenium.SeleniumClient;
import com.atlassian.selenium.SeleniumConfiguration;
import com.atlassian.selenium.SeleniumStarter;

/**
 * Client that supports automatically installing the appropriate browser for the environment
 *
 * @since 2.0
 */
public class AutoInstallClient
{
    private static final SeleniumClient client;

    private static SeleniumAssertions assertThat;
    private static AutoInstallConfiguration config;

    static
    {
        config = new AutoInstallConfiguration();
        if (SeleniumStarter.getInstance().isManual())
        {
            SeleniumStarter.getInstance().start(config);
        }

        client = SeleniumStarter.getInstance().getSeleniumClient(config);
        assertThat = new SeleniumAssertions(client, config);
    }

    public static SeleniumClient seleniumClient()
    {
        return client;
    }

    public static SeleniumAssertions assertThat()
    {
        return assertThat;
    }

    public static SeleniumConfiguration seleniumConfiguration()
    {
        return config;
    }
}
