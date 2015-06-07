package com.atlassian.webdriver;

import com.atlassian.browsers.BrowserConfig;
import com.atlassian.webdriver.browsers.AutoInstallConfiguration;
import com.atlassian.webdriver.utils.WebDriverUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.atlassian.webdriver.utils.WebDriverUtil.getUnderlyingDriver;

/**
 * <p/>
 * Simple lifecycle aware Webdriver helper that will setup auto browsers and then retrieve a driver from the factory.
 * Once the driver is running it will be re-used if the same browser property is retrieved again.
 *
 * <p/>
 * When the runtime is shutdown it will handle cleaning up the browser. It also provides a way to manually quit all
 * the registered drivers/browsers via {@link #shutdown()}. When that method is called, the shutdown hooks for those
 * browser will be unregistered.
 *
 * @since 2.1
 */
public class LifecycleAwareWebDriverGrid
{
    private static final Logger log = LoggerFactory.getLogger(LifecycleAwareWebDriverGrid.class);
    private final static Map<String,AtlassianWebDriver> drivers = new ConcurrentHashMap<String, AtlassianWebDriver>();
    private volatile static AtlassianWebDriver currentDriver;

    private static final Map<String,WeakReference<Thread>> SHUTDOWN_HOOKS = new ConcurrentHashMap<String, WeakReference<Thread>>();

    private LifecycleAwareWebDriverGrid() {}

    /**
     * Retrieves the driver from the {@link WebDriverFactory}. If an instance
     * of the driver is already running then it will be re-used instead of
     * creating a new instance.
     *
     * @return an instance of the driver
     */
    public static AtlassianWebDriver getDriver()
    {
        String browserProperty = WebDriverFactory.getBrowserProperty();
        if (browserIsConfigured(browserProperty))
        {
            AtlassianWebDriver driver = drivers.get(browserProperty);
            currentDriver = driver;
            return driver;
        }

        AtlassianWebDriver driver = null;
        if (RemoteWebDriverFactory.matches(browserProperty))
        {
            log.info("Loading remote driver: " + browserProperty);
            driver = RemoteWebDriverFactory.getDriver(browserProperty);
        }
        else
        {
            log.info("Loading local driver: " + browserProperty);
            BrowserConfig browserConfig = AutoInstallConfiguration.setupBrowser();
            driver = WebDriverFactory.getDriver(browserConfig);
        }
        drivers.put(browserProperty, driver);
        currentDriver = driver;

        addShutdownHook(browserProperty, driver);
        return driver;
    }

    public static AtlassianWebDriver getCurrentDriver()
    {
        Preconditions.checkState(currentDriver != null, "The current driver has not been initialised");
        return currentDriver;
    }

    public static Supplier<AtlassianWebDriver> currentDriverSupplier()
    {
        return new Supplier<AtlassianWebDriver>()
        {
            @Override
            public AtlassianWebDriver get()
            {
                return getCurrentDriver();
            }
        };
    }

    /**
     * A manual shut down of the registered drivers. This basically resets the grid to a blank state. The shutdown hooks
     * for the drivers that have been closed are also removed.
     *
     */
    public static void shutdown()
    {
        for (Map.Entry<String,AtlassianWebDriver> driver: drivers.entrySet())
        {
            quit(driver.getValue());
            removeHook(driver);
        }
        drivers.clear();
        SHUTDOWN_HOOKS.clear();
        currentDriver = null;
    }

    private static void removeHook(Map.Entry<String, AtlassianWebDriver> driver)
    {
        WeakReference<Thread> hookRef = SHUTDOWN_HOOKS.get(driver.getKey());
        final Thread hook = hookRef != null ? hookRef.get() : null;
        if (hook != null)
        {
            Runtime.getRuntime().removeShutdownHook(hook);
        }
    }

    private static void quit(AtlassianWebDriver webDriver)
    {
        try
        {
            webDriver.quit();
        }
        catch (WebDriverException e)
        {
            onQuitError(webDriver, e);
        }
    }


    private static boolean browserIsConfigured(String browserProperty)
    {
        return drivers.containsKey(browserProperty);
    }

    private static void addShutdownHook(final String browserProperty, final WebDriver driver) {
        final Thread quitter = new Thread()
        {
            @Override
            public void run()
            {
                log.debug("Running shut down hook for {}", driver);
                try
                {
                    drivers.remove(browserProperty);
                    if (driver.equals(currentDriver))
                    {
                        currentDriver = null;
                    }
                    log.info("Quitting {}", getUnderlyingDriver(driver));
                    driver.quit();
                    log.debug("Finished shutdown hook {}", this);
                }
                catch (NullPointerException e)
                {
                    // SELENIUM-247: suppress this error (but log it) since it's likely to be due to a harmless
                    // known issue where we're trying to clean up resources that the driver already cleaned up.
                    onQuitError(driver, e);
                }
                catch (WebDriverException e)
                {
                    onQuitError(driver, e);
                }
            }
        };
        SHUTDOWN_HOOKS.put(browserProperty, new WeakReference<Thread>(quitter));
        Runtime.getRuntime().addShutdownHook(quitter);
    }


    private static void onQuitError(WebDriver webDriver, Exception e)
    {
        // there is no sense propagating the exception, and in 99 cases out of 100, the browser is already dead if an
        // exception happens
        log.warn("Exception when trying to quit driver {}: {}", webDriver, e.getMessage());
        log.debug("Exception when trying to quit driver - details", e);
    }

}
