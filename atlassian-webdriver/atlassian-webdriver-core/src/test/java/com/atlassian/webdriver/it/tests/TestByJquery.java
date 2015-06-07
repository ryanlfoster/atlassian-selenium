package com.atlassian.webdriver.it.tests;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.browser.RequireBrowser;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.ByJqueryPage;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for checking ByJquery functionality in Atlassian WebDriver.
 */
@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestByJquery extends AbstractSimpleServerTest
{

    ByJqueryPage byJqueryPage;
    WebDriver driver;

    @Before
    public void init()
    {
        byJqueryPage = product.visit(ByJqueryPage.class);
        driver = product.getTester().getDriver();
    }
    
    @Test
    public void testGetHeadingByJquery()
    {
        assertEquals("By Jquery test page",driver.findElement(ByJquery.$("h1")).getText());
    }

    @Test
    public void testSimpleClassSelector()
    {
        List<WebElement> els = driver.findElements(ByJquery.$(".class1"));
        assertTrue(els.size() == 1);

        WebElement el = els.get(0);
        assertEquals("div", el.getTagName());
        assertEquals("class1", el.getAttribute("class"));
        assertEquals("Simple class test", el.getText());
    }

    @Test
    @IgnoreBrowser(value = {Browser.HTMLUNIT}, reason = "HtmlUnit does not support className magic")
    public void testClassNameReturnsClass()
    {
        List<WebElement> els = driver.findElements(ByJquery.$(".class1"));
        assertEquals(1, els.size());

        assertEquals("class1", els.get(0).getAttribute("className"));
    }

    @Test
    @RequireBrowser(Browser.HTMLUNIT)
    public void testClassNameFailsToReturnsClassInHtmlUnit()
    {
        List<WebElement> els = driver.findElements(ByJquery.$(".class1"));
        assertEquals(1, els.size());

        assertNull(els.get(0).getAttribute("className"));
    }

    @Test
    public void testSimpleIdSelector()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("#id1"));
        assertTrue(els.size() == 1);

        WebElement el = els.get(0);
        assertEquals("div", el.getTagName());
        assertEquals("id1", el.getAttribute("id"));
        assertEquals("Simple ID test", el.getText());
    }

    @Test
    public void testMultipleSelector()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("#id2 .innerblock"));
        assertTrue(els.size() == 1);

        WebElement el = els.get(0);
        assertEquals("div", el.getTagName());
        assertEquals("innerblock", el.getAttribute("class"));
        assertEquals("Inner block test", el.getText());
    }

    @Test
    public void testMultipleElementsByClassName()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("div.block2").children());
        assertTrue(els.size() == 2);


        for (WebElement el : els)
        {
            assertEquals("span", el.getTagName());
        }
    }

    @Test
    public void testContainsSelector()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("div:contains('contains1')"));
        assertTrue("Expected two elements to be found.", els.size() == 2);

        assertEquals("contains-test", els.get(0).getAttribute("class"));
        assertEquals("contains1", els.get(1).getText());
    }

    @Test
    public void testMultipleSelectorWithContains()
    {
        List<WebElement> els = driver.findElements(ByJquery.$(".contains-test div:contains('contains1')"));

        assertTrue("Only expected one element to be found", els.size() == 1);

        assertEquals("contains1", els.get(0).getText());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleSelectorElementNotFoundShouldThrowException()
    {
        driver.findElement(ByJquery.$("#NonExistant"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testMultipleSelectorElementNotFoundShouldThrowException()
    {
        driver.findElement(ByJquery.$("#id2 .nonExistant"));
    }

    @Test
    public void testClosestSelectorLocatesParent()
    {
        WebElement el = driver.findElement(ByJquery.$("li.item-a").closest("ul"));
        assertEquals("level-2", el.getAttribute("class"));
    }

    @Test
    public void testClosestSelectorLocatesSelf()
    {
        WebElement el = driver.findElement(ByJquery.$("li.item-a").closest("li"));
        assertEquals("item-a", el.getAttribute("class"));
    }

    @Test
    public void testFilterUsingASelector()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("#filter-test div").filter("':odd'"));

        assertEquals("Expected to find 3 elements", 3, els.size());

        assertEquals("First element should be 2", "2", els.get(0).getText());
        assertEquals("Second element should be 12", "12", els.get(1).getText());
        assertEquals("Third element should be 22", "22", els.get(2).getText());
    }

    @Test
    public void testFilterUsingAFunction()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("#filter-test div").filter("function() { return ATLWD.$(this).text() == '1' }"));

        assertEquals("Expected to find 1 element", 1, els.size());

        assertEquals("Element text should be 1", "1", els.get(0).getText());
    }

    @Test
    public void testFilterUsingFunctionWithIndex()
    {
        List<WebElement> els = driver.findElements(ByJquery.$("#filter-test div").filter("function(index) { return index % 3 == 2 }"));

        assertEquals("Expected to find 2 elements", 2, els.size());

        assertEquals("The first element text should be 11", "11", els.get(0).getText());
        assertEquals("The second element text should be 22", "22", els.get(1).getText());
    }
}
