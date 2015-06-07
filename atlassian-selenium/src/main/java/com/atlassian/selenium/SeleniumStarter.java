package com.atlassian.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.RemoteControlConfiguration;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Helper class to setup the Selenium Proxy and client.
 */
public class SeleniumStarter
{
    private static final Logger log = Logger.getLogger(SeleniumStarter.class);

    private static SeleniumStarter instance = new SeleniumStarter();
    private SeleniumClient client;
    private SeleniumServer server;
    private String userAgent;
    private boolean manual = true;

    private SeleniumStarter()
    {
    }

    public static SeleniumStarter getInstance()
    {
        return instance;
    }

    public synchronized SeleniumClient getSeleniumClient(SeleniumConfiguration config)
    {
        if (client == null)
        {
            client = new SingleBrowserSeleniumClient(config);
        }
        return client;
    }

    public SeleniumClient getSeleniumClient(List<SeleniumConfiguration> configs) {
        return getSeleniumClient(configs, false);
    }

    /**
     * This method will create a Multi-Browser Selenium client with a browser corresponding to
     * each of the selenium configurations passed in in the list.
     */

    public synchronized SeleniumClient getSeleniumClient(List<SeleniumConfiguration> configs, boolean parallel)
    {
        if (client == null)
        {
            InvocationHandler ih = new MultiBrowserSeleniumClientInvocationHandler(configs, 10000000L, true, parallel);
            client = (SeleniumClient)Proxy.newProxyInstance(SeleniumClient.class.getClassLoader(), 
                                                             new Class[] {SeleniumClient.class}, ih );
        }
        return client;

    }

    public synchronized SeleniumServer getSeleniumServer(SeleniumConfiguration config)
    {
        if (server == null)
        {
            try
            {
                RemoteControlConfiguration rcConfig = new RemoteControlConfiguration();
                rcConfig.setPort(config.getServerPort());
                rcConfig.setDebugMode(true);
                rcConfig.setSingleWindow(config.getSingleWindowMode());                
                if (config.getFirefoxProfileTemplate() != null) {
                    rcConfig.setFirefoxProfileTemplate(new File(config.getFirefoxProfileTemplate()));
                }
                server = new SeleniumServer(rcConfig);
            } catch (Exception e)
            {
                log.error("Error creating SeleniumServer!", e);
            }
        }
        return server;
    }


    private void startServer(SeleniumConfiguration config)
    {
        try
        {
            if(config.getStartSeleniumServer())
            {
                log.info("Starting Selenium Server");
                getSeleniumServer(config).start();
                log.info("Selenium Server Started");
            }
            else
            {
                log.info("Not starting Selenium Server");
            }

        } catch (Exception e)
        {
            log.error("Error starting SeleniumServer!", e);
        }

    }

    public void start(SeleniumConfiguration config)
    {
        log.info("Starting Selenium");
        startServer(config);
        log.info("Starting Selenium Client");
        getSeleniumClient(config).start();
        log.info("Selenium Client Started");
        log.info("Selenium startup complete");
    }

    public void start(List<SeleniumConfiguration> configs)
    {
        log.info("Starting Selenium");
        startServer(configs.get(0));
        log.info("Starting Selenium Client");
        getSeleniumClient(configs).start();
        log.info("Selenium Client Started");
        log.info("Selenium startup complete");

    }

    public void stop()
    {
        if(client != null)
        {
            client.stop();
        }
        
        if(server != null)
        {
            server.stop();
            // we clear the server object so that any state inside the server object is
            // removed.  Otherwise people cant run individual tests in IDEA.
            server = null;
        }
    }

    public boolean isManual()
    {
        return manual;
    }

    public void setManual(boolean manual)
    {
        this.manual = manual;
    }
}
