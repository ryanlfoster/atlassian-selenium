package com.atlassian.selenium;

import com.atlassian.selenium.pageobjects.PageElement;
import com.thoughtworks.selenium.Selenium;
import junit.framework.Assert;
import org.apache.log4j.Logger;

import java.util.Arrays;


public class SeleniumChecks {

    private static final Logger log = Logger.getLogger(SeleniumAssertions.class);

    private final Selenium client;
    private final long conditionCheckInterval;
    private final long defaultMaxWait;

    public SeleniumChecks(Selenium client, SeleniumConfiguration config)
    {
        this.client = client;
        this.conditionCheckInterval = config.getConditionCheckInterval();
        this.defaultMaxWait = config.getActionWait();
    }

    public boolean visibleByTimeout(String locator)
    {
        return byTimeout(Conditions.isVisible(locator));
    }

    public boolean visibleByTimeout(PageElement element)
    {
        return visibleByTimeout(element.getLocator());
    }

    /**
     * This will wait until an element is visible.  If it doesnt become visible in
     * maxMillis return false
     *
     * @param locator   the selenium element locator
     * @param maxMillis how long to wait as most in milliseconds
     */
    public boolean visibleByTimeout(String locator, long maxMillis)
    {
        return byTimeout(Conditions.isVisible(locator), maxMillis);
    }

    public boolean visibleByTimeout(PageElement element, long maxMillis)
    {
        return visibleByTimeout(element.getLocator(), maxMillis);
    }

    public boolean notVisibleByTimeout(String locator)
    {
        return byTimeout(Conditions.isNotVisible(locator));
    }

    public boolean notVisibleByTimeout(PageElement element)
    {
        return notVisibleByTimeout(element.getLocator());
    }

    public boolean notVisibleByTimeout(String locator, long maxMillis)
    {
        return byTimeout(Conditions.isNotVisible(locator), maxMillis);
    }

    public boolean notVisibleByTimeout(PageElement element, long maxMillis)
    {
        return notVisibleByTimeout(element.getLocator(), maxMillis);
    }

    public boolean elementPresentByTimeout(String locator)
    {
        return byTimeout(Conditions.isPresent(locator));
    }

    public boolean elementPresentByTimeout(PageElement element)
    {
        return elementPresentByTimeout(element.getLocator());
    }

    public boolean elementPresentByTimeout(String locator, long maxMillis)
    {
        return byTimeout(Conditions.isPresent(locator), maxMillis);
    }

    public boolean elementPresentByTimeout(PageElement element, long maxMillis)
    {
        return elementPresentByTimeout(element.getLocator(), maxMillis);
    }

    public boolean elementPresentUntilTimeout(String locator)
    {
        return untilTimeout(Conditions.isPresent(locator));
    }

    public boolean elementPresentUntilTimeout(PageElement element)
    {
        return elementPresentUntilTimeout(element.getLocator());
    }

    public  boolean elementPresentUntilTimeout(String locator, long maxMillis)
    {
        return untilTimeout(Conditions.isPresent(locator), maxMillis);
    }

    public  boolean elementPresentUntilTimeout(PageElement element, long maxMillis)
    {
        return elementPresentUntilTimeout(element.getLocator(), maxMillis);
    }

    public  boolean elementNotPresentByTimeout(String locator)
    {
        return byTimeout(Conditions.isNotPresent(locator));
    }

    public  boolean elementNotPresentByTimeout(PageElement element)
    {
        return elementNotPresentByTimeout(element.getLocator());
    }

    public  boolean elementNotPresentUntilTimeout(String locator)
    {
        return untilTimeout(Conditions.isNotPresent(locator));
    }

    public  boolean elementNotPresentUntilTimeout(PageElement element)
    {
        return elementNotPresentUntilTimeout(element.getLocator());
    }

    public  boolean elementNotPresentUntilTimeout(String locator, long maxMillis)
    {
        return untilTimeout(Conditions.isNotPresent(locator), maxMillis);
    }

    public  boolean elementNotPresentUntilTimeout(PageElement element, long maxMillis)
    {
        return elementNotPresentUntilTimeout(element.getLocator(), maxMillis);
    }

    public  boolean textPresentByTimeout(String text, long maxMillis)
    {
        return byTimeout(Conditions.isTextPresent(text), maxMillis);
    }

    public  boolean textPresentByTimeout(String text)
    {
        return byTimeout(Conditions.isTextPresent(text));
    }

    public  boolean textNotPresentByTimeout(String text, long maxMillis)
    {
        return byTimeout(Conditions.isTextNotPresent(text), maxMillis);
    }

    public  boolean textNotPresentByTimeout(String text)
    {
        return byTimeout(Conditions.isTextNotPresent(text));
    }

    /**
     * This will wait until an element is not present.  If it doesnt become not present in
     * maxMillis
     *
     * @param locator   the selenium element locator
     * @param maxMillis how long to wait as most in milliseconds
     */
    public  boolean elementNotPresentByTimeout(String locator, long maxMillis)
    {
        return byTimeout(Conditions.isNotPresent(locator), maxMillis);
    }

