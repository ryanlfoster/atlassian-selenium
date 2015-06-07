package com.atlassian.webdriver.confluence.component.menu;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.confluence.page.LogoutPage;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Inject;

/**
 * TODO: Document this class / interface here
 *
 */
public class ConfluenceUserMenu
{
    private final By USER_MENU_LOCATOR = ByJquery.$("#user-menu-link").parent("li");

    @Inject
    AtlassianWebDriver driver;

    @Inject
    PageBinder pageBinder;

    @FindBy(id="logout-link")
    private WebElement logoutLink;


    private AjsDropdownMenu userMenu;

    @Init
    public void initialise()
    {
        userMenu = pageBinder.bind(AjsDropdownMenu.class, USER_MENU_LOCATOR);
    }

    public LogoutPage logout()
    {
        logoutLink.click();
        return pageBinder.bind(LogoutPage.class);
    }

    public ConfluenceUserMenu open()
    {
        userMenu.open();
        return this;
    }

    public boolean isOpen()
    {
        return userMenu.isOpen();
    }

    public ConfluenceUserMenu close()
    {
        userMenu.close();
        return this;
    }
}
