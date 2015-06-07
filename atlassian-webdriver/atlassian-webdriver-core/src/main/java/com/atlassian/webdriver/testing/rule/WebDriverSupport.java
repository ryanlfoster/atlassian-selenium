package com.atlassian.webdriver.testing.rule;

import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.browsers.WebDriverBrowserAutoInstall;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p/>
 * Support for rules that need a {@link org.openqa.selenium.WebDriver} instance.
 *
 * <p/>
 * This class defines 'templates' for constructors to provide the WebDriver instance in a generic way. By default it
 * falls back to {@link com.atlassian.webdriver.LifecycleAwareWebDriverGrid}.
 *
 * @since 2.1
 */
public final class WebDriverSupport<WD extends WebDriver>
{
    public static WebDriverSupport<AtlassianWebDriver> fromAutoInstall()
    {
        return new WebDriverSupport<AtlassianWebDriver>(WebDriverBrowserAutoInstall.driverSupplier());
    }

    public static <VE extends WebDriver> WebDriverSupport<VE> forSupplier(Supplier<? extends VE> driverSupplier)
    {
        return new WebDriverSupport<VE>(driverSupplier);
    }

    public static <VE extends WebDriver> WebDriverSupport<VE> forInstance(VE webDriver)
    {
        return new WebDriverSupport<VE>(Suppliers.ofInstance(checkNotNull(webDriver, "webDriver")));
    }

    private final Supplier<? extends WD> driverSupplier;

    private WebDriverSupport(@Nonnull Supplier<? extends WD> driverSupplier)
    {
        this.driverSupplier = Suppliers.memoize(checkNotNull(driverSupplier, "driverSupplier"));
    }

    @Nonnull
    public final WD getDriver()
    {
        return driverSupplier.get();
    }

}
