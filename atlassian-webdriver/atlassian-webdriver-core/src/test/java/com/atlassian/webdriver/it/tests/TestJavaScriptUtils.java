package com.atlassian.webdriver.it.tests;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.browser.RequireBrowser;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.JavaScriptUtilsPage;
import com.atlassian.webdriver.utils.JavaScriptUtils;
import com.atlassian.webdriver.utils.MouseEvents;
import com.atlassian.webdriver.utils.element.WebDriverPoller;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static com.atlassian.webdriver.utils.element.ElementConditions.isNotVisible;
import static com.atlassian.webdriver.utils.element.ElementConditions.isVisible;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestJavaScriptUtils extends AbstractSimpleServerTest
{

    @Inject private WebDriverPoller poller;
    @Inject private WebDriver driver;

    @Before
    public void init()
    {
        poller = poller.withDefaultTimeout(5, TimeUnit.SECONDS);
        product.visit(JavaScriptUtilsPage.class);
        driver = product.getTester().getDriver();
        // move away from any other tested element
        MouseEvents.hover(By.id("hover-sink"), driver);
        JavaScriptUtils.execute("window.hideAll();", driver);
    }

    /**
     * This test is testing that css psuedo class :hover will display an element
     */
    @Test
    @IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.IE}, reason = "CSS hovering on elements does not work.")
    public void testCssHoverRespondsToMouseover()
    {
        WebElement hoveringDiv = driver.findElement(By.id("hovering-element"));
        assertTrue(hoveringDiv.isDisplayed());
        assertCssChidrenNotVisible();

        // now hover over div to show the children
        MouseEvents.hover(hoveringDiv, driver);
        assertCssChidrenVisible();
    }

    @Test
    @IgnoreBrowser(Browser.IE)
    public void testJQueryHoverRespondsToMouseover()
    {
        WebElement hoveringDiv = driver.findElement(By.id("hovering-jquery-element"));
        assertTrue(hoveringDiv.isDisplayed());
        assertJQueryChildrenNotVisible();

        // now hover over div to show the children
        MouseEvents.hover(hoveringDiv, driver);
        assertJQueryChildrenVisible();
    }

    @Test(expected = TimeoutException.class)
    @RequireBrowser({Browser.HTMLUNIT, Browser.IE})
    public void testCssHoverBreaksForSomeBrowsers()
    {
        WebElement hoveringDiv = driver.findElement(By.id("hovering-element"));
        assertTrue(hoveringDiv.isDisplayed());
        assertCssChidrenNotVisible();

        // now hover over div to show the children
        MouseEvents.hover(hoveringDiv, driver);
        assertCssChidrenVisible();
        fail("HtmlUnit/IE CSS hovers are working now - check the browser used for this test");
    }

    @Test(expected = TimeoutException.class)
    @RequireBrowser({Browser.IE})
    public void testJQueryHoverBreaksForSomeBrowsers()
    {
        WebElement hoveringDiv = driver.findElement(By.id("hovering-element"));
        assertTrue(hoveringDiv.isDisplayed());
        assertJQueryChildrenNotVisible();

        // now hover over div to show the children
        MouseEvents.hover(hoveringDiv, driver);
        assertJQueryChildrenVisible();
        fail("IE JQuery hover is working now");
    }

    @Test
    public void testMouseEventMouseOut()
    {
        WebElement mouseoutDiv = driver.findElement(By.id("mouseout-jquery-element"));
        assertTrue(mouseoutDiv.isDisplayed());

        WebElement mouseoutContainer = driver.findElement(By.id("mouseout-container"));
        assertEquals("no mouseout", mouseoutContainer.getText());

        MouseEvents.mouseout(mouseoutDiv, driver);
        assertEquals("mouseout", mouseoutContainer.getText());

    }

    private void assertCssChidrenNotVisible()
    {
        poller.waitUntil(isNotVisible(By.id("child-element-one")));
        poller.waitUntil(isNotVisible(By.id("child-element-two")));
        poller.waitUntil(isNotVisible(By.id("child-element-three")));
    }

    private void assertCssChidrenVisible()
    {
        poller.waitUntil(isVisible(By.id("child-element-one")));
        poller.waitUntil(isVisible(By.id("child-element-two")));
        poller.waitUntil(isVisible(By.id("child-element-three")));
    }

    private void assertJQueryChildrenNotVisible()
    {
        poller.waitUntil(isNotVisible(By.id("child-jquery-element-one")));
        poller.waitUntil(isNotVisible(By.id("child-jquery-element-two")));
        poller.waitUntil(isNotVisible(By.id("child-jquery-element-three")));
    }

    private void assertJQueryChildrenVisible()
    {
        poller.waitUntil(isVisible(By.id("child-jquery-element-one")));
        poller.waitUntil(isVisible(By.id("child-jquery-element-two")));
        poller.waitUntil(isVisible(By.id("child-jquery-element-three")));
    }
}
