package com.atlassian.webdriver.confluence.component.header;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.component.WebSudoBanner;
import com.atlassian.pageobjects.page.HomePage;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.utils.Check;
import com.google.common.base.Preconditions;
import org.openqa.selenium.By;

import javax.inject.Inject;

/**
 * @since 2.1.0
 */
public class ConfluenceWebSudoBanner implements WebSudoBanner
{
    private static final By WEBSUDO_LOCATOR = By.id("confluence-message-websudo-message");

    @Inject
    AtlassianWebDriver driver;

    @Inject
    PageBinder pageBinder;

    public boolean isShowing()
    {
        return Check.elementExists(WEBSUDO_LOCATOR, driver) &&
                Check.elementIsVisible(WEBSUDO_LOCATOR, driver);
    }

    public String getMessage()
    {
        return isShowing() ? driver.findElement(WEBSUDO_LOCATOR).getText() : null;
    }

    public <M extends Page> M dropWebSudo(final Class<M> nextPage)
    {
        Preconditions.checkNotNull("nextPage can not be null.", nextPage);

        if (isShowing())
        {
            driver.findElement(By.id("websudo-drop")).click();
            driver.waitUntilElementIsNotVisible(WEBSUDO_LOCATOR);
            return HomePage.class.isAssignableFrom(nextPage) ?
                    pageBinder.bind(nextPage) : pageBinder.navigateToAndBind(nextPage);
        }
        else
        {
            return pageBinder.navigateToAndBind(nextPage);
        }

    }
}
