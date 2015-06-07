package com.atlassian.webdriver.debug;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.atlassian.webdriver.utils.WebDriverUtil.as;
import static com.atlassian.webdriver.utils.WebDriverUtil.isInstance;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An injectable component for performing common debug operations using {@link WebDriver}.
 *
 * @since 2.2
 */
public final class WebDriverDebug
{
    private static final Logger log = LoggerFactory.getLogger(WebDriverDebug.class);

    private final WebDriver webDriver;

    @Inject
    public WebDriverDebug(@Nonnull WebDriver webDriver)
    {
        this.webDriver = checkNotNull(webDriver, "webDriver");
    }


    /**
     * Get the URL that the underlying web driver is currently at.
     *
     * @return current URL
     */
    @Nonnull
    public String getCurrentUrl()
    {
        return webDriver.getCurrentUrl();
    }

    /**
     * <p/>
     * Writes the source of the last loaded page to the specified file. See {@link #dumpPageSourceTo(java.io.Writer)}
     * for details on how it works.
     *
     * <p/>
     * As opposed to {@link #dumpPageSourceTo(java.io.Writer)}, this method makes sure to close the handle used
     * to write to {@code dumpFile}.
     *
     * @param dumpFile File to write the source to.
     *
     */
    public boolean dumpSourceTo(@Nonnull File dumpFile)
    {
        checkNotNull(dumpFile, "dumpFile");
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(dumpFile);
            IOUtils.write(webDriver.getPageSource(), fileWriter);
            return true;
        }
        catch (IOException e)
        {
            log.warn("Error dumping page source to " + dumpFile.getAbsolutePath() + ": " + e.getMessage());
            log.debug("Error dumping page source - details", e);
            return false;
        }
        finally
        {
            IOUtils.closeQuietly(fileWriter);
        }
    }


    /**
     * <p/>
     * Writes the source of the last loaded page to the specified writer. The writer is othewrwise not interacted with.
     * In particular, clients have to take care to close the {@code writer}.
     *
     * <p/>
     * This method will attempt to not propagate any exceptions that prevent it from completing. Instead, the write will
     * be aborted, the exception logged and {@literal false} returned. The only exception raised by this method is
     * {@link NullPointerException} in case {@code writer} is {@literal null}.
     *
     * @param writer writer to write the page source to
     */
    public boolean dumpPageSourceTo(@Nonnull Writer writer)
    {
        checkNotNull(writer, "writer");
        try
        {
            IOUtils.write(webDriver.getPageSource(), writer);
            return true;
        }
        catch (IOException e)
        {
            log.warn("Error dumping page source to " + writer + ": " + e.getMessage());
            log.debug("Error dumping page source - details", e);
            return false;
        }
    }


    /**
     * Saves screen shot of the browser to the specified file.
     *
     * @param destFile File to save screen shot.
     */
    public boolean takeScreenshotTo(@Nonnull File destFile)
    {
        checkNotNull(destFile, "destFile");
        if (isInstance(webDriver, TakesScreenshot.class))
        {
            TakesScreenshot shotter = as(webDriver, TakesScreenshot.class);
            log.info("Saving screenshot to: " + destFile.getAbsolutePath());
            try
            {
                File screenshot = shotter.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshot, destFile);
                return true;
            }
            catch (IOException e)
            {
                logScreenshotError(destFile, e);
            }
            catch (WebDriverException e)
            {
                logScreenshotError(destFile, e);
            }
        }
        else
        {
            log.warn("Driver {} is not capable of taking screenshots.", webDriver);
        }
        return false;
    }

    private void logScreenshotError(File destFile, Exception e)
    {
        log.warn("Could not capture screenshot to {}: {}", destFile, e.getMessage());
        log.debug("Capture screenshot - error details", e);
    }
}
