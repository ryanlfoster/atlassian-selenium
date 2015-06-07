package com.atlassian.selenium;

import com.atlassian.webtest.ui.keys.KeyEventType;
import com.thoughtworks.selenium.Selenium;
import com.atlassian.selenium.visualcomparison.VisualComparableClient;

import java.util.Collection;


/**
 * Extends the {@link Selenium} client to provide a more sensible implementation
 * as well some extra utility methods such as keypress.
 */
public interface SeleniumClient extends Selenium, VisualComparableClient
{

    /**
     * Unlike {@link DefaultSelenium#open}, this opens the provided URL relative to the application context path.
     * It also waits for the page to load -- a maximum of PAGE_LOAD_WAIT before returning.
     */
    public void open(String url);

    /**
     * Wait for page to load doesn't work the case of non-HTML based resources (like images).
     * So sometimes you really do want to open a url without waiting.
     * @param url
     */
    public void openNoWait(String url);

    /**
     * Opens the given URL and waits a maximum of timeoutMillis for the page to load completely.
     */
    public void open(String url, long timeoutMillis);

    /**
     * Overloads {@link #waitForPageToLoad(String)} to take in a long.
     */
    public void waitForPageToLoad(long timeoutMillis);

    /**
     * Waits for the page to load with the default timeout configured in {@link SeleniumConfiguration}.
     */
    public void waitForPageToLoad();

    /**
     * Executes the given Javascript in the context of the text page and waits for it to evaluate to true
     * for a maximum of {@link SeleniumConfiguration.getActionWait} milliseconds.
     * @see #waitForCondition(String, long) if you would like to specify your own timeout.
     */
    public void waitForCondition(String javascript);

    /**
     * Executes the given Javascript in the context of the text page and waits for it to evaluate to true
     * for a maximum of timeoutMillis.
     */
    public void waitForCondition(String javascript, long timeoutMillis);

    /**
     * Waits for the page to finish loading ajax calls, and returns if there are no more ajax calls currently running.
     * The method will check for a maximum of {@link #ACTION_WAIT} milliseconds
     * @see #waitForAjaxWithJquery(long) if you would like to specify your own timeout.
     */
    public void waitForAjaxWithJquery();

    /**
     * Waits for the page to finish loading ajax calls, and returns if there are no more ajax calls currently running.
     * The method will check for a maximum of timeoutMillis
     */
    public void waitForAjaxWithJquery(long timeoutMillis);

    /**
     * Click the element with the given locator and optionally wait for the page to load, using {@link #PAGE_LOAD_WAIT}.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     * @param waitForPageToLoad whether to wait for the page to reload. Don't use this unless the page is completely
     * reloaded.
     * @see #click(String, long) if you would like to specify your own timeout.
     */
    public void click(String locator, boolean waitForPageToLoad);

    /**
     * Submit the named form locator and optionally wait for the page to load, using {@link #PAGE_LOAD_WAIT}.
     *
     * @param form to click, specified using Selenium selector syntax
     * @param waitForPageToLoad whether to wait for the page to reload. Don't use this unless the page is completely
     * reloaded.
     * @see #submit(String, long) if you would like to specify your own timeout.
     */
    public void submit(String form, boolean waitForPageToLoad);

    /**
     * Click the element with the given locator and wait for the page to load, for a maximum of timeoutMillis.
     * <p/>
     * Do not use this method if the page does not reload.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     * @param timeoutMillis the maximum number of milliseconds to wait for the page to load. Polling takes place
     * more frequently.
     * @see #click(String, boolean) if you would like to use the default timeout
     */
    public void click(String locator, long timeoutMillis);

    /**
     * Click the element with the given locator and wait for the ajax call to finish.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     */
    public void clickAndWaitForAjaxWithJquery(String locator);

    /**
     * Click the element with the given locator and wait for the ajax call to finish.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     * @param timeoutMillis the maximum number of milliseconds to wait for the ajax calls to finish.
     * @see #clickAndWaitForAjaxWithJquery(String) if you would like to use the default timeout
     */
    public void clickAndWaitForAjaxWithJquery(String locator, long timeoutMillis);

    /**
     * Submit the given form and wait for the page to load, for a maximum of timeoutMillis.
     * <p/>
     * Do not use this method if the page does not reload.
     *
     * @param form the form to submit
     * @param timeoutMillis the maximum number of milliseconds to wait for the page to load. Polling takes place
     * more frequently.
     * @see #click(String, boolean) if you would like to use the default timeout
     */
    public void submit(String form, long timeoutMillis);

    /**
     * This will type into a field by sending key down / key press / key up events.
     * @param locator Uses the Selenium locator syntax
     * @param key The key to be pressed
     */
    public void keyPress(String locator, String key);

    /**
     * This will type into a field by first blanking it out and then sending key down / key press / key up
     * events.
     *
     * @param locator the Selenium locator
     * @param string  the string to type
     * @param reset   Should the field be reset first?
     */
    public void typeWithFullKeyEvents(String locator, String string, boolean reset);

    /**
     * This will type into a field by first blanking it out and then sending key down / key press / key up
     * events. This really only calls {@link #typeWithFullKeyEvents(String,String,boolean)})}
     *
     * @param locator - the usual Selenium locator
     * @param string  the string to type into a field
     */
    public void typeWithFullKeyEvents(String locator, String string);

