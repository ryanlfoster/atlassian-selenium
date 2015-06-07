package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.TimedElement;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.junit.Test;
import org.openqa.selenium.By;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.equalToIgnoringCase;

@IgnoreBrowser(Browser.HTMLUNIT_NOJS)
public class TestTimedElement extends AbstractPageElementBrowserTest
{
    @Test
    public void testIsPresent()
    {
        product.visit(ElementsPage.class);

        // Positive - verify element that exists
        Poller.waitUntilTrue(product.find(By.id("test1_addElementsButton")).timed().isPresent());

        // Negative - verify element that does not exist
        Poller.waitUntilFalse(product.find(By.id("non_present_element")).timed().isPresent());

        // Delayed presence & Delayed positive - click on button that adds a span with delay, verify isPresent waits.
        product.find(By.id("test1_addElementsButton")).click();
        Poller.waitUntilTrue(product.find(By.id("test1_delayedSpan")).timed().isPresent());

        // Delayed Negative
    }

    @Test
    public void testIsVisible()
    {
        product.visit(ElementsPage.class);

        TimedElement testInput = product.find(By.id("test2_input")).timed();

        // Positive - verify input that is visible
        Poller.waitUntilTrue(testInput.isVisible());

        // Negative - click on button to make input invisible and verify
        product.find(By.id("test2_toggleInputVisibility")).click();
        Poller.waitUntilFalse(testInput.isVisible());

        // Delayed presence - click on a button that adds an element with delay, verify isVisible waits
        product.find(By.id("test2_addElementsButton")).click();
        Poller.waitUntilTrue(product.find(By.id("test2_delayedSpan")).timed().isVisible());

        // Delayed positive - click on button to make input visible with delay and verify
        product.find(By.id("test2_toggleInputVisibilityWithDelay")).click();
        Poller.waitUntilTrue(testInput.isVisible());

        // Delayed Negative
    }

    @Test
    public void testText()
    {
        product.visit(ElementsPage.class);

        // Positive - verify span with text
        Poller.waitUntilEquals("Span Value", product.find(By.id("test3_span")).timed().getText());

        // check non-case-sensitive
        Poller.waitUntil(product.find(By.id("test3_span")).timed().getText(), equalToIgnoringCase("span value"));

        // Negative - verify a span that has no text
        Poller.waitUntilEquals("", product.find(By.id("test3_spanEmpty")).timed().getText());

        // Delayed presence - click on button that adds a span with delay, verify getText waits
        product.find(By.id("test3_addElementsButton")).click();
        Poller.waitUntilEquals("Delayed Span", product.find(By.id("test3_delayedSpan")).timed().getText());

        // Delayed postive - click on button that sets the text of span with delay, verify getText waits
        product.find(By.id("test3_setTextButton")).click();
        Poller.waitUntilEquals("Delayed Text", product.find(By.id("test3_spanEmpty")).timed().getText());

        // Delayed negative
    }

    @Test
    public void testHasText()
    {
        product.visit(ElementsPage.class);

        // Positive - verify span with text
        Poller.waitUntilTrue(product.find(By.id("test3_span")).timed().hasText("Span Value"));

        // Negative - verify span with wrong text
        Poller.waitUntilFalse(product.find(By.id("test3_spanEmpty")).timed().hasText("foo"));

        // Delayed presence - click on button that adds a span with delay, verify getText waits
        product.find(By.id("test3_addElementsButton")).click();
        Poller.waitUntilTrue(product.find(By.id("test3_delayedSpan")).timed().hasText("Delayed Span"));
    }

    @Test
    public void testAttribute()
    {
        product.visit(ElementsPage.class);

        // Positive - verify class attribute of span
        Poller.waitUntilEquals("test5-input-class", product.find(By.id("test5_input")).timed().getAttribute("class"));

        // Negative

        // Delayed presence - click on button that adds a span with delay, verify getAtribute waits
        product.find(By.id("test5_addElementsButton")).click();
        Poller.waitUntilEquals("test5-span-delayed", product.find(By.id("test5_delayedSpan")).timed().getAttribute("class"));

        // Delayed positive - click on a button that adds attribute of a span, verify getAttribute waits
        product.find(By.id("test5_addAttribute")).click();
        Poller.waitUntilEquals("test5-input-value", product.find(By.id("test5_input")).timed().getAttribute("value"));

        // Delayed negative
    }

    @Test
    public void testTimedElementLocatesElementWithinAContext()
    {
        product.visit(ElementsPage.class);

        product.find(By.id("test6_hideSpanButton")).click();
        Poller.waitUntilFalse(product.find(By.id("test6_Div")).find(By.className("test6_class")).timed().isVisible());
    }

    @Test
    public void testGetTagName()
    {
        product.visit(ElementsPage.class);

        product.find(By.id("test1_addElementsButton")).click();
        Poller.waitUntilEquals("span", product.find(By.id("test1_delayedSpan")).timed().getTagName());
    }

    @Test
    public void testHasAttribute()
    {
        product.visit(ElementsPage.class);

        // Delayed presence
        product.find(By.id("test1_addElementsButton")).click();
        Poller.waitUntilTrue(product.find(By.id("test1_delayedSpan")).timed().hasAttribute("class", "testClass"));

        // incorrect attribute
        Poller.waitUntilFalse(product.find(By.id("test1_delayedSpan")).timed().hasAttribute("class", "foo"));

        // attribute not present
        Poller.waitUntilFalse(product.find(By.id("test1_delayedSpan")).timed().hasAttribute("nonexistant", "foo"));
    }

    @Test
    public void testTimedElementWithFindAll()
    {
        product.visit(ElementsPage.class);
        PageElement leafList = product.find(By.id("test4_leafList"));
        for (PageElement li : leafList.findAll(By.tagName("li")))
        {
            Poller.waitUntilTrue(li.timed().isPresent());
        }
    }

    @Test
    public void testElementWithTimeout()
    {
        product.visit(ElementsPage.class);
        PageElement element = product.find(By.id("test1_addElementsButton"));
        assertEquals(element.timed().isPresent().defaultTimeout(), product.timeouts().timeoutFor(TimeoutType.DEFAULT));
        element = element.withTimeout(TimeoutType.AJAX_ACTION);
        assertEquals(element.timed().isPresent().defaultTimeout(), product.timeouts().timeoutFor(TimeoutType.AJAX_ACTION));
    }

    @Test
    public void testGetValue()
    {
        product.visit(ElementsPage.class);

        final PageElement testedInput = product.find(By.id("test9_input"));
        final PageElement toggleValueButton = product.find(By.id("test9_toggleValue"));

        Poller.waitUntilEquals("test9_value", testedInput.timed().getValue());

        toggleValueButton.click();
        Poller.waitUntilEquals("test9_newvalue", testedInput.timed().getValue());

        // ... and switch again!
        toggleValueButton.click();
        Poller.waitUntilEquals("test9_value", testedInput.timed().getValue());
    }

    @Test
    public void testHasValue()
    {
        product.visit(ElementsPage.class);

        final PageElement testedInput = product.find(By.id("test9_input"));
        final PageElement toggleValueButton = product.find(By.id("test9_toggleValue"));

        Poller.waitUntilTrue(testedInput.timed().hasValue("test9_value"));

        toggleValueButton.click();
        Poller.waitUntilTrue(testedInput.timed().hasValue("test9_newvalue"));

        // ... and switch again!
        toggleValueButton.click();
        Poller.waitUntilTrue(testedInput.timed().hasValue("test9_value"));
    }
}
