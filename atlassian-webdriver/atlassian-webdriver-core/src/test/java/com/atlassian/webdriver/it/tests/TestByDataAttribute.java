package com.atlassian.webdriver.it.tests;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.ByDataAttributePage;
import com.atlassian.webdriver.testing.annotation.IgnoreBrowser;
import com.atlassian.webdriver.utils.by.ByDataAttribute;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.atlassian.webdriver.utils.element.WebElementMatchers.containsAtLeast;
import static com.atlassian.webdriver.utils.element.WebElementMatchers.tagNameEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test {@link com.atlassian.webdriver.utils.by.ByDataAttribute}.
 *
 * @since 2.1
 */
public class TestByDataAttribute extends AbstractSimpleServerTest
{

    ByDataAttributePage byDataAttributePage;
    WebDriver driver;

    @Before
    public void init()
    {
        byDataAttributePage = product.visit(ByDataAttributePage.class);
        driver = product.getTester().getDriver();
    }


    @Test
    @IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS}, reason = "SELENIUM-166 HtmlUnit has partial support for data- attributes")
    public void shouldFindAllByAttributeName()
    {
        final List<WebElement> elements = driver.findElements(ByDataAttribute.byData("type"));
        assertEquals(6, elements.size());
        assertThat(elements, containsAtLeast(tagNameEqual("li"), 4));
        assertThat(elements, containsAtLeast(tagNameEqual("ul"), 1));
        assertThat(elements, containsAtLeast(tagNameEqual("div"), 1));

        final List<WebElement> listElements = driver.findElements(ByDataAttribute.byData("ordering"));
        assertEquals(4, listElements.size());
        assertThat(listElements, containsAtLeast(tagNameEqual("li"), 4));
    }

    @Test
    public void shouldFindByAttributeNameAndValue()
    {
        final WebElement firstListItem = driver.findElement(ByDataAttribute.byData("ordering", "1"));
        assertIsListElementWithText(firstListItem, "a");
    }

    @Test
    @IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS}, reason = "SELENIUM-166 HtmlUnit has partial support for data- attributes")
    public void shouldFindByTagNameAndData()
    {
        final List<WebElement> listElements = driver.findElements(ByDataAttribute.byTagAndData("li", "type"));
        assertEquals(4, listElements.size());
        assertThat(listElements, containsAtLeast(tagNameEqual("li"), 4));

        final WebElement mainDiv = driver.findElement(ByDataAttribute.byTagAndData("div", "type"));
        assertEquals("div", mainDiv.getTagName());
        assertEquals("main container", mainDiv.getAttribute("data-type"));
    }

    @Test
    public void shouldFindByTagNameAndDataAndValue()
    {
        final WebElement lastListItem = driver.findElement(ByDataAttribute.byTagAndData("li", "ordering", "4"));
        assertIsListElementWithText(lastListItem, "d");
    }

    @Test
    @IgnoreBrowser(value = {Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS}, reason = "SELENIUM-166 HtmlUnit has partial support for data- attributes")
    public void shouldFindByWeirdDataAttributeValues()
    {
        final WebElement weirdOne = driver.findElement(ByDataAttribute.byTagAndData("li", "a-attribute"));
        assertIsListElementWithText(weirdOne, "a");

        final WebElement weirdOneAgain = driver.findElement(ByDataAttribute.byTagAndData("li", "a-attribute", "==A is awesome=="));
        assertIsListElementWithText(weirdOneAgain, "a");

        final WebElement weirdTwo = driver.findElement(ByDataAttribute.byTagAndData("li", "b-attribute"));
        assertIsListElementWithText(weirdTwo, "b");

        final WebElement weirdTwoAgain = driver.findElement(ByDataAttribute.byTagAndData("li", "b-attribute", "==B is cool=="));
        assertIsListElementWithText(weirdTwoAgain, "b");
    }

    private void assertIsListElementWithText(WebElement firstListItem, String text)
    {
        assertEquals(firstListItem.getTagName(), "li");
        assertEquals(firstListItem.getText(), text);
    }
}
