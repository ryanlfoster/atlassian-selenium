package com.atlassian.selenium.mock;

import com.atlassian.selenium.Browser;
import com.atlassian.selenium.SeleniumClient;
import com.atlassian.selenium.visualcomparison.ScreenElement;
import com.atlassian.selenium.visualcomparison.utils.ScreenResolution;
import com.atlassian.webtest.ui.keys.KeyEventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mock implementation of the {@link com.atlassian.selenium.SeleniumClient} interface.
 *
 * <p>
 * Probably the longest mock-that-does-nothing in the history of software.
 *
 * @since v1.21
 */
public class MockSeleniumClient implements SeleniumClient
{
    private final List<String> presentElementLocators = new ArrayList<String>();

    private final Map<String,String> scripts = new HashMap<String,String>();
    private String unmatchedScriptResult;
    private final List<String> executedScripts = new ArrayList<String>();

    public void open(final String url)
    {
    }

    public void openWindow(final String s, final String s1)
    {
    }

    public void selectWindow(final String s)
    {
    }

    public void selectPopUp(final String s)
    {
    }

    public void deselectPopUp()
    {
    }

    public void selectFrame(final String s)
    {
    }

    public boolean getWhetherThisFrameMatchFrameExpression(final String s, final String s1)
    {
        return false;
    }

    public boolean getWhetherThisWindowMatchWindowExpression(final String s, final String s1)
    {
        return false;
    }

    public void waitForPopUp(final String s, final String s1)
    {
    }

    public void chooseCancelOnNextConfirmation()
    {
    }

    public void chooseOkOnNextConfirmation()
    {
    }

    public void answerOnNextPrompt(final String s)
    {
    }

    public void goBack()
    {
    }

    public void refresh()
    {
    }

    public void close()
    {
    }

    public boolean isAlertPresent()
    {
        return false;
    }

    public boolean isPromptPresent()
    {
        return false;
    }

    public boolean isConfirmationPresent()
    {
        return false;
    }

    public String getAlert()
    {
        return null;
    }

    public String getConfirmation()
    {
        return null;
    }

    public String getPrompt()
    {
        return null;
    }

    public String getLocation()
    {
        return null;
    }

    public String getTitle()
    {
        return null;
    }

    public String getBodyText()
    {
        return null;
    }

    public String getValue(final String s)
    {
        return null;
    }

    public String getText(final String s)
    {
        return null;
    }

    public void highlight(final String s)
    {
    }

    public String getEval(final String script)
    {
        executedScripts.add(script);
        String result = scripts.get(script);
        if (result != null)
        {
            return result;
        }
        return unmatchedScriptResult;
    }

    public MockSeleniumClient addScriptResult(String script, String result)
    {
        scripts.put(script, result);
        return this;
    }

    public MockSeleniumClient genericScriptResult(String result)
    {
        this.unmatchedScriptResult = result;
        return this;
    }

    public List<String> executedScripts()
    {
        return new ArrayList<String>(executedScripts);
    }

    public boolean isChecked(final String s)
    {
        return false;
    }

    public String getTable(final String s)
    {
        return null;
    }

    public String[] getSelectedLabels(final String s)
    {
        return new String[0];
    }

    public String getSelectedLabel(final String s)
    {
        return null;
    }

    public String[] getSelectedValues(final String s)
    {
        return new String[0];
    }

    public String getSelectedValue(final String s)
    {
        return null;
    }

    public String[] getSelectedIndexes(final String s)
    {
        return new String[0];
    }

    public String getSelectedIndex(final String s)
    {
        return null;
    }

    public String[] getSelectedIds(final String s)
    {
        return new String[0];
    }

    public String getSelectedId(final String s)
    {
        return null;
    }

    public boolean isSomethingSelected(final String s)
    {
        return false;
    }

    public String[] getSelectOptions(final String s)
    {
        return new String[0];
    }

    public String getAttribute(final String s)
    {
        return null;
    }

    public boolean isTextPresent(final String s)
    {
        return false;
    }


    public boolean isElementPresent(String elemLocator)
    {
        return presentElementLocators.contains(elemLocator);
    }

    public MockSeleniumClient addPresentElements(String... elements)
    {
        this.presentElementLocators.addAll(Arrays.asList(elements));
        return this;
    }

    public boolean isVisible(final String s)
    {
        return false;
    }

    public boolean isEditable(final String s)
    {
        return false;
    }

    public String[] getAllButtons()
    {
        return new String[0];
    }

    public String[] getAllLinks()
    {
        return new String[0];
    }

    public String[] getAllFields()
    {
        return new String[0];
    }

    public String[] getAttributeFromAllWindows(final String s)
    {
        return new String[0];
    }

    public void dragdrop(final String s, final String s1)
    {
    }

    public void setMouseSpeed(final String s)
    {
    }

    public Number getMouseSpeed()
    {
        return null;
    }

    public void dragAndDrop(final String s, final String s1)
    {
    }

    public void dragAndDropToObject(final String s, final String s1)
    {
    }

