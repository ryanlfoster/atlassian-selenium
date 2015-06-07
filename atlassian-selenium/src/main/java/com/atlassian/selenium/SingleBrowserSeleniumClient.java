

package com.atlassian.selenium;

import com.atlassian.selenium.keyboard.KeyEvent;
import com.atlassian.selenium.keyboard.KeyEventSequence;
import com.atlassian.selenium.visualcomparison.ScreenElement;
import com.atlassian.selenium.visualcomparison.utils.ScreenResolution;
import com.atlassian.webtest.ui.keys.KeyEventType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.selenium.DefaultSelenium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Extends the {@link DefaultSelenium} client to provide a more sensible implementation
 * as well some extra utility methods such as keypress.
 */
public class SingleBrowserSeleniumClient extends DefaultSelenium implements SeleniumClient
{

    private Browser browser;

    private Map<String, KeyEventSequence> characterKeySequenceMap = new HashMap<String, KeyEventSequence>();

    private static final Set<KeyEventType> ALL_EVENTS = EnumSet.allOf(KeyEventType.class);

    /**
     * The maximum page load wait time used by Selenium. This value is set with
     * {@link SeleniumConfiguration#getPageLoadWait()}.
     */
    protected final long PAGE_LOAD_WAIT;

    /**
     * The maximum wait time for actions that don't require page loads. This value is set with
     * {@link SeleniumConfiguration#getActionWait()}.
     */
    protected final long ACTION_WAIT;

    public SingleBrowserSeleniumClient(SeleniumConfiguration config)
    {
        super(new HtmlDumpingHttpCommandProcessor(config.getServerLocation(), config.getServerPort(), config.getBrowserStartString(), config.getBaseUrl()));

        this.PAGE_LOAD_WAIT = config.getPageLoadWait();
        this.ACTION_WAIT = config.getActionWait();

        browser = Browser.typeOf(config.getBrowserStartString());
    }

    public Browser getBrowser()
    {
        return browser;
    }

    public void seleniumKeyPress(String locator, String key) {
        super.keyPress(locator, key);
    }

    /**
     * Unlike {@link DefaultSelenium#open}, this opens the provided URL relative to the application context path.
     * It also waits for the page to load -- a maximum of {@link #PAGE_LOAD_WAIT} before returning.
     */
    public void open(String url)
    {
        open(url, PAGE_LOAD_WAIT);
    }

    /**
     * Wait for page to load doesn't work the case of non-HTML based resources (like images).
     * So sometimes you really do want to open a url without waiting.
     * @param url
     */
    public void openNoWait(String url)
    {
        super.open(url);
    }

    /**
     * Opens the given URL and waits a maximum of timeoutMillis for the page to load completely.
     */
    public void open(String url, long timeoutMillis)
    {
        super.open(url);
        super.waitForPageToLoad(String.valueOf(timeoutMillis));
    }

    /**
     * Overloads {@link #waitForPageToLoad(String)} to take in a long.
     */
    public void waitForPageToLoad(long timeoutMillis)
    {
        super.waitForPageToLoad(String.valueOf(timeoutMillis));
    }

    /**
     * Waits for the page to load with the default timeout configured in {@link SeleniumConfiguration}.
     */
    public void waitForPageToLoad()
    {
        waitForPageToLoad(PAGE_LOAD_WAIT);
    }

    /**
     * Executes the given Javascript in the context of the text page and waits for it to evaluate to true
     * for a maximum of {@link #ACTION_WAIT} milliseconds.
     * @see #waitForCondition(String, long) if you would like to specify your own timeout.
     */
    public void waitForCondition(String javascript)
    {
        waitForCondition(javascript, ACTION_WAIT);
    }

    /**
     * Executes the given Javascript in the context of the text page and waits for it to evaluate to true
     * for a maximum of timeoutMillis.
     */
    public void waitForCondition(String javascript, long timeoutMillis)
    {
        waitForCondition(javascript, Long.toString(timeoutMillis));
    }

