package com.atlassian.selenium;

import com.atlassian.performance.EventTime;
import com.atlassian.performance.TimeRecorder;
import com.thoughtworks.selenium.Selenium;
import com.atlassian.selenium.pageobjects.PageElement;
import junit.framework.Assert;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * This provides helper methods for selenium assertions.  This
 * mostly means waiting for events to occur (i.e. a dropdown to
 * appear after a certain timeout, etc)
 */
public class SeleniumAssertions
{
    private static final Logger log = Logger.getLogger(SeleniumAssertions.class);
    private final Selenium client;
    private final long conditionCheckInterval;
    private final long defaultMaxWait;
    private final TimeRecorder recorder;

    public SeleniumAssertions(Selenium client, SeleniumConfiguration config, TimeRecorder recorder)
    {
        this.client = client;
        this.conditionCheckInterval = config.getConditionCheckInterval();
        this.defaultMaxWait = config.getActionWait();
        this.recorder = recorder;
    }
    
    
    /**
     * Temporarily bring back the old constructor so that some older codes compile.
     */
    public SeleniumAssertions(Selenium client, SeleniumConfiguration config)
    {
        this.client = client;
        this.conditionCheckInterval = config.getConditionCheckInterval();
        this.defaultMaxWait = config.getActionWait();
        this.recorder = new TimeRecorder("Unnamed Test");
    }    

    private String defIfNull(String def, String alt)
    {
        if(alt == null)
        {
            return def;
        }
        else
        {
            return alt;
        }
    }


    public ByTimeoutConfiguration generateByTimeoutConfig(Condition condition, String locator, long maxWait)
    {
        return new ByTimeoutConfiguration(condition, locator, true, maxWait, conditionCheckInterval, null);
    }


    public ByTimeoutConfiguration generateByTimeoutConfig(Condition condition, PageElement elem, long maxWait)
    {
        return new ByTimeoutConfiguration(condition, elem, maxWait, conditionCheckInterval, null);
    }

    public void visibleByTimeout(String locator)
    {
       visibleByTimeout(locator, defaultMaxWait);
    }

    public void visibleByTimeout(PageElement element)
    {
        visibleByTimeout(element, defaultMaxWait);
    }

