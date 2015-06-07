package com.atlassian.pageobjects.elements.test.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElements;
import com.atlassian.pageobjects.elements.query.Poller;
import org.openqa.selenium.By;

public class PageElementSearchPage implements Page
{
    @ElementBy(tagName = PageElements.BODY)
    protected PageElement body;

    @ElementBy(id = "table-list")
    private PageElement tableRoot;

    @ElementBy(id = "async-update-parent")
    private PageElement asyncRoot;

    @Override
    public String getUrl()
    {
        return "/html/page-element-search.html";
    }

    @WaitUntil
    public void waitForPageLoad()
    {
        Poller.waitUntilTrue(body.timed().hasAttribute("id", "page-element-search-page"));
    }

    public PageElement getTableRoot()
    {
        return tableRoot;
    }

    public PageElement getAsyncRoot() {
        return asyncRoot;
    }

    public PageElementSearchPage clickAsyncButton(int buttonNumber) {
        findAsyncButton(buttonNumber).click();

        return this;
    }

    protected PageElement findAsyncButton(int number) {
        return asyncRoot.find(By.id("async-to-state-" + number));
    }
}