    /**
     * Waits for the page to finish loading ajax calls, and returns if there are no more ajax calls currently running.
     * The method will check for a maximum of {@link #ACTION_WAIT} milliseconds
     * @see #waitForAjaxWithJquery(long) if you would like to specify your own timeout.
     */
    public void waitForAjaxWithJquery()
    {
        waitForAjaxWithJquery(ACTION_WAIT);
    }

    /**
     * Waits for the page to finish loading ajax calls, and returns if there are no more ajax calls currently running.
     * The method will check for a maximum of timeoutMillis
     */
    public void waitForAjaxWithJquery(long timeoutMillis)
    {
        if (!hasJquery())
        {
            throw new UnsupportedOperationException("This operation requires jQuery.");
        }
        waitForCondition("selenium.browserbot.getCurrentWindow().jQuery.active == 0;", Long.toString(timeoutMillis));
    }

    /**
     * Click the element with the given locator and optionally wait for the page to load, using {@link #PAGE_LOAD_WAIT}.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     * @param waitForPageToLoad whether to wait for the page to reload. Don't use this unless the page is completely
     * reloaded.
     * @see #click(String, long) if you would like to specify your own timeout.
     */
    public void click(String locator, boolean waitForPageToLoad)
    {
        super.click(locator);
        if (waitForPageToLoad)
            super.waitForPageToLoad(String.valueOf(PAGE_LOAD_WAIT));
    }

    /**
     * Submit the named form locator and optionally wait for the page to load, using {@link #PAGE_LOAD_WAIT}.
     *
     * @param form to click, specified using Selenium selector syntax
     * @param waitForPageToLoad whether to wait for the page to reload. Don't use this unless the page is completely
     * reloaded.
     * @see #submit(String, long) if you would like to specify your own timeout.
     */
    public void submit(String form, boolean waitForPageToLoad)
    {
        super.submit(form);
        if (waitForPageToLoad)
            super.waitForPageToLoad(String.valueOf(PAGE_LOAD_WAIT));
    }

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
    public void click(String locator, long timeoutMillis)
    {
        super.click(locator);
        super.waitForPageToLoad(Long.toString(timeoutMillis));
    }

    /**
     * Click the element with the given locator and wait for the ajax call to finish.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     */
    public void clickAndWaitForAjaxWithJquery(String locator)
    {
        super.click(locator);
        waitForAjaxWithJquery();
    }

    /**
     * Click the element with the given locator and wait for the ajax call to finish.
     *
     * @param locator the element to click, specified using Selenium selector syntax
     * @param timeoutMillis the maximum number of milliseconds to wait for the ajax calls to finish.
     * @see #clickAndWaitForAjaxWithJquery(String) if you would like to use the default timeout
     */
    public void clickAndWaitForAjaxWithJquery(String locator, long timeoutMillis)
    {
        super.click(locator);
        waitForAjaxWithJquery(timeoutMillis);
    }

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
    public void submit(String form, long timeoutMillis)
    {
        super.submit(form);
        super.waitForPageToLoad(Long.toString(timeoutMillis));
    }

    /**
     * This will type into a field by sending key down / key press / key up events.
     * @param locator Uses the Selenium locator syntax
     * @param key The key to be pressed
     */
    public void keyPress(String locator, String key)
    {
        super.keyDown(locator, key);
        super.keyPress(locator, key);
        super.keyUp(locator, key);
    }

    /**
     * This will type into a field by first blanking it out and then sending key down / key press / key up
     * events.
     *
     * @param locator the Selenium locator
     * @param string  the string to type
     * @param reset   Should the field be reset first?
     */
    public void typeWithFullKeyEvents(String locator, String string, boolean reset)
    {
        new SeleniumKeyHandler(this, locator, ALL_EVENTS, reset).typeWithFullKeyEvents(string);
    }

