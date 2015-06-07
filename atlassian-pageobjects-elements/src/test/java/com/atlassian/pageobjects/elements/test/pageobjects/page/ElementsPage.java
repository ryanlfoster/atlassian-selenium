package com.atlassian.pageobjects.elements.test.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.webdriver.utils.by.ByJquery;

import javax.inject.Inject;

/**
 * Represents the elements.html
 */
public class ElementsPage implements Page
{
    @Inject
    protected PageBinder pageBinder;

    @Inject
    protected PageElementFinder elementFinder;

    @ElementBy(id="test1_addElementsButton")
    private PageElement test1_addElementsButton;

    @ElementBy(id="test1_delayedSpan")
    private PageElement test1_delayedSpan;

    @Override
    public String getUrl()
    {
        return "/html/elements.html";
    }

    @WaitUntil
    @IgnoreBrowser(value = Browser.HTMLUNIT_NOJS, reason = "Selector is not possible without jQuery")
    public void waitForTitle()
    {
        Poller.waitUntilTrue(elementFinder.find(ByJquery.$("h1:contains(Html Elements Page)")).timed().isPresent());
    }

    public PageElement test1_addElementsButton()
    {
        return test1_addElementsButton;
    }

     public PageElement test1_delayedSpan()
    {
        return test1_delayedSpan;
    }
}
