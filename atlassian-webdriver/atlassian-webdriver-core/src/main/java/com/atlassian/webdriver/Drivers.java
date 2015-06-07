package com.atlassian.webdriver;

import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.webdriver.pageobjects.WebDriverTester;
import com.google.common.base.Supplier;

/**
 * Utils for getting and manipulating {@link org.openqa.selenium.WebDriver} instances.
 *
 * @since 2.1
 */
public final class Drivers
{

    private Drivers()
    {
        throw new AssertionError("Don't instantiate me");
    }


    public static Supplier<AtlassianWebDriver> fromProduct(final TestedProduct<? extends WebDriverTester> product)
    {
        return new Supplier<AtlassianWebDriver>()
        {
            @Override
            public AtlassianWebDriver get()
            {
                return product.getTester().getDriver();
            }
        };
    }
}