    /**
     * This will type into a field by first blanking it out and then sending key down / key press / key up
     * events. This really only calls {@link #typeWithFullKeyEvents(String,String,boolean)})}
     *
     * @param locator - the usual Selenium locator
     * @param string  the string to type into a field
     */
    public void typeWithFullKeyEvents(String locator, String string)
    {
        typeWithFullKeyEvents(locator, string, true);
    }

    public void simulateKeyPressForCharacter(final String locator, final Character character)
    {
        simulateKeyPressForCharacter(locator,character, ALL_EVENTS);
    }

    public void simulateKeyPressForCharacter(final String locator, final Character character, Collection<KeyEventType> eventsToFire)
    {
        simulateKeyPressForIdentifier(locator,character.toString(), eventsToFire);
    }

    public void simulateKeyPressForSpecialKey(final String locator, final int keyCode)
    {
        simulateKeyPressForSpecialKey(locator,keyCode, ALL_EVENTS);
    }

    public void simulateKeyPressForSpecialKey(final String locator, final int keyCode, Collection<KeyEventType> eventsToFire)
    {
        simulateKeyPressForIdentifier(locator,String.format("0x%x",keyCode), eventsToFire);
    }

    public void generateKeyEvent(final String locator, final KeyEventType eventType, final int keyCode, final int characterCode,
                                 final boolean shiftKey, final boolean altKey, final boolean controlKey, final boolean metaKey)
    {
        getEval("selenium.generateKeyEvent(\""+locator+"\",'"
                +eventType.getEventString()+"',"
                +Integer.toString(keyCode)+","
                +Integer.toString(characterCode)+","
                +Boolean.TRUE.toString()+","
                +Boolean.toString(shiftKey)+","
                +Boolean.toString(altKey)+","
                +Boolean.toString(controlKey)+","
                +Boolean.toString(metaKey)+")");
    }

    private void simulateKeyPressForIdentifier(final String locator, final String identifier, Collection<KeyEventType> eventsToFire)
    {
        if (characterKeySequenceMap.containsKey(identifier))
        {
            for (KeyEvent ke : characterKeySequenceMap.get(identifier).getKeyEvents())
            {
                if (!ke.getBrowsers().contains(this.browser))
                {
                    continue;
                }
                if (!eventsToFire.contains(ke.getEventType()))
                {
                    continue;
                }
                int keyCode = 0;
                int characterCode = 0;
                if (ke.isToCharacterCode())
                {
                    characterCode = ke.getCode();
                }
                if (ke.isToKeyCode())
                {
                   keyCode = ke.getCode();
                }
                generateKeyEvent(locator,ke.getEventType(),keyCode,characterCode, ke.isCtrlKeyDown(),ke.isAltKeyDown(),
                        ke.isShiftKeyDown(),ke.isMetaKey());
            }
        }
    }

    private void toggleShiftKey(boolean keyIsPressed)
    {
        if (keyIsPressed)
        {
            super.shiftKeyDown();
        }
        else
        {
            super.shiftKeyUp();
        }
    }

    private void toggleAltKey(boolean keyIsPressed)
    {
        if (keyIsPressed)
        {
            super.altKeyDown();
        }
        else
        {
            super.altKeyUp();
        }
    }

    private void toggleControlKey(boolean keyIsPressed)
    {
        if (keyIsPressed)
        {
            super.controlKeyDown();
        }
        else
        {
            super.controlKeyUp();
        }
    }

    private void toggleMetaKey(boolean keyIsPressed)
    {
        if (keyIsPressed)
        {
            super.metaKeyDown();
        }
        else
        {
            super.metaKeyUp();
        }

    }

    public void toggleToKeyCode(boolean toKeyCode)
    {
        super.getEval("this.browserbot.toKeyCode = "+toKeyCode+";");
    }

