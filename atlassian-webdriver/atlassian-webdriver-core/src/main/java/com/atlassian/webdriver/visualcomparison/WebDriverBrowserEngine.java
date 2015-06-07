package com.atlassian.webdriver.visualcomparison;

import com.atlassian.selenium.visualcomparison.ScreenElement;
import com.atlassian.selenium.visualcomparison.v2.BrowserEngine;
import com.atlassian.selenium.visualcomparison.v2.settings.Resolution;
import com.atlassian.webdriver.debug.WebDriverDebug;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.atlassian.webdriver.utils.JavaScriptUtils.execute;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @since 2.3
 */
public final class WebDriverBrowserEngine implements BrowserEngine
{
    private static final Logger log = LoggerFactory.getLogger(WebDriverBrowserEngine.class);

    private final WebDriver webDriver;

    @Inject
    public WebDriverBrowserEngine(@Nonnull WebDriver webDriver)
    {
        this.webDriver = checkNotNull(webDriver, "webDriver");
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public ScreenElement getElementAt(int x, int y)
    {
        try
        {
            ElementByPoint elementByPoint = new ElementByPoint(x, y).calculate();
            // scroll to put the element in viewport
            executeScript(Object.class, format("window.scrollTo(%d,%d)",
                    elementByPoint.scrollTo.getX(), elementByPoint.scrollTo.getY()));
            final WebElement element = webDriver.findElement(byPoint(elementByPoint.elementCoordinates));
            return new SimpleScreenElement(executeScript(String.class,
                    "var d = document.createElement('div'); d.appendChild(arguments[0].cloneNode(true)); return d.innerHTML;",
                    element));
        }
        catch (Exception e)
        {
            log.error(format("Error while getting screen element at %d,%d", x, y), e);
            return new SimpleScreenElement("");
        }
    }

    @Nonnull
    @Override
    public BrowserEngine resizeTo(@Nonnull Resolution resolution)
    {
        setSize(resolution);
        // NOT a mistake... WebDriver sometimes needs an extra kick in butt
        setSize(resolution);
        return this;
    }

    @Nonnull
    @Override
    public BrowserEngine captureScreenshotTo(@Nonnull File file)
    {
        new WebDriverDebug(webDriver).takeScreenshotTo(file);
        return this;
    }

    @Nonnull
    @Override
    public BrowserEngine reloadPage()
    {
        webDriver.navigate().refresh();
        return this;
    }

    @Nullable
    @Override
    public <T> T executeScript(@Nonnull Class<T> returnType, @Nonnull String script, @Nonnull Object... args)
    {
        return execute(returnType, script, webDriver, args);
    }

    private By byPoint(final Point coordinates)
    {
        return new By()
        {
            @Override
            public List<WebElement> findElements(SearchContext context)
            {
                final String findByPoint = format("return document.elementFromPoint(%d,%d);", coordinates.getX(),
                        coordinates.getY());
                final WebElement element = execute(WebElement.class, findByPoint, webDriver);
                return element != null ? Collections.singletonList(element) : Collections.<WebElement>emptyList();
            }

            @Override
            public String toString()
            {
                return format("By point in viewport %d,%d", coordinates.getX(), coordinates.getY());
            }
        };
    }

    @SuppressWarnings("ConstantConditions")
    private Dimension getDimensionFor(String selector)
    {
        long x = executeScript(Long.class, "return jQuery(" + selector + ").width();");
        long y = executeScript(Long.class, "return jQuery(" + selector + ").height();");
        return new Dimension((int) x, (int) y);
    }

    private void setSize(Resolution resolution)
    {
        webDriver.manage().window().setSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
    }

    private final class ElementByPoint
    {
        private final int x;
        private final int y;

        private Dimension viewport;
        private Dimension document;

        Point scrollTo;
        Point elementCoordinates;

        private ElementByPoint(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        ElementByPoint calculate()
        {
            this.document = getDimensionFor("document");
            this.viewport = getDimensionFor("window");
            calculateScroll();
            calculateCoordinates();
            return this;
        }

        private void calculateScroll()
        {
            int deltaX = Math.max(0, document.getWidth() - viewport.getWidth());
            int scrollX = Math.min(x, deltaX);
            int deltaY = Math.max(0, document.getHeight() - viewport.getHeight());
            int scrollY = Math.min(y, deltaY);
            scrollTo = new Point(scrollX, scrollY);
        }

        private void calculateCoordinates()
        {
            int relativeX = x - scrollTo.getX();
            int relativeY = y - scrollTo.getY();
            elementCoordinates = new Point(relativeX, relativeY);
        }
    }

    private static final class SimpleScreenElement implements ScreenElement
    {

        private final String html;

        private SimpleScreenElement(@Nonnull String html)
        {
            this.html = html;
        }

        @Override
        @Nonnull
        public String getHtml()
        {
            return html;
        }
    }

}