    /**
     * This will send all the events for a particular character
     * NOTE: This function will only handle vanilla ascii characters
     * more exotic characters will be ignored (ie. NO events will be fired).
     *     *
     * @param locator - the usual Selenium locator
     * @param character - the character to send
     * @param eventsToFire - a collection of the types of events to Fire
     */
    public void simulateKeyPressForCharacter(final String locator, final Character character, Collection<KeyEventType> eventsToFire);

    /**
     * This will send all the events for a particular character
     * NOTE: This function will only handle vanilla ascii characters
     * more exotic characters will be ignored (ie. NO events will be fired).
     * This will fire ALL events for the Character
     *     *
     * @param locator - the usual Selenium locator
     * @param character - the character to send
     */
    public void simulateKeyPressForCharacter(String locator, Character character);

    /**
     * This will send all the events for a particular special key (for example F1 or the down arrow key)
     * The keyCode matches the codes in java.awt.event.KeyEvent
     *  NOTE: the return key actually fires events with a keyCode of 13 NOT VK_ENTER
     *     *
     * @param locator - the usual Selenium locator
     * @param keyCode - the code for the special Key can use java.awt.event.KeyEvent.XXX to find these
     * @param eventsToFire - a collection of the types of events to Fire
     */
    public void simulateKeyPressForSpecialKey(final String locator, final int keyCode, Collection<KeyEventType> eventsToFire);

    /**
     * This is a low level function to generate one keyboard event. It would be preferable to use the simulatKeyPress... functions
     * above.
     * @param locator - the locator of the element to send the keyevent to
     * @param eventType - The type of key event
     * @param keyCode - the keyCode for the keyEvent
     * @param characterCode - the character code for the key event
     * @param shiftKey - Is the shift key down?
     * @param altKey - Is the alt/option key down?
     * @param controlKey - Is the control key down?
     * @param metaKey - Is the meta (aka the apple or windows) key down?
     */
    public void generateKeyEvent(final String locator, final KeyEventType eventType, final int keyCode, final int characterCode,
                                 final boolean shiftKey, final boolean altKey, final boolean controlKey, final boolean metaKey);

    /**
     * This will send all the events for a particular special key (for example F1 or the down arrow key)
     * The keyCode matches the codes in java.awt.event.KeyEvent
     *  NOTE: the return key actually fires events with a keyCode of 13 NOT VK_ENTER
     * This will fire ALL events for the special key.
     *     *
     * @param locator - the usual Selenium locator
     * @param keyCode - the code for the special Key can use java.awt.event.KeyEvent.XXX to find these
     */
    public void simulateKeyPressForSpecialKey(String locator, int keyCode);

    /**
     * This sets if subsiquent calls to the keyEvent (@keyDown, @keyPress, @keyUp) fuctions
     * will set the code in the keyCode or not.
     * This is applicable to Firefox and webkit.
     * @param toKeyCode - to set or not to set the keyCode in the event
     */
    public void toggleToKeyCode(boolean toKeyCode);

    /**
     * This sets if subsiquent calls to the keyEvent (@keyDown, @keyPress, @keyUp) fuctions
     * will set the code in the characterCode or not.
     * This is applicable to Firefox and webkit.
     * @param toCharacterCode - to set or not to set the characterCode in the event
     */
    public void toggleToCharacterCode(boolean toCharacterCode);

    /**
     * This will select an option from a {@code select} field.
     *
     * @param selectName the select field name
     * @param label the label to select
     */
    public void selectOption(String selectName, String label);

    /**
     * This will select an option from a {@code select} field. If the field calls executes an ajax call onchange of
     * the value, this method will wait for that ajax method to finish.
     *
     * @param selectName the select field name
     * @param label the label to select
     */
    public void selectOptionAndWaitForAjaxWithJquery(String selectName, String label);

    /**
     * Checks a checkbox given a name and value.
     */
    public void check(String name, String value);

    public void clickLinkWithText(String text, boolean waitForPageToLoad);

    public void clickButton(String buttonText, boolean waitForPageToLoad);

    public void clickButtonAndWaitForAjaxWithJquery(String buttonText);

    public void clickButtonWithName(String buttonName, boolean waitForPageToLoad);

    public void clickButtonWithNameAndWaitForAjaxWithJquery(String buttonName);

    public void clickElementWithTitle(String title);

    public void clickElementWithTitleAndWaitForAjaxWithJquery(String title);

    public void clickElementWithClass(String className);

    public void clickElementWithClassAndWaitForAjaxWithJquery(String className);

    public void clickElementWithCss(String cssSelector);

    public void clickElementWithCssAndWaitForAjaxWithJquery(String cssSelector);

    public void clickElementWithXpath(String xpath);

    public void clickElementWithXpathAndWaitForAjaxWithJquery(String xpath);

    public void typeInElementWithName(String elementName, String text);

    public void typeInElementWithCss(String cssSelector, String text);

    public boolean hasJquery();

    public void start();

    public Browser getBrowser();

    public void seleniumKeyPress(String locator, String key);

}