    /**
     * This will wait until an element is visible.  If it doesnt become visible in
     * maxMillis fail.
     *
     * @param locator   the selenium element locator
     * @param maxMillis how long to wait as most in milliseconds
     */
    public void visibleByTimeout(String locator, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isVisible(locator), locator, maxMillis));
    }

    public void visibleByTimeout(PageElement element, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isVisible(element.getLocator()), element, maxMillis));
    }

    public void notVisibleByTimeout(String locator)
    {
        byTimeout(Conditions.isNotVisible(locator));
    }

    public void notVisibleByTimeout(PageElement element)
    {
        notVisibleByTimeout(element, defaultMaxWait);
    }

    public void notVisibleByTimeout(String locator, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isNotVisible(locator), locator, maxMillis));
    }

    public void notVisibleByTimeout(PageElement element, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isNotVisible(element.getLocator()), element, maxMillis));
    }

    public void elementPresentByTimeout(String locator)
    {
        elementPresentByTimeout(locator, defaultMaxWait);
    }

    public void elementPresentByTimeout(PageElement element)
    {
        elementPresentByTimeout(element, defaultMaxWait);
    }

    public void elementPresentByTimeout(String locator, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isPresent(locator), locator, maxMillis));
    }

    public void elementPresentByTimeout(PageElement element, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isPresent(element.getLocator()), element, maxMillis));
    }


    public void elementPresentUntilTimeout(String locator)
    {
        untilTimeout(Conditions.isPresent(locator));
    }

    public void elementPresentUntilTimeout(PageElement element)
    {
        elementPresentUntilTimeout(element.getLocator());        
    }

    public void elementPresentUntilTimeout(String locator, long maxMillis)
    {
        untilTimeout(Conditions.isPresent(locator), maxMillis);
    }

    public void elementPresentUntilTimeout(PageElement element, long maxMillis)
    {
        elementPresentUntilTimeout(element.getLocator(), maxMillis);
    }
    
    public void elementNotPresentByTimeout(String locator)
    {
        elementNotPresentByTimeout(locator, defaultMaxWait);
    }

    public void elementNotPresentByTimeout(PageElement element)
    {
        elementNotPresentByTimeout(element, defaultMaxWait);
    }

    public void elementNotPresentUntilTimeout(String locator)
    {
        untilTimeout(Conditions.isNotPresent(locator));
    }

    public void elementNotPresentUntilTimeout(PageElement element)
    {
        elementNotPresentUntilTimeout(element.getLocator());
    }

    public void elementNotPresentUntilTimeout(String locator, long maxMillis)
    {
        untilTimeout(Conditions.isNotPresent(locator), maxMillis);
    }

    public void elementNotPresentUntilTimeout(PageElement element, long maxMillis)
    {
        elementNotPresentUntilTimeout(element.getLocator(), maxMillis);
    }
    
    public void textPresentByTimeout(String text, long maxMillis)
    {
        byTimeout(Conditions.isTextPresent(text), maxMillis);
    }

    public void textPresentByTimeout(String text)
    {
        byTimeout(Conditions.isTextPresent(text));
    }

    public void textNotPresentByTimeout(String text, long maxMillis)
    {
        byTimeout(Conditions.isTextNotPresent(text), maxMillis);
    }

    public void textNotPresentByTimeout(String text)
    {
        byTimeout(Conditions.isTextNotPresent(text));
    }

    /**
     * This will wait until an element is not present.  If it doesnt become not present in
     * maxMillis
     *
     * @param locator   the selenium element locator
     * @param maxMillis how long to wait as most in milliseconds
     */
    public void elementNotPresentByTimeout(String locator, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isNotPresent(locator), locator, maxMillis));
    }

    public void elementNotPresentByTimeout(PageElement element, long maxMillis)
    {
        byTimeout(generateByTimeoutConfig(Conditions.isNotPresent(element.getLocator()), element, maxMillis));
    }

    public void byTimeout(Condition condition)
    {
        byTimeout(condition, defaultMaxWait);
    }

    public void byTimeout(Condition condition, long maxWaitTime)
    {
        byTimeout(new ByTimeoutConfiguration(condition, null, true, maxWaitTime, conditionCheckInterval, null));
    }


    public void byTimeout(ByTimeoutConfiguration config)
    {
        EventTime et = EventTime.timeEvent(config.getKey(), config.getAutoGeneratedKey(), new TimedWaitFor(config, client));
        recorder.record(et);
        if(et.getTimedOut())
        {
            log.error("Page source:\n" + client.getHtmlSource());

            throw new AssertionError(prepareAssertionText(config.getAssertMessage(),
                                                          "Waited " + config.getMaxWaitTime() + " ms for [" + config.getCondition().errorMessage() + "], but it never became true."));
        }
    }

    private String prepareAssertionText(String userTxt, String systemTxt)
    {
        if(userTxt != null)
        {
            return userTxt + " \n" + systemTxt;
        }
        else
        {
            return systemTxt;
        }
    }

    public void untilTimeout(Condition condition)
    {
        untilTimeout(condition, defaultMaxWait);
    }

    public void untilTimeout(Condition condition, long maxWaitTime)
    {
        long startTime = System.currentTimeMillis();
        while (true)
        {
            if (System.currentTimeMillis() - startTime >= maxWaitTime)
            {
                break;
            }

            if (!condition.executeTest(client))
            {
                log.error("Page source:\n" + client.getHtmlSource());
                throw new AssertionError("Condition [" + condition.errorMessage() + "], became before timeout.");
            }

            try
            {
                Thread.sleep(conditionCheckInterval);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException("Thread was interupted", e);
            }
        }
    }

    /**
     * @param text Asserts that text is present in the current page
     */
    public void textPresent(String text)
    {
        Assert.assertTrue("Expected text not found in response: '" + text + "'", client.isTextPresent(text));
    }

    /**
     * @param text Asserts that text is not present in the current page
     */
    public void textNotPresent(String text)
    {
        Assert.assertFalse("Un-expected text found in response: '" + text + "'", client.isTextPresent(text));
    }

    /**
     * Asserts that a given element has a specified value
     * @param locator Locator for element using the standard selenium locator syntax
     * @param value The value the element is expected to contain
     */
    public void formElementEquals(String locator, String value)
    {
        Assert.assertEquals("Element with id '" + locator + "' did not have the expected value '" + value + "'", value, client.getValue(locator));
    }

    /**
     * Asserts that a given element is present
     * @param locator Locator for the element that should be present given using the standard selenium locator syntax
     */
    public void elementPresent(String locator)
    {
        Assert.assertTrue("Expected element not found in response: '" + locator + "'", client.isElementPresent(locator));
    }

    public void elementPresent(PageElement element)
    {
        elementPresent(element.getLocator());
    }


    /**
     * Asserts that a given element is not present on the current page
     * @param locator Locator for the element that should not be present given using the standard selenium locator syntax
     */
    public void elementNotPresent(String locator)
    {
        Assert.assertFalse("Un-expected element found in response: '" + locator + "'", client.isElementPresent(locator));
    }

    public void elementNotPresent(PageElement element)
    {
        elementNotPresent(element.getLocator());
    }

    /**
     * Asserts that a given element is present and is visible. Under some browsers just calling the seleinium.isVisible method
     * on an element that doesn't exist causes selenium to throw an exception.
     * @param locator Locator for the element that should be visible specified in the standard selenium syntax
     */
    public void elementVisible(String locator)
    {
        Assert.assertTrue("Expected element not visible in response: '" + locator + "'", client.isElementPresent(locator) && client.isVisible(locator));
    }


    public void elementVisible(PageElement element)
    {
        elementVisible(element.getLocator());
    }
    
    /**
     * Asserts that a given element is not present and visible. Calling selenium's native selenium.isVisible method on
     * an element that doesn't exist causes selenium to throw an exception
     * @param locator Locator for the element that should not be visible specified in the standard selenium syntax
     */
    public void elementNotVisible(String locator)
    {
        Assert.assertFalse("Un-expected element visible in response: '" + locator + "'", client.isElementPresent(locator) && client.isVisible(locator));
    }


    public void elementNotVisible(PageElement element)
    {
        elementNotVisible(element.getLocator());
    }
    /**
     * Asserts that a given element is visible and also contains the given text.
     * @param locator Locator for the element that should be visible specified in the standard selenium syntax
     * @param text the text that the element should contain
     */
    public void elementVisibleContainsText(String locator, String text)
    {
        elementVisible(locator);
        elementContainsText(locator, text);
    }

    public void elementVisibleContainsText(PageElement element, String text)
    {
        elementVisibleContainsText(element.getLocator(), text);
    }

    /**
     * Asserts that a particular piece of HTML is present in the HTML source. It is recommended that the elementPresent, elementHasText or some other method
     * be used because browsers idiosyncratically add white space to the HTML source
     * @param html Lower case representation of HTML string that should not be present
     */
    public void htmlPresent(String html)
    {
        Assert.assertTrue("Expected HTML not found in response: '" + html + "'", client.getHtmlSource().toLowerCase().indexOf(html) >= 0);
    }

    /**
     * Asserts that a particular piece of HTML is not present in the HTML source. It is recommended that the elementNotPresent, elementDoesntHaveText or
     * some other method be used because browsers idiosyncratically add white space to the HTML source
     * @param html Lower case representation of HTML string that should not be present
     */
    public void htmlNotPresent(String html)
    {
        Assert.assertFalse("Unexpected HTML found in response: '" + html + "'", client.getHtmlSource().toLowerCase().indexOf(html) >= 0);
    }

    /**
     * Asserts that the element specified by the locator contains the specified text
     * @param locator Locator given in standard selenium syntax
     * @param text The text that the element designated by the locator should contain
     */
    public void elementHasText(String locator, String text)
    {
        Assert.assertTrue("Element(s) with locator '" + locator +"' did not contain text '"+ text + "'", (client.getText(locator).indexOf(text) >= 0));
    }

    public void elementHasText(PageElement element, String text)
    {
        elementHasText(element.getLocator(), text);
    }

    /**
     * Asserts that the element specified by the locator does not contain the specified text
     * @param locator Locator given in standard selenium syntax
     * @param text The text that the element designated by the locator should not contain
     */
    public void elementDoesntHaveText(String locator, String text)
    {
        Assert.assertFalse("Element(s) with locator '" + locator +"' did contained text '"+ text + "'", (client.getText(locator).indexOf(text) >= 0));
    }

    public void elementDoesntHaveText(PageElement element, String text)
    {
        elementDoesntHaveText(element.getLocator(), text);
    }

    /**
     * Asserts that the element given by the locator has an attribute which contains the required value.
     * @param locator Locator given in standard selenium syntax
     * @param attribute The element attribute
     * @param value The value expected to be found in the element's attribute
     */
    public void attributeContainsValue(String locator, String attribute, String value)
    {
        String attributeValue = client.getAttribute(locator + "@" + attribute);
        Assert.assertTrue("Element with locator '" + locator + "' did not contain value '" + value + "' in attribute '" + attribute + "=" + attributeValue + "'", (attributeValue.indexOf(value) >= 0));
    }

    public void attributeContainsValue(PageElement element, String attribute, String value)
    {
        attributeContainsValue(element.getLocator(), attribute, value);
    }

    /**
     * Asserts that the element given by the locator has an attribute which does not contain the given value.
     * @param locator Locator given in standard selenium syntax
     * @param attribute The element attribute
     * @param value The value expected to be found in the element's attribute
     */
    public void attributeDoesntContainValue(String locator, String attribute, String value)
    {
        String attributeValue = client.getAttribute(locator + "@" + attribute);
        Assert.assertFalse("Element with locator '" + locator + "' did not contain value '" + value + "' in attribute '" + attribute + "'", (attributeValue.indexOf(value) >= 0));
    }

    public void attributeDoesntContainValue(PageElement element, String attribute, String value)
    {
        attributeDoesntContainValue(element.getLocator(), attribute, value);
    }

    /**
     * Asserts that a link containing the given text appears on the page
     * @param text The text that a link on the page should contain
     * @see #linkVisibleWithText(String) also
     */
    public void linkPresentWithText(String text)
    {
        Assert.assertTrue("Expected link with text not found in response: '" + text + "'", client.isElementPresent("link=" + text));
    }

    /**
     * Asserts that no link exists on the page containing the given text
     * @param text The text that no link on the page should contain
     */
    public void linkNotPresentWithText(String text)
    {
        Assert.assertFalse("Unexpected link with text found in response: '" + text + "'", client.isElementPresent("link=" + text));
    }

    /**
     * Asserts that a link containin the given text is present and visible.
     * @param text The text that a link on the page should contain
     */
    public void linkVisibleWithText(String text)
    {
        linkPresentWithText(text);
        Assert.assertTrue("Expected link with text not visible: '" + text + "'", client.isVisible("link=" + text));
    }

    /**
     * Asserts that two elements (located by selenium syntax) are vertically within deltaPixels of each other.
     * @param locator1 Locator for element 1 given in standard selenium syntax
     * @param locator2 Locator for element 2 given in standard selenium syntax
     * @param deltaPixels The maximum allowable distance between the two element
     */
    public void elementsVerticallyAligned(String locator1, String locator2, int deltaPixels)
    {
        int middle1 = client.getElementPositionTop(locator1).intValue() + (client.getElementHeight(locator1).intValue() / 2);
        int middle2 = client.getElementPositionTop(locator2).intValue() + (client.getElementHeight(locator2).intValue() / 2);
        String message = "Vertical position of element '" + locator1 + "' (" + middle1 + ") was not within " + deltaPixels +
            " pixels of the vertical position of element '" + locator2 + "' (" + middle2 + ")";
        Assert.assertTrue(message, Math.abs(middle1 - middle2) <= deltaPixels);
    }

    public void elementsVerticallyAligned(PageElement element1, PageElement element2, int deltaPixels)
    {
        elementsVerticallyAligned(element1.getLocator(), element2.getLocator(), deltaPixels);
    }

    public void elementsSameHeight(final String locator1, final String locator2, final int deltaPixels)
    {
        int height1 = client.getElementHeight(locator1).intValue();
        int height2 = client.getElementHeight(locator2).intValue();
        String message = "Height of element '" + locator1 + "' (" + height1 + ") was not within " + deltaPixels +
            " pixels of the height of element '" + locator2 + "' (" + height2 + ")";
        Assert.assertTrue(message, Math.abs(height1 - height2) <= deltaPixels);
    }

    public void elementsSameHeight(PageElement element1, PageElement element2, final int deltaPixels)
    {
        elementsSameHeight(element1.getLocator(), element2.getLocator(), deltaPixels);
    }

    /**
     * Asserts that an element contains the given text.
     */
    public void elementContainsText(String locator, String text)
    {
        String elementText = client.getText(locator);
        Assert.assertTrue("Element(s) with locator '" + locator +"' did not contain text '"+ text + "', but contained '" + elementText + "'",
            elementText.indexOf(text) >= 0);
    }

    public void elementContainsText(PageElement element, String text)
    {
        elementContainsText(element.getLocator(), text);        
    }


    /**
     * Asserts that an element does not contain the given text.
     */
    public void elementDoesNotContainText(String locator, String text)
    {
        Assert.assertFalse("Element(s) with locator '" + locator +"' did contained text '"+ text + "'",
            client.getText(locator).indexOf(text) >= 0);
    }

    public void elementDoesNotContainText(PageElement element, String text)
    {
        elementDoesNotContainText(element.getLocator(), text);        
    }

    public void windowClosed(String windowName)
    {
        Assert.assertFalse(Arrays.asList(client.getAllWindowNames()).contains(windowName));
    }

    public void windowOpen(String windowName)
    {
        Assert.assertTrue(Arrays.asList(client.getAllWindowNames()).contains(windowName));
    }
}
