package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.elements.Options;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.SelectElement;
import com.atlassian.pageobjects.elements.WebDriverElement;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.test.pageobjects.page.DynamicPage;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.atlassian.webdriver.utils.by.ByJquery;
import org.junit.Test;
import org.openqa.selenium.By;

import javax.inject.Inject;

import static com.atlassian.pageobjects.elements.query.Poller.waitUntilEquals;
import static com.atlassian.pageobjects.elements.query.Poller.waitUntilFalse;
import static com.atlassian.pageobjects.elements.query.Poller.waitUntilTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestElement extends AbstractPageElementBrowserTest
{
    @Inject
    private PageElementFinder elementFinder;

    @Test
    public void testFieldInjection()
    {
        ElementsPage elementsPage = product.visit(ElementsPage.class);

        // click on a button that was injected via @ElementBy
        elementsPage.test1_addElementsButton().click();

        // verify delayed element that was injected via @ElementBy waits
        Poller.waitUntilTrue(elementsPage.test1_delayedSpan().timed().isPresent());
    }

    @Test
    public void testIsPresent()
    {
        product.visit(ElementsPage.class);

        // Positive - verify element that exists
        assertTrue(product.find(By.id("test1_addElementsButton")).isPresent());

        // Negative - verify element that does not exist
        assertFalse(product.find(By.id("test1_non_existant")).isPresent());

        // Delayed presence - click on button that adds a span with delay, verify isPresent does not wait.
        product.find(By.id("test1_addElementsButton")).click();
        assertFalse(product.find(By.id("test1_delayedSpan")).isPresent());
    }

    @Test
    public void testIsPresentWithParent()
    {
        product.visit(ElementsPage.class);

        final PageElement parentContainer = product.find(By.id("parent-container"));
        assertTrue(parentContainer.isPresent());
        assertTrue(parentContainer.find(By.className("child-button")).isPresent());
    }


    @Test
    public void testIsPresentWhenParentIsNotPresent()
    {
        product.visit(ElementsPage.class);

        final PageElement notExistingParent = product.find(By.id("not-existing-parent"));
        assertFalse(notExistingParent.isPresent());
        assertFalse(notExistingParent.find(By.className("child-button")).isPresent());
    }

    @Test
    public void testIsVisible()
    {
        product.visit(ElementsPage.class);

        PageElement testInput = product.find(By.id("test2_input"));

        // Positive - verify input that is visible
        assertTrue(testInput.isVisible());

        // Delayed presence - click on a button that adds an element with delay, verify isVisible waits
        product.find(By.id("test2_addElementsButton")).click();
        assertTrue(product.find(By.id("test2_delayedSpan")).isVisible());

        // Delayed positive - click on button to make input visible with delay and verify that it did not wait
        product.find(By.id("test2_toggleInputVisibility")).click();
        product.find(By.id("test2_toggleInputVisibilityWithDelay")).click();
        assertFalse(testInput.isVisible());
    }

    @Test
    public void testGetText()
    {
        product.visit(ElementsPage.class);

        // Positive - verify span with text
        assertEquals("Span Value", product.find(By.id("test3_span")).getText());

        // Delayed presence - click on button that adds a span with delay, verify getText waits
        product.find(By.id("test3_addElementsButton")).click();
        assertEquals("Delayed Span", product.find(By.id("test3_delayedSpan")).getText());

        // Delayed positive - click on button that sets the text of span with delay, verify getText does not wait
        product.find(By.id("test3_setTextButton")).click();
        assertEquals("", product.find(By.id("test3_spanEmpty")).getText());
    }

    @Test
    public void testFind()
    {
        product.visit(ElementsPage.class);

        // find a delayed elements within another
        PageElement childList = product.find(By.id("test4_parentList")).find(By.tagName("ul"));
        assertEquals("test4_childList", childList.getAttribute("id"));

        PageElement leafList = childList.find(By.tagName("ul"));
        assertEquals("test4_leafList", leafList.getAttribute("id"));

        PageElement listItem = leafList.find(By.tagName("li"));
        assertEquals("Item 1", listItem.getText());

        //wait for presence on an element within another
        product.find(By.id("test4_addElementsButton")).click();
        waitUntilTrue(leafList.find(By.linkText("Item 4")).timed().isPresent());

        //wait for text on an element within another
        product.visit(ElementsPage.class);

        product.find(By.id("test4_addElementsButton")).click();
        assertEquals("Item 5", product.find(By.className("listitem-active")).getText());
    }

    @Test
    public void testGetTagName()
    {
        ElementsPage elementsPage = product.visit(ElementsPage.class);

        assertEquals("input", elementsPage.test1_addElementsButton().getTagName());
    }

    @Test
    public void testHasAttribute()
    {
        ElementsPage elementsPage = product.visit(ElementsPage.class);

        // positive
        assertTrue(elementsPage.test1_addElementsButton().hasAttribute("type", "button"));

        // incorrect attribute
        assertFalse(elementsPage.test1_addElementsButton().hasAttribute("type", "bar"));

        // attribute not present
        assertFalse(elementsPage.test1_addElementsButton().hasAttribute("nonexistant", "foo"));
    }

    @Test
    public void shouldFindElementByJquery()
    {
        product.visit(ElementsPage.class);
        final PageElement awesomeDiv = product.find(By.id("awesome-div"));
        final PageElement awesomeSpan = awesomeDiv.find(ByJquery.$("span:contains(Awesome)"));
        assertNotNull(awesomeSpan);
        assertEquals("awesome-span", awesomeSpan.getAttribute("id"));

        final SelectElement awesomeSelect = elementFinder.find(By.id("awesome-select"), SelectElement.class);
        awesomeSelect.find(ByJquery.$("option:contains(Volvo)")).select();
        assertEquals(Options.value("volvo"), awesomeSelect.getSelected());
    }

    @Test
    public void shouldRebindElementIfStale_whenLocatedByAnnotation()
    {
        DynamicPage page = product.visit(DynamicPage.class);

        assertEquals("Hello Tester!", page.createFieldSet().helloWorld("Tester").getMessage());
        assertEquals("Hello Developer!", page.createFieldSet().helloWorld("Developer").getMessage());

    }

    @Test
    public void shouldRebindElementIfStale_whenOriginalElementBecomesStaleAfterSomeTime()
    {
        // This strategy can be used by pabeobjects that reload the same page after an action

        DynamicPage page = product.visit(DynamicPage.class);
        page.createFieldSet();

        PageElementFinder elementFinder = page.getElementFinder();
        PageElement button = elementFinder.find(By.id("helloWorldButton"), TimeoutType.AJAX_ACTION);

        product.getTester().getDriver().executeScript("$('#helloWorldButton').addClass('posting')");
        page.removeAndCreateFieldSetSlowly();
        waitUntilFalse(button.timed().hasClass("posting"));
    }


    @Test
    public void shouldRebindElementIfStale_whenLocatedByElementFinder()
    {
        DynamicPage page = product.visit(DynamicPage.class);

        PageElementFinder elementFinder = page.getElementFinder();
        PageElement username = elementFinder.find(By.id("nameTextBox"));
        PageElement button = elementFinder.find(By.id("helloWorldButton"));
        PageElement message = elementFinder.find(By.id("messageSpan"));

        page.createFieldSet();

        username.type("Tester");
        button.click();
        assertEquals("Hello Tester!", message.getText());

        // recreate the fields
        page.createFieldSet();
        username.type("Developer");
        button.click();
        assertEquals("Hello Developer!", message.getText());
    }

    @Test
    public void shouldRebindElementsIfStale_whenLocatedByParentFindSingle()
    {
        DynamicPage page = product.visit(DynamicPage.class);
        PageElementFinder elementFinder = page.getElementFinder();

        PageElement div = elementFinder.find(By.id("placeHolderDiv"));
        PageElement username = div.find(By.tagName("fieldset")).find(By.id("nameTextBox"));
        PageElement button = div.find(By.tagName("fieldset")).find(By.id("helloWorldButton"));
        PageElement message = div.find(By.tagName("fieldset")).find(By.id("messageSpan"));

        page.createFieldSet();
        username.type("Tester");
        button.click();
        assertEquals("Hello Tester!", message.getText());

        //recreate the fields
        page.createFieldSet();
        username.type("Developer");
        button.click();
        assertEquals("Hello Developer!", message.getText());
    }

    @Test
    public void elementShouldBeConvertedToWebElement()
    {
        DynamicPage page = product.visit(DynamicPage.class);
        PageElementFinder elementFinder = page.getElementFinder();

        PageElement div = elementFinder.find(By.id("placeHolderDiv"));
        WebDriverElement username = (WebDriverElement) div.find(By.tagName("fieldset")).find(By.id("nameTextBox"));
        PageElement button = div.find(By.tagName("fieldset")).find(By.id("helloWorldButton"));
        PageElement message = div.find(By.tagName("fieldset")).find(By.id("messageSpan"));

        page.createFieldSet();
        username.asWebElement().sendKeys("Tester");
        button.click();
        assertEquals("Hello Tester!", message.getText());

        //recreate the fields
        page.createFieldSet();
        username.asWebElement().sendKeys("Developer");
        button.click();
        assertEquals("Hello Developer!", message.getText());
    }

    @Test
    public void shouldRebindElementsIfStale_whenLocatingByParentFindAll()
    {
        DynamicPage page = product.visit(DynamicPage.class);
        PageElementFinder elementFinder = page.getElementFinder();

        page.createFieldSet();

        PageElement fieldset = elementFinder.find(By.tagName("fieldset"));
        PageElement username = fieldset.findAll(By.tagName("input")).get(0);
        PageElement button = fieldset.findAll(By.tagName("input")).get(1);
        PageElement message = fieldset.find(By.id("messageSpan"));

        username.type("Tester");
        button.click();
        assertEquals("Hello Tester!", message.getText());

        //recreate the fields
        page.createFieldSet();
        username.type("Developer");
        button.click();
        assertEquals("Hello Developer!", message.getText());
    }

    @Test
    public void shouldRebindElementsIfStale_whenLocatingByParentFindAllGivenParentAlwaysExists()
    {
        DynamicPage page = product.visit(DynamicPage.class);
        PageElementFinder elementFinder = page.getElementFinder();

        page.createFieldSet();

        PageElement mainDiv = elementFinder.find(By.id("placeHolderDiv"));
        // needs timeout more than 3 seconds
        PageElement username = mainDiv.findAll(By.tagName("input")).get(0).withTimeout(TimeoutType.AJAX_ACTION);
        PageElement button = mainDiv.findAll(By.tagName("input")).get(1).withTimeout(TimeoutType.AJAX_ACTION);
        PageElement message = mainDiv.find(By.id("messageSpan")).withTimeout(TimeoutType.AJAX_ACTION);

        username.type("Tester");
        button.click();
        assertEquals("Hello Tester!", message.getText());

        //recreate the fields
        page.createFieldSetSlowly();
        username.type("Developer");
        button.click();
        waitUntilEquals("Hello Developer!", message.timed().getText());
    }

    @Test
    public void testExecuteScriptOnAnElement()
    {
        product.visit(ElementsPage.class);
        PageElement button = elementFinder.find(By.id(("test11_button")));

        assertFalse(button.hasClass("test"));

        button.javascript().execute("$(arguments[0]).addClass('test')");
        Poller.waitUntilTrue(button.timed().hasClass("test"));

        assertEquals(Boolean.TRUE, button.javascript().execute("return $(arguments[0]).hasClass('test')"));
    }
}