    public void toggleToCharacterCode(boolean toCharacterCode)
    {
        super.getEval("this.browserbot.toCharacterCode = "+toCharacterCode+";");        
    }

    /**
     * This will select an option from a {@code select} field.
     *
     * @param selectName the select field name
     * @param label the label to select
     */
    public void selectOption(String selectName, String label)
    {
        // In some browsers (i.e. Safari) the select items have funny padding
        // so we need to use this funny method to find how the select item is
        // padded so that it can be matched
        String[] options = super.getSelectOptions(selectName);
        int i = 0;
        for(; i < options.length; i++)
        {
            if(options[i].trim().equals(label))
            {
                break;
            }
        }
        if(i < options.length)
        {
            super.select(selectName, options[i]);
        }
    }

    /**
     * This will select an option from a {@code select} field. If the field calls executes an ajax call onchange of
     * the value, this method will wait for that ajax method to finish.
     *
     * @param selectName the select field name
     * @param label the label to select
     */
    public void selectOptionAndWaitForAjaxWithJquery(String selectName, String label)
    {
        this.selectOption(selectName, label);
        this.waitForAjaxWithJquery();
    }

    /**
     * Checks a checkbox given a name and value.
     */
    public void check(String name, String value)
    {
        check("name=" + name + " value=" + value);
    }

    public void clickLinkWithText(String text, boolean waitForPageToLoad)
    {
        super.click("link=" + text);
        if (waitForPageToLoad) waitForPageToLoad();
    }

    public void clickButton(String buttonText, boolean waitForPageToLoad)
    {
        clickElementWithXpath("//input[@value = '" + buttonText + "']");
        if (waitForPageToLoad) waitForPageToLoad();
    }

    public void clickButtonAndWaitForAjaxWithJquery(String buttonText)
    {
        this.clickButton(buttonText, false);
        waitForAjaxWithJquery();
    }

    public void clickButtonWithName(String buttonName, boolean waitForPageToLoad)
    {
        clickElementWithXpath("//input[@name = '" + buttonName + "']");
        if (waitForPageToLoad) waitForPageToLoad();
    }

    public void clickButtonWithNameAndWaitForAjaxWithJquery(String buttonName)
    {
        this.clickButtonWithName(buttonName, false);
        waitForAjaxWithJquery();
    }

    public void clickElementWithTitle(String title)
    {
        super.click("xpath=//*[@title='" + title + "']");
    }

    public void clickElementWithTitleAndWaitForAjaxWithJquery(String title)
    {
        this.clickElementWithTitle(title);
        waitForAjaxWithJquery();
    }

    public void clickElementWithClass(String className)
    {
        super.click("css=." + className);
    }

    public void clickElementWithClassAndWaitForAjaxWithJquery(String className)
    {
        this.clickElementWithClass(className);
        waitForAjaxWithJquery();
    }

    public void clickElementWithCss(String cssSelector)
    {
        super.click("css=" + cssSelector);
    }

    public void clickElementWithCssAndWaitForAjaxWithJquery(String cssSelector)
    {
        this.clickElementWithCss(cssSelector);
        waitForAjaxWithJquery();
    }

    public void clickElementWithXpath(String xpath)
    {
        super.click("xpath=" + xpath);
    }

    public void clickElementWithXpathAndWaitForAjaxWithJquery(String xpath)
    {
        this.clickElementWithXpath(xpath);
        waitForAjaxWithJquery();
    }

    public void typeInElementWithName(String elementName, String text)
    {
        super.type("name=" + elementName, text);
    }

    public void typeInElementWithCss(String cssSelector, String text)
    {
        super.type("css=" + cssSelector, text);
    }

    public boolean hasJquery()
    {
        String evalJquery = getEval("selenium.browserbot.getCurrentWindow().jQuery");
        return evalJquery != null && !"null".equals(evalJquery) && !"undefined".equals(evalJquery);
    }

