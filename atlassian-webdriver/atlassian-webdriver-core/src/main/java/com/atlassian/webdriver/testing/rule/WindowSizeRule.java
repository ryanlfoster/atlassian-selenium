package com.atlassian.webdriver.testing.rule;

import com.atlassian.webdriver.testing.annotation.WindowSize;
import com.google.common.base.Supplier;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkState;

/**
 * <p/>
 * Implements support for the {@link com.atlassian.webdriver.testing.annotation.WindowSize} annotation.
 *
 * <p/>
 * If this rule is used in a test and the {@link com.atlassian.webdriver.testing.annotation.WindowSize} annotation is
 * not present on either the test method, or the test class, the window will be maximized by default.
 *
 * <p/>
 * Otherwise the directives present on the annotation found (with method-level annotation superseding the class-level
 * one) will be applied to the current driver's window in a following manner:
 * <ul>
 *     <li>if the <tt>maximize</tt> flag is set to <code>true</code>, the window will be maximized</li>
 *     <li>otherwise the properties <tt>height</tt> and <tt>width</tt> will be used to set the window size. In such
 *     case those properties must have positive integer value, or an exception will be raised</li>
 * </ul>
 *
 * <p/>
 * Example:
 *
 *
 * <pre>
 *      public class MyWebDriverTest
 *      {
 *          &#064;Rule public WindowSizeRule windowSizeRule = //...
 *
 *          &#064;Test
 *          public void myTest()
 *          {
 *              // at this stage the window will be maximized
 *          }
 *      }
 * </pre>
 *
 * <pre>
 *      public class MyWebDriverTest
 *      {
 *          &#064;Rule public WindowSizeRule windowSizeRule = //...
 *
 *          &#064;Test
 *          &#064;WindowSize(width=1024,height=768)
 *          public void myTest()
 *          {
 *              // at this stage the window will be set to 1024x768
 *          }
 *      }
 * </pre>
 *
 * @since 2.1
 */
public class WindowSizeRule extends TestWatcher
{
    private final static Logger log = LoggerFactory.getLogger(WindowSizeRule.class);

    private final WebDriverSupport<? extends WebDriver> support;

    @Inject
    public WindowSizeRule(WebDriver webDriver)
    {
        this.support = WebDriverSupport.forInstance(webDriver);
    }

    public WindowSizeRule(Supplier<? extends WebDriver> driverSupplier)
    {
        this.support = WebDriverSupport.forSupplier(driverSupplier);
    }

    public WindowSizeRule()
    {
        this.support = WebDriverSupport.fromAutoInstall();
    }

    @Override
    protected void starting(Description description)
    {
        try
        {
            WindowSize windowSize = findAnnotation(description);
            if (windowSize != null)
            {
                handleWindowSize(windowSize);
            }
            else
            {
                maximizeWindow();
            }
        }
        catch (WebDriverException e)
        {
            // half of the drivers does not support it, let's not break because of this
            log.warn("Caught exception while trying to adjust window size.");
            log.debug("Exception while trying to adjust window size", e);
        }
    }

    private WindowSize findAnnotation(Description description)
    {
        if (description.getAnnotation(WindowSize.class) != null)
        {
            return description.getAnnotation(WindowSize.class);
        }
        else if (description.getTestClass() != null && description.getTestClass().isAnnotationPresent(WindowSize.class))
        {
            return description.getTestClass().getAnnotation(WindowSize.class);
        }
        else
        {
            return null;
        }
    }

    private void handleWindowSize(WindowSize windowSize)
    {
        if (windowSize.maximize())
        {
            maximizeWindow();
        }
        else
        {
            setSize(computeDimension(windowSize));
        }
    }

    private void maximizeWindow()
    {
        support.getDriver().manage().window().maximize();
    }

    private void setSize(Dimension dimension)
    {
        support.getDriver().manage().window().setPosition(new Point(0,0));
        support.getDriver().manage().window().setSize(dimension);
        // _not_ a mistake... don't ask
        support.getDriver().manage().window().setSize(dimension);
    }

    private Dimension computeDimension(WindowSize windowSize)
    {
        checkState(windowSize.width() > 0, "@WindowSize width must be greater than 0, was: " + windowSize.width());
        checkState(windowSize.height() > 0, "@WindowSize height must be greater than 0, was: " + windowSize.height());
        return new Dimension(windowSize.width(), windowSize.height());
    }
}
