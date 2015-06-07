package com.atlassian.webdriver.confluence.component.menu;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.confluence.page.ConfluenceAdminHomePage;
import com.atlassian.webdriver.confluence.page.PeopleDirectoryPage;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Inject;

/**
 * TODO: Document this class / interface here
 *
 */
public class BrowseMenu
{
    @Inject
    AtlassianWebDriver driver;

    @Inject
    PageBinder pageBinder;

    @FindBy(id="administration-link")
    private WebElement adminPageLink;

    @FindBy(id="people-directory-link")
    private WebElement peopleDirectoryLink;

    private AjsDropdownMenu browseMenu;

    @Init
    public void initialise()
    {
        browseMenu = pageBinder.bind(AjsDropdownMenu.class, ByJquery.$("#browse-menu-link").parent("li"));
    }

    public ConfluenceAdminHomePage gotoAdminPage()
    {
        adminPageLink.click();
        return pageBinder.bind(ConfluenceAdminHomePage.class);
    }

    public PeopleDirectoryPage gotoPeopleDirectoryPage()
    {
        peopleDirectoryLink.click();
        return pageBinder.bind(PeopleDirectoryPage.class);
    }

    public BrowseMenu open()
    {
        browseMenu.open();
        return this;
    }

    public boolean isOpen()
    {
        return browseMenu.isOpen();
    }

    public BrowseMenu close()
    {
        browseMenu.close();
        return this;
    }
}
