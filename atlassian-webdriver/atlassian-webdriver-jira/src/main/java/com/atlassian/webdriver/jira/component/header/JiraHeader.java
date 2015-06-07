package com.atlassian.webdriver.jira.component.header;

import javax.inject.Inject;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.pageobjects.component.Header;
import com.atlassian.pageobjects.component.WebSudoBanner;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.jira.component.UserDiscoverable;
import com.atlassian.webdriver.jira.component.menu.AdminMenu;
import com.atlassian.webdriver.jira.component.menu.DashboardMenu;
import com.atlassian.webdriver.jira.component.menu.JiraUserMenu;
import com.atlassian.webdriver.jira.page.LogoutPage;

import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 2.0
 */
public class JiraHeader implements UserDiscoverable, Header
{
    @Inject
    PageBinder pageBinder;

    @Inject
    AtlassianWebDriver driver;

    @FindBy(id="header")
    private WebElement headerElement;

    private String userName;

    @Init
    public void init()
    {
        By byId = By.id("header-details-user-fullname");

        userName = driver.elementIsVisible(byId) ? driver.findElement(byId).getText() : null;
    }

    public DashboardMenu getDashboardMenu()
    {
        return pageBinder.bind(DashboardMenu.class);
    }
    public AdminMenu getAdminMenu()
    {
        return pageBinder.bind(AdminMenu.class);
    }
    public JiraUserMenu getUserMenu()
    {
        return pageBinder.bind(JiraUserMenu.class);
    }

    public boolean isLoggedIn()
    {
        return userName != null;
    }

    public boolean isAdmin()
    {
        return isLoggedIn() && driver.elementExistsAt(By.id("admin_link"), headerElement);
    }

    public <M extends Page> M logout(Class<M> nextPage)
    {
        if (isLoggedIn()) {
            pageBinder.navigateToAndBind(LogoutPage.class).confirmLogout();
        }
        return LogoutPage.class.isAssignableFrom(nextPage) ? pageBinder.bind(nextPage) : pageBinder.navigateToAndBind(nextPage);
    }

    public WebSudoBanner getWebSudoBanner()
    {
        throw new NotImplementedException();
    }
}
