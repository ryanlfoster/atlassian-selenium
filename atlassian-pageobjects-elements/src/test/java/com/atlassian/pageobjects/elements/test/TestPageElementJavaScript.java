package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.elements.GlobalElementFinder;
import com.atlassian.pageobjects.elements.Options;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.SelectElement;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link com.atlassian.pageobjects.elements.PageElementJavascript} execute script methods.
 *
 * @since 2.1
 */
@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestPageElementJavaScript extends AbstractPageElementBrowserTest
{

    private PageElementFinder elementFinder;

    @Before
    public void init()
    {
        product.visit(ElementsPage.class);
        elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
    }

    @Test
    public void shouldExecuteSimpleJavascript()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        final Object delayedDivPresent = delayedDiv.javascript().execute("return $(arguments[0]).length > 0");
        assertNotNull(delayedDivPresent);
        assertTrue(delayedDivPresent instanceof Boolean);
        assertTrue((Boolean) delayedDivPresent);
    }

    @Test
    public void shouldExecuteTimedScript()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        TimedQuery<Boolean> delayedSpanPresent = delayedDiv.javascript().executeTimed(Boolean.class,
                "return $(arguments[0]).find('#test1_delayedSpan').length > 0");
        assertFalse(delayedSpanPresent.now());
        elementFinder.find(By.id("test1_addElementsButton")).click();
        Poller.waitUntilTrue(delayedSpanPresent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeTimedShouldNotAcceptPageElementAsResultType()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        delayedDiv.javascript().executeTimed(PageElement.class, "return arguments[0]");
    }

    @Test
    public void shouldReturnSamePageElement()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        Object result = delayedDiv.javascript().execute("return arguments[0]");
        assertSame(delayedDiv, result);
    }

    @Test
    public void shouldReturnSamePageElementWithCast()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        PageElement result = delayedDiv.javascript().execute(PageElement.class, "return arguments[0]");
        assertSame(delayedDiv, result);
    }

    @Test
    public void shouldReturnNewPageElement()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        Object result = delayedDiv.javascript().execute("return arguments[1]", elementFinder.find(By.id("test1_addElementsButton")));
        assertTrue(result instanceof PageElement);
        final PageElement element = (PageElement) result;
        assertEquals("test1_addElementsButton", element.getAttribute("id"));
    }

    @Test
    public void shouldReturnSelectElement()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        SelectElement awesomeSelect = delayedDiv.javascript().execute(SelectElement.class, "return arguments[1]", elementFinder.find(By.id("awesome-select")));
        awesomeSelect.select(Options.value("volvo"));
    }

    @Test
    public void executeAsyncNonTypedShouldWorkForSimpleType()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        Object result = delayedDiv.javascript().executeAsync("arguments[1](true)");
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    @IgnoreBrowser(value = Browser.HTMLUNIT, reason = "http://code.google.com/p/selenium/issues/detail?id=4257")
    public void executeAsyncNonTypedShouldWorkForPageElement()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        Object result = delayedDiv.javascript().executeAsync("arguments[1](arguments[0])");
        assertThat(result, Matchers.instanceOf(PageElement.class));
        assertSame(delayedDiv, result);
    }

    @Test
    public void executeAsyncTypedShouldWorkForSimpleType()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        String result = delayedDiv.javascript().executeAsync(String.class, "arguments[1]('woohoo')");
        assertEquals("woohoo", result);
    }

    @Test
    @IgnoreBrowser(value = Browser.HTMLUNIT, reason = "http://code.google.com/p/selenium/issues/detail?id=4257")
    public void executeAsyncTypedShouldWorkForPageElement()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        PageElement result = delayedDiv.javascript().executeAsync(PageElement.class, "arguments[1](arguments[0])");
        assertSame(delayedDiv, result);
    }

    @Test
    @IgnoreBrowser(value = Browser.HTMLUNIT, reason = "http://code.google.com/p/selenium/issues/detail?id=4257")
    public void executeAsyncTypedShouldWorkForSelectElement()
    {
        final PageElement delayedDiv = elementFinder.find(By.id("test1_delayedDiv"));
        SelectElement result = delayedDiv.javascript().executeAsync(SelectElement.class, "arguments[2](arguments[1])",
                elementFinder.find(By.id("awesome-select")));
        assertEquals("awesome-select", result.getAttribute("id"));
        result.select(Options.value("volvo"));
    }
}