    public void windowFocus()
    {
    }

    public void windowMaximize()
    {
    }

    public String[] getAllWindowIds()
    {
        return new String[0];
    }

    public String[] getAllWindowNames()
    {
        return new String[0];
    }

    public String[] getAllWindowTitles()
    {
        return new String[0];
    }

    public String getHtmlSource()
    {
        return null;
    }

    public void setCursorPosition(final String s, final String s1)
    {
    }

    public Number getElementIndex(final String s)
    {
        return null;
    }

    public boolean isOrdered(final String s, final String s1)
    {
        return false;
    }

    public Number getElementPositionLeft(final String s)
    {
        return null;
    }

    public Number getElementPositionTop(final String s)
    {
        return null;
    }

    public Number getElementWidth(final String s)
    {
        return null;
    }

    public Number getElementHeight(final String s)
    {
        return null;
    }

    public Number getCursorPosition(final String s)
    {
        return null;
    }

    public String getExpression(final String s)
    {
        return null;
    }

    public Number getXpathCount(final String s)
    {
        return null;
    }

    public Number getCssCount(String css)
    {
        return null;
    }

    public void assignId(final String s, final String s1)
    {
    }

    public void allowNativeXpath(final String s)
    {
    }

    public void ignoreAttributesWithoutValue(final String s)
    {
    }

    public void waitForCondition(final String s, final String s1)
    {
    }

    public void setTimeout(final String s)
    {
    }

    public void waitForPageToLoad(final String s)
    {
    }

    public void waitForFrameToLoad(final String s, final String s1)
    {
    }

    public String getCookie()
    {
        return null;
    }

    public String getCookieByName(final String s)
    {
        return null;
    }

    public boolean isCookiePresent(final String s)
    {
        return false;
    }

    public void createCookie(final String s, final String s1)
    {
    }

    public void deleteCookie(final String s, final String s1)
    {
    }

    public void deleteAllVisibleCookies()
    {
    }

    public void setBrowserLogLevel(final String s)
    {
    }

    public void runScript(final String s)
    {
    }

    public void addLocationStrategy(final String s, final String s1)
    {
    }

    public void captureEntirePageScreenshot(final String s, final String s1)
    {
    }

    public void rollup(final String s, final String s1)
    {
    }

    public void addScript(final String s, final String s1)
    {
    }

    public void removeScript(final String s)
    {
    }

    public void useXpathLibrary(final String s)
    {
    }

    public void setContext(final String s)
    {
    }

    public void attachFile(final String s, final String s1)
    {
    }

    public void captureScreenshot(final String s)
    {
    }

    public String captureScreenshotToString()
    {
        return null;
    }

    public String captureNetworkTraffic(final String s)
    {
        return null;
    }

    public void addCustomRequestHeader(final String s, final String s1)
    {
    }

    public String captureEntirePageScreenshotToString(final String s)
    {
        return null;
    }

    public void shutDownSeleniumServer()
    {
    }

    public String retrieveLastRemoteControlLogs()
    {
        return null;
    }

    public void keyDownNative(final String s)
    {
    }

    public void keyUpNative(final String s)
    {
    }

    public void keyPressNative(final String s)
    {
    }

    public void openNoWait(final String url)
    {
    }

    public void open(final String url, final long timeoutMillis)
    {
    }

    public void waitForPageToLoad(final long timeoutMillis)
    {
    }

    public void waitForPageToLoad()
    {
    }

    public void waitForCondition(final String javascript)
    {
    }

    public void waitForCondition(final String javascript, final long timeoutMillis)
    {
    }

    public void waitForAjaxWithJquery()
    {
    }

    public void waitForAjaxWithJquery(final long timeoutMillis)
    {
    }

    public void click(final String locator, final boolean waitForPageToLoad)
    {
    }

    public void submit(final String form, final boolean waitForPageToLoad)
    {
    }

    public void click(final String locator, final long timeoutMillis)
    {
    }

    public void clickAndWaitForAjaxWithJquery(final String locator)
    {
    }

    public void clickAndWaitForAjaxWithJquery(final String locator, final long timeoutMillis)
    {
    }

    public void submit(final String form, final long timeoutMillis)
    {
    }

    public void keyPress(final String locator, final String key)
    {
    }

    public void shiftKeyDown()
    {
    }

    public void shiftKeyUp()
    {
    }

    public void metaKeyDown()
    {
    }

    public void metaKeyUp()
    {
    }

    public void altKeyDown()
    {
    }

    public void altKeyUp()
    {
    }

    public void controlKeyDown()
    {
    }

    public void controlKeyUp()
    {
    }

    public void keyDown(final String s, final String s1)
    {
    }

    public void keyUp(final String s, final String s1)
    {
    }

    public void mouseOver(final String s)
    {
    }

    public void mouseOut(final String s)
    {
    }

    public void mouseDown(final String s)
    {
    }

    public void mouseDownRight(final String s)
    {
    }

    public void mouseDownAt(final String s, final String s1)
    {
    }