    public  boolean elementNotPresentByTimeout(PageElement element, long maxMillis)
    {
        return elementNotPresentByTimeout(element.getLocator(), maxMillis);
    }

    public  boolean byTimeout(Condition condition)
    {
        return byTimeout(condition, defaultMaxWait);
    }

    public  boolean byTimeout(Condition condition, long maxWaitTime)
    {
        long startTime = System.currentTimeMillis();
        while (true)
        {
            if (System.currentTimeMillis() - startTime >= maxWaitTime)
            {
                return condition.executeTest(client);
            }

            if (condition.executeTest(client))
                return true;

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

    public boolean untilTimeout(Condition condition)
    {
        return untilTimeout(condition, defaultMaxWait);
    }

    public boolean untilTimeout(Condition condition, long maxWaitTime)
    {
        long startTime = System.currentTimeMillis();
        while (true)
        {
            if (System.currentTimeMillis() - startTime >= maxWaitTime)
            {
                return true;
            }

            if (!condition.executeTest(client))
            {
                return false;
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
    public boolean textPresent(String text)
    {
        return client.isTextPresent(text);
    }

    /**
     * @param text Asserts that text is not present in the current page
     */
    public boolean textNotPresent(String text)
    {
        return !client.isTextPresent(text);
    }

    /**
     * Asserts that a given element has a specified value
     * @param locator Locator for element using the standard selenium locator syntax
     * @param value The value the element is expected to contain
     */
    public boolean formElementEquals(String locator, String value)
    {
        return value.equals(client.getValue(locator));
    }

    /**
     * Asserts that a given element is present
     * @param locator Locator for the element that should be present given using the standard selenium locator syntax
     */
    public boolean elementPresent(String locator)
    {
        return client.isElementPresent(locator);
    }

    public boolean elementPresent(PageElement element)
    {
        return elementPresent(element.getLocator());
    }


    /**
     * Asserts that a given element is not present on the current page
     * @param locator Locator for the element that should not be present given using the standard selenium locator syntax
     */
    public boolean elementNotPresent(String locator)
    {
        return client.isElementPresent(locator);
    }

    public boolean elementNotPresent(PageElement element)
    {
        return elementNotPresent(element.getLocator());
    }

    /**
     * Asserts that a given element is present and is visible. Under some browsers just calling the seleinium.isVisible method
     * on an element that doesn't exist causes selenium to throw an exception.
     * @param locator Locator for the element that should be visible specified in the standard selenium syntax
     */
    public boolean elementVisible(String locator)
    {
        return (client.isElementPresent(locator) && client.isVisible(locator));
    }

    public boolean elementVisible(PageElement element)
    {
        return elementVisible(element.getLocator());
    }
    /**
     * Asserts that a given element is not present and visible. Calling selenium's native selenium.isVisible method on
     * an element that doesn't exist causes selenium to throw an exception
     * @param locator Locator for the element that should not be visible specified in the standard selenium syntax
     */
    public boolean elementNotVisible(String locator)
    {
        return (client.isElementPresent(locator) && client.isVisible(locator));
    }

    public boolean elementNotVisible(PageElement element)
    {
        return elementNotVisible(element.getLocator());
    }
    /**
     * Asserts that a given element is visible and also contains the given text.
     * @param locator Locator for the element that should be visible specified in the standard selenium syntax
     * @param text the text that the element should contain
     */
    public boolean elementVisibleContainsText(String locator, String text)
    {
        return (elementVisible(locator) && elementContainsText(locator, text));
    }

    public boolean elementVisibleContainsText(PageElement element, String text)
    {
        return elementVisibleContainsText(element.getLocator(), text);
    }

    /**
     * Asserts that a particular piece of HTML is present in the HTML source. It is recommended that the elementPresent, elementHasText or some other method
     * be used because browsers idiosyncratically add white space to the HTML source
     * @param html Lower case representation of HTML string that should not be present
     */
    public boolean htmlPresent(String html)
    {
        return  (client.getHtmlSource().toLowerCase().indexOf(html) >= 0);
    }

    /**
     * Asserts that a particular piece of HTML is not present in the HTML source. It is recommended that the elementNotPresent, elementDoesntHaveText or
     * some other method be used because browsers idiosyncratically add white space to the HTML source
     * @param html Lower case representation of HTML string that should not be present
     */
    public boolean htmlNotPresent(String html)
    {
        return !(client.getHtmlSource().toLowerCase().indexOf(html) >= 0);
    }

    /**
     * Asserts that the element specified by the locator contains the specified text
     * @param locator Locator given in standard selenium syntax
     * @param text The text that the element designated by the locator should contain
     */
    public boolean elementHasText(String locator, String text)
    {
        return (client.getText(locator).indexOf(text) >= 0);
    }

    public boolean elementHasText(PageElement element, String text)
    {
        return elementHasText(element.getLocator(), text);
    }

    /**
     * Asserts that the element specified by the locator does not contain the specified text
     * @param locator Locator given in standard selenium syntax
     * @param text The text that the element designated by the locator should not contain
     */
    public boolean elementDoesntHaveText(String locator, String text)
    {
        return !elementHasText(locator, text);
    }

    public boolean elementDoesntHaveText(PageElement element, String text)
    {
        return elementDoesntHaveText(element.getLocator(), text);
    }

    /**
     * Asserts that the element given by the locator has an attribute which contains the required value.
     * @param locator Locator given in standard selenium syntax
     * @param attribute The element attribute
     * @param value The value expected to be found in the element's attribute
     */
    public boolean attributeContainsValue(String locator, String attribute, String value)
    {
        String attributeValue = client.getAttribute(locator + "@" + attribute);
        return (attributeValue.indexOf(value) >= 0);
    }

    public boolean attributeContainsValue(PageElement element, String attribute, String value)
    {
        return attributeContainsValue(element.getLocator(), attribute, value);
    }

    /**
     * Asserts that the element given by the locator has an attribute which does not contain the given value.
     * @param locator Locator given in standard selenium syntax
     * @param attribute The element attribute
     * @param value The value expected to be found in the element's attribute
     */
    public boolean attributeDoesntContainValue(String locator, String attribute, String value)
    {
        String attributeValue = client.getAttribute(locator + "@" + attribute);
        return (attributeValue.indexOf(value) >= 0);
    }

    public boolean attributeDoesntContainValue(PageElement element, String attribute, String value)
    {
        return attributeDoesntContainValue(element.getLocator(), attribute, value);
    }

    /**
     * Asserts that a link containing the given text appears on the page
     * @param text The text that a link on the page should contain
     * @see #linkVisibleWithText(String) also
     */
    public boolean linkPresentWithText(String text)
    {
        return client.isElementPresent("link=" + text);
    }

    /**
     * Asserts that no link exists on the page containing the given text
     * @param text The text that no link on the page should contain
     */
    public boolean linkNotPresentWithText(String text)
    {
        return !client.isElementPresent("link=" + text);
    }

    /**
     * Asserts that a link containin the given text is present and visible.
     * @param text The text that a link on the page should contain
     */
    public boolean linkVisibleWithText(String text)
    {
        return linkPresentWithText(text) && client.isVisible("link=" + text);
    }

    /**
     * Asserts that two elements (located by selenium syntax) are vertically within deltaPixels of each other.
     * @param locator1 Locator for element 1 given in standard selenium syntax
     * @param locator2 Locator for element 2 given in standard selenium syntax
     * @param deltaPixels The maximum allowable distance between the two element
     */
    public boolean elementsVerticallyAligned(String locator1, String locator2, int deltaPixels)
    {
        int middle1 = client.getElementPositionTop(locator1).intValue() + (client.getElementHeight(locator1).intValue() / 2);
        int middle2 = client.getElementPositionTop(locator2).intValue() + (client.getElementHeight(locator2).intValue() / 2);
        String message = "Vertical position of element '" + locator1 + "' (" + middle1 + ") was not within " + deltaPixels +
            " pixels of the vertical position of element '" + locator2 + "' (" + middle2 + ")";
        return plusMinusEqual(middle1, middle2, deltaPixels);
    }

    public boolean elementsVerticallyAligned(PageElement element1, PageElement element2, int deltaPixels)
    {
        return elementsVerticallyAligned(element1.getLocator(), element2.getLocator(), deltaPixels);
    }

    public boolean elementsSameHeight(final String locator1, final String locator2, final int deltaPixels)
    {
        int height1 = client.getElementHeight(locator1).intValue();
        int height2 = client.getElementHeight(locator2).intValue();
        String message = "Height of element '" + locator1 + "' (" + height1 + ") was not within " + deltaPixels +
            " pixels of the height of element '" + locator2 + "' (" + height2 + ")";
        return plusMinusEqual(height1, height2, deltaPixels);
    }

    public boolean elementsSameHeight(PageElement element1, PageElement element2, final int deltaPixels)
    {
        return elementsSameHeight(element1.getLocator(), element2.getLocator(), deltaPixels);
    }

    private boolean plusMinusEqual(int x, int y, int delta)
    {
        return (Math.abs(x - y) <= delta);
    }

    /**
     * Asserts that an element contains the given text.
     */
    public boolean elementContainsText(String locator, String text)
    {
        String elementText = client.getText(locator);
        return (elementText.indexOf(text) >= 0);
    }

    public boolean elementContainsText(PageElement element, String text)
    {
        return elementContainsText(element.getLocator(), text);
    }


    /**
     * Asserts that an element does not contain the given text.
     */
    public boolean elementDoesNotContainText(String locator, String text)
    {
        return !(client.getText(locator).indexOf(text) >= 0);
    }

    public boolean elementDoesNotContainText(PageElement element, String text)
    {
        return elementDoesNotContainText(element.getLocator(), text);
    }

    public boolean windowClosed(String windowName)
    {
        return !windowOpen(windowName);
    }

    public boolean windowOpen(String windowName)
    {
        return Arrays.asList(client.getAllWindowNames()).contains(windowName);
    }

}
