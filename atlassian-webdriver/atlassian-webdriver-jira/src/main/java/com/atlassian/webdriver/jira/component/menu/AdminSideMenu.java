package com.atlassian.webdriver.jira.component.menu;

import com.atlassian.pageobjects.Page;
import com.atlassian.webdriver.jira.component.WebDriverLink;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


/**
 *
 */
public class AdminSideMenu 
{
    @FindBy(id = "adminMenu")
    private WebElement sideMenuContainer;

    public <T extends Page> T activate(WebDriverLink<T> link) {
        return link.activate(sideMenuContainer);
    }
}
