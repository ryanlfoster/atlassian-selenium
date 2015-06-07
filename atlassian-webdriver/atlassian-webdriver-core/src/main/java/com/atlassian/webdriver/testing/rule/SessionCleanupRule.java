package com.atlassian.webdriver.testing.rule;

import com.atlassian.webdriver.browsers.WebDriverBrowserAutoInstall;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

/**
 * <p/>
 * A simple rule to clear the browsers session at the end of a test. This removes the need to have an @After
 * method that logs the user out of the TestedProduct.
 *
 * <p/>
 * This simply clears the cookies from the browser making it much quicker then actually
 * going through the products logout process.
 *
 * @since 2.1.0
 */
public class SessionCleanupRule extends FailsafeExternalResource
{

    private final Supplier<? extends WebDriver> webDriver;

    public SessionCleanupRule(Supplier<? extends WebDriver> webDriver)
    {
        this.webDriver = webDriver;
    }

    @Inject
    public SessionCleanupRule(WebDriver webDriver)
    {
        this(Suppliers.ofInstance(webDriver));
    }

    public SessionCleanupRule()
    {
        this(WebDriverBrowserAutoInstall.driverSupplier());
    }

    @Override
    protected void before() throws Throwable
    {
        cleanUp();
    }

    @Override
    protected void after()
    {
        cleanUp();
    }

    private void cleanUp()
    {
        final WebDriver driver = webDriver.get();
        if (driver != null)
        {
            driver.manage().deleteAllCookies();
        }
    }
}