    public void mouseDownRightAt(final String s, final String s1)
    {
    }

    public void mouseUp(final String s)
    {
    }

    public void mouseUpRight(final String s)
    {
    }

    public void mouseUpAt(final String s, final String s1)
    {
    }

    public void mouseUpRightAt(final String s, final String s1)
    {
    }

    public void mouseMove(final String s)
    {
    }

    public void mouseMoveAt(final String s, final String s1)
    {
    }

    public void type(final String s, final String s1)
    {
    }

    public void typeKeys(final String s, final String s1)
    {
    }

    public void setSpeed(final String s)
    {
    }

    public String getSpeed()
    {
        return null;
    }

    public String getLog()
    {
        return null;
    }

    public void check(final String s)
    {
    }

    public void uncheck(final String s)
    {
    }

    public void select(final String s, final String s1)
    {
    }

    public void addSelection(final String s, final String s1)
    {
    }

    public void removeSelection(final String s, final String s1)
    {
    }

    public void removeAllSelections(final String s)
    {
    }

    public void submit(final String s)
    {
    }

    public void open(final String s, final String s1)
    {
    }

    public void typeWithFullKeyEvents(final String locator, final String string, final boolean reset)
    {
    }

    public void typeWithFullKeyEvents(final String locator, final String string)
    {
    }

    public void simulateKeyPressForCharacter(final String locator, final Character character, final Collection<KeyEventType> eventsToFire)
    {
    }

    public void simulateKeyPressForCharacter(String s, Character character)
    {
    }

    public void simulateKeyPressForSpecialKey(final String locator, final int keyCode, final Collection<KeyEventType> eventsToFire)
    {
    }

    public void generateKeyEvent(String locator, KeyEventType eventType, int keyCode, int characterCode, boolean shiftKey, boolean altKey, boolean controlKey, boolean metaKey) {
    }

    public void simulateKeyPressForSpecialKey(String s, int i)
    {
    }

    public void toggleToKeyCode(boolean b)
    {
    }

    public void toggleToCharacterCode(boolean b)
    {
    }

    public void selectOption(final String selectName, final String label)
    {
    }

    public void selectOptionAndWaitForAjaxWithJquery(final String selectName, final String label)
    {
    }

    public void check(final String name, final String value)
    {
    }

    public void clickLinkWithText(final String text, final boolean waitForPageToLoad)
    {
    }

    public void clickButton(final String buttonText, final boolean waitForPageToLoad)
    {
    }

    public void clickButtonAndWaitForAjaxWithJquery(final String buttonText)
    {
    }

    public void clickButtonWithName(final String buttonName, final boolean waitForPageToLoad)
    {
    }

    public void clickButtonWithNameAndWaitForAjaxWithJquery(final String buttonName)
    {
    }

    public void clickElementWithTitle(final String title)
    {
    }

    public void clickElementWithTitleAndWaitForAjaxWithJquery(final String title)
    {
    }

    public void clickElementWithClass(final String className)
    {
    }

    public void clickElementWithClassAndWaitForAjaxWithJquery(final String className)
    {
    }

    public void clickElementWithCss(final String cssSelector)
    {
    }

    public void clickElementWithCssAndWaitForAjaxWithJquery(final String cssSelector)
    {
    }

    public void clickElementWithXpath(final String xpath)
    {
    }

    public void clickElementWithXpathAndWaitForAjaxWithJquery(final String xpath)
    {
    }

    public void typeInElementWithName(final String elementName, final String text)
    {
    }

    public void typeInElementWithCss(final String cssSelector, final String text)
    {
    }

    public boolean hasJquery()
    {
        return false;
    }

    public void setExtensionJs(final String s)
    {
    }

    public void start()
    {
    }

    public void start(final String s)
    {
    }

    public void start(final Object o)
    {
    }

    public void stop()
    {
    }

    public void showContextualBanner()
    {
    }

    public void showContextualBanner(final String s, final String s1)
    {
    }

    public void click(final String s)
    {
    }

    public void doubleClick(final String s)
    {
    }

    public void contextMenu(final String s)
    {
    }

    public void clickAt(final String s, final String s1)
    {
    }

    public void doubleClickAt(final String s, final String s1)
    {
    }

    public void contextMenuAt(final String s, final String s1)
    {
    }

    public void fireEvent(final String s, final String s1)
    {
    }

    public void focus(final String s)
    {
    }

    public Browser getBrowser()
    {
        return null;
    }

    public void seleniumKeyPress(final String locator, final String key)
    {
    }

    public void evaluate (String command)
    {
    }

    public Object execute (String command, Object... arguments)
    {
        return null;
    }

    public void captureEntirePageScreenshot (String filePath)
    {
    }

    public boolean resizeScreen(ScreenResolution resolution, boolean refreshAfterResize)
    {
        return false;
    }

    public void refreshAndWait ()
    {
    }

    public boolean waitForJQuery (long waitTimeMillis)
    {
        return false;
    }

    @Override
    public ScreenElement getElementAtPoint(int x, int y)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
