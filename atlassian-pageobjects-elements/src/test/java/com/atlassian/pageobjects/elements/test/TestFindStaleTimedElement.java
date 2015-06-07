package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.elements.GlobalElementFinder;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.Assert.assertEquals;

@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestFindStaleTimedElement extends AbstractPageElementBrowserTest
{
    private PageElementFinder elementFinder;

    @Before
    public void initFinder()
    {
        elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
    }

    @Test
    public void staleElementsShouldBeSuccessfullyLocated()
    {
        product.visit(ElementsPage.class);

        PageElement leafList = elementFinder.find(By.id("test10_leafList"));
        // reload the element
        elementFinder.find(By.id("test10_reloadChildListButton")).click();

        Poller.waitUntilTrue(leafList.timed().isPresent());
        assertEquals(3, leafList.findAll(By.className("test10_item")).size());
    }

    @Test
    public void staleElementsInListShouldBeSuccessfullyLocated()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("test10_childList"));
        List<PageElement> items = parent.findAll(By.className("test10_item"));

        // reload the items
        elementFinder.find(By.id("test10_reloadChildElementsButton")).click();

        assertEquals(3, items.size());
        Poller.waitUntilTrue(items.get(0).timed().isPresent());
        Poller.waitUntilTrue(items.get(0).timed().hasText("Item 1"));
        Poller.waitUntilTrue(items.get(1).timed().isPresent());
        Poller.waitUntilTrue(items.get(1).timed().hasText("Item 2"));
        Poller.waitUntilTrue(items.get(2).timed().isPresent());
        Poller.waitUntilTrue(items.get(2).timed().hasText("Item 3"));
    }
}
