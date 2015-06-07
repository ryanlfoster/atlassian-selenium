package com.atlassian.webdriver.browsers.firefox;

import com.atlassian.browsers.BrowserConfig;
import com.atlassian.webdriver.browsers.profile.ProfilePreferences;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Map;

/**
 * A helper utility for obtaining a FirefoxDriver.
 * @since 2.0
 */
public final class FirefoxBrowser
{
    private static final Logger log = LoggerFactory.getLogger(FirefoxBrowser.class);

    private FirefoxBrowser()
    {
        throw new IllegalStateException("FirefoxBrowser is not constructable");
    }

    /**
     * Gets the default FirefoxDriver that tries to use the default system paths
     * for where the firefox binary should be
     * @return Default configured FirefoxDriver
     */
    public static FirefoxDriver getFirefoxDriver()
    {
        FirefoxBinary firefox = new FirefoxBinary();
        setSystemProperties(firefox);
        return constructFirefoxDriver(firefox, null);
    }

    /**
     * Configures a FirefoxDriver based on the browserConfig
     * @param browserConfig browser config that points to the binary path of the browser and
     * optional profile
     * @return A configured FirefoxDriver based on the browserConfig passed in
     */
    public static FirefoxDriver getFirefoxDriver(BrowserConfig browserConfig)
    {
        FirefoxBinary firefox;
        FirefoxProfile profile = null;

        if (browserConfig != null)
        {
            firefox = new FirefoxBinary(new File(browserConfig.getBinaryPath()));
            if (browserConfig.getProfilePath() != null)
            {
                File profilePath = new File(browserConfig.getProfilePath());

                profile = new FirefoxProfile();

                addExtensionsToProfile(profile, profilePath);
                addPreferencesToProfile(profile, profilePath);
            }

            setSystemProperties(firefox);
            return constructFirefoxDriver(firefox, profile);
        }

        // Fall back on default firefox driver
        return getFirefoxDriver();
    }

    private static void addPreferencesToProfile(FirefoxProfile profile, File profilePath)
    {
        File profilePreferencesFile = new File(profilePath, "profile.preferences");

        if (profilePreferencesFile.exists())
        {
            ProfilePreferences profilePreferences = new ProfilePreferences(profilePreferencesFile);
            Map<String, Object> preferences = profilePreferences.getPreferences();
            for (String key : preferences.keySet())
            {
                Object value = preferences.get(key);
                if (value instanceof Integer)
                {
                    profile.setPreference(key, (Integer)value);
                }
                else if (value instanceof Boolean)
                {
                    profile.setPreference(key, (Boolean)value);
                }
                else
                {
                    profile.setPreference(key, (String)value);
                }
            }
        }
    }

    private static void addExtensionsToProfile(FirefoxProfile profile, File profilePath)
    {
        // Filter the extentions path to only include extensions.
        for (File extension : profilePath.listFiles(new FileFilter()
            {
                public boolean accept(final File file)
                {
                    if (file.getName().matches(".*\\.xpi$"))
                    {
                        return true;
                    }

                    return false;
                }
            }))
        {
            try
            {
                profile.addExtension(extension);
            }
            catch (IOException e)
            {
                log.error("Unable to load extension: " + extension, e);
            }
        }
    }

    /**
     * Gets a firefox driver based on the browser path based in
     * @param browserPath the path to the firefox binary to use for the firefox driver.
     * @return A FirefoxDriver that is using the binary at the browserPath
     */
    public static FirefoxDriver getFirefoxDriver(String browserPath)
    {
        FirefoxBinary firefox;

        if (browserPath != null)
        {
            firefox = new FirefoxBinary(new File(browserPath));
            setSystemProperties(firefox);
            return constructFirefoxDriver(firefox, null);
        }

        // Fall back on default firefox driver
        log.info("Browser path was null, falling back to default firefox driver.");
        return getFirefoxDriver();
    }

    private static FirefoxDriver constructFirefoxDriver(final FirefoxBinary binary, FirefoxProfile profile)
    {
        if (profile == null)
        {
            profile = new FirefoxProfile();
        }

        try
        {
            JavaScriptError.addExtension(profile);
        }
        catch (IOException e)
        {
            log.error("Failed to add JavaScriptError extension to profile", e);
        }

        return new FirefoxDriver(binary, profile);
    }

    /**
     * Sets up system properties on the firefox driver.
     * @param firefox
     */
    private static void setSystemProperties(FirefoxBinary firefox)
    {
        if (System.getProperty("DISPLAY") != null)
        {
            firefox.setEnvironmentProperty("DISPLAY", System.getProperty("DISPLAY"));
        }
    }
}
