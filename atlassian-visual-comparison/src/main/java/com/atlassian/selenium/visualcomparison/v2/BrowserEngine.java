package com.atlassian.selenium.visualcomparison.v2;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.annotations.PublicSpi;
import com.atlassian.selenium.visualcomparison.ScreenElement;
import com.atlassian.selenium.visualcomparison.v2.settings.Resolution;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * An SPI to hook in the browser automation framework used to drive the visual comparison.
 *
 * @since 2.3
 */
@ExperimentalApi
@PublicSpi
public interface BrowserEngine
{
    /**
     * Get an {@link ScreenElement element} that is closest to the point in the document specified by {@code x} and
     * {@code y}.
     *
     * @param x x coordinate of the point of interest
     * @param y y coordinate of the point of interest
     * @return element at {@code (x,y)}
     */
    @Nonnull
    ScreenElement getElementAt(int x, int y);

    /**
     * Resize the browser window to the given {@code resolution}.
     *
     * @param resolution resolution to resize to
     * @return this engine instance for chaining.
     */
    @Nonnull
    BrowserEngine resizeTo(@Nonnull Resolution resolution);

    /**
     * Capture a screenshot of the current page. This should be an entire document and not just contents of the
     * viewport.
     *
     * @param file file to store the screenshot in
     * @return this engine instance for chaining.
     */
    @Nonnull
    BrowserEngine captureScreenshotTo(@Nonnull File file);

    /**
     * Refresh the current page.
     *
     * @return this engine instance for chaining.
     */
    @Nonnull
    BrowserEngine reloadPage();

    /**
     * Execute a Javascript snippet in the context of the current page.
     *
     * @param returnType expected return type. At the very least {@code String}, {@code Boolean} and {@link Long}
     *                   should be supported.
     * @param script script to execute
     * @param args arguments to the script
     * @param <T> return type parameter
     * @return result of the script
     * @throws ClassCastException if the result type was different than expected
     */
    @Nullable
    <T> T executeScript(@Nonnull Class<T> returnType, @Nonnull String script, @Nonnull Object... args);
}
