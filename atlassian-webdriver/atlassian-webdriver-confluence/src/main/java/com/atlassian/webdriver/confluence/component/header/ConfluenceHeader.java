package com.atlassian.webdriver.confluence.component.header;

import javax.inject.Inject;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.component.Header;
import com.atlassian.pageobjects.component.WebSudoBanner;
import com.atlassian.pageobjects.page.HomePage;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.confluence.component.UserDiscoverable;
import com.atlassian.webdriver.confluence.component.menu.BrowseMenu;
import com.atlassian.webdriver.confluence.component.menu.ConfluenceUserMenu;

import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 2.0
 */
public class ConfluenceHeader implements Header, UserDiscoverable
{

    @Inject
    AtlassianWebDriver driver;

    @Inject
    PageBinder pageBinder;

    @FindBy(id = "header")
    WebElement headerElement;

    private final static By USER_MENU_LOCATOR = By.id("user-menu-link");
    private final static By ADMIN_MENU_LOCATOR = By.id("administration-link");


    public boolean isLoggedIn()
    {
        return driver.elementExistsAt(USER_MENU_LOCATOR, headerElement);
    }

    public <M extends Page> M logout(Class<M> nextPage)
    {
        if (isLoggedIn()) {
            getUserMenu().open().logout();
        }
        return HomePage.class.isAssignableFrom(nextPage) ? pageBinder.bind(nextPage) : pageBinder.navigateToAndBind(nextPage);
    }

    public WebSudoBanner getWebSudoBanner()
    {
        return pageBinder.bind(WebSudoBanner.class);
    }

    public boolean isAdmin()
    {
        return driver.elementExistsAt(ADMIN_MENU_LOCATOR, headerElement);
    }

    public ConfluenceUserMenu getUserMenu()
    {
        if (isLoggedIn())
        {
            return pageBinder.bind(ConfluenceUserMenu.class);
        }
        else
        {
            throw new RuntimeException("Tried to get the user menu but the user is not logged in.");
        }
    }

    public BrowseMenu getBrowseMenu()
    {
        return pageBinder.bind(BrowseMenu.class);
    }
}
