package com.atlassian.pageobjects.elements.test.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.test.pageobjects.component.JQueryMenu;
import com.atlassian.webdriver.utils.by.ByJquery;

import javax.inject.Inject;

/**
 * Represents the jquery.html
 */
public class JQueryPage implements Page
{
    @Inject
    protected PageBinder pageBinder;

    @Inject
    protected PageElementFinder elementFinder;

    public String getUrl()
    {
        return "/html/jquery.html";
    }

    @WaitUntil
    public void doWait()
    {
        Poller.waitUntilTrue(elementFinder.find(ByJquery.$("h2:contains(JQuery DropDown Menu)")).timed().isPresent());
    }

    public JQueryMenu jqueryMenu()
    {
        return pageBinder.bind(JQueryMenu.class);
    }

    public JQueryMenu openJqueryMenu() {
        return pageBinder.bind(JQueryMenu.class).open();
    }
}