    private void addJqueryLocator() throws IOException
    {
        String jqueryImpl = readFile("jquery-1.4.2.min.js");
        String jqueryLocStrategy = readFile("jquery-locationStrategy.js");
        //client.setExtensionJs(jqueryImpl);
        addScript(jqueryImpl, "jquery");
        addLocationStrategy("jquery", jqueryLocStrategy );
    }

    private void addKeyPressJSCorrections() throws IOException
    {
        String keyPressCorrectionsImpl = readFile("KeyPressCorrected.js");
        String generateKeyPressImpl = readFile("generateKeyEvent.js");
        addScript(keyPressCorrectionsImpl, "KeyPressCorrected");        
        addScript(generateKeyPressImpl, "generateKeyEvent");
    }

    private void loadCharacterKeyEventMap() throws IOException
    {
        Gson gson = new GsonBuilder().create();
        String characterArrayJSON = readFile("charactersKeySequences.json");

        Type collectionType = new TypeToken<HashMap<String, KeyEventSequence>>(){}.getType();
        characterKeySequenceMap = gson.fromJson(characterArrayJSON,collectionType);
        characterKeySequenceMap.put(" ",characterKeySequenceMap.get(String.format("0x%x",java.awt.event.KeyEvent.VK_SPACE)));
        characterKeySequenceMap.put("\n",characterKeySequenceMap.get(String.format("0x%x",java.awt.event.KeyEvent.VK_ENTER)));
    }

    private void addTagNameScripts() throws IOException
    {
        String tagNames = readFile("tag-name.js");
        addScript(tagNames, "seleniumTagNames");
    }

    private static String readFile(String file) throws IOException
    {
        InputStream stream = SingleBrowserSeleniumClient.class.getClassLoader().getResourceAsStream(file);
        BufferedReader reader =  new BufferedReader(new InputStreamReader(stream));

        String line = reader.readLine();
        StringBuffer contents = new StringBuffer();

        while(line != null)
        {
            contents.append(line).append("\n");
            line = reader.readLine();
        }

        return contents.toString();
    }

    public void start()
    {
        super.start();
        try
        {
            addJqueryLocator();
            loadCharacterKeyEventMap();
            addKeyPressJSCorrections();
            addTagNameScripts();
        }
        catch (IOException ioe)
        {
            System.err.println("Unable to load JQuery locator strategy: " + ioe);
            ioe.printStackTrace(System.err);
        }

    }

    public void evaluate (String command)
    {
        execute(command);
    }

    /**
     * Execute a script on the client.
     * @param command a string of javascript to send to the client.
     * @param arguments Selenium does not accept additional arguments. These will be ignored.
     * @return The evaluated result of the script
     */
    public Object execute(String command, Object... arguments)
    {
        // For compatibility with VisualComparableClient
        return this.getEval(command);
    }

    public void captureEntirePageScreenshot (String filePath)
    {
        // For compatibility with VisualComparableClient
        this.captureEntirePageScreenshot(filePath, "");
    }

    public boolean resizeScreen(ScreenResolution resolution, boolean refreshAfterResize)
    {
        final String result = this.getEval("selenium.browserbot.getCurrentWindow().resizeTo(" + resolution.width + ", " + resolution.height + ");");
        if (refreshAfterResize) refreshAndWait();
        return true;
    }

    public void refreshAndWait ()
    {
        // For compatibility with VisualComparableClient
        this.refresh();
        this.waitForPageToLoad();
    }

    /**
     * Wait for jQuery AJAX calls to return.
     * @param waitTimeMillis the time to wait for everything to finish.
     * @return
     */
    public boolean waitForJQuery (long waitTimeMillis)
    {
        try
        {
            this.waitForCondition("selenium.browserbot.getCurrentWindow().jQuery.active == 0;", Long.toString(waitTimeMillis));
            Thread.sleep(400);
        }
        catch (InterruptedException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public ScreenElement getElementAtPoint(int x, int y)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
