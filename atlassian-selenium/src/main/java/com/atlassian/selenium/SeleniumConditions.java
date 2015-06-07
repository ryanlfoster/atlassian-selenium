package com.atlassian.selenium;

import com.atlassian.selenium.pageobjects.PageElement;

/**
 * Created by IntelliJ IDEA.
 * User: hbarney
 * Date: 11/11/2010
 * Time: 11:25:12 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SeleniumConditions {
    public boolean visibleByTimeout(String locator);

    public boolean visibleByTimeout(PageElement element);

    public boolean visibleByTimeout(String locator, long maxMillis);

    public boolean visibleByTimeout(PageElement element, long maxMillis);

    public boolean notVisibleByTimeout(String locator);

    public boolean notVisibleByTimeout(PageElement element);

    void notVisibleByTimeout(String locator, long maxMillis);

    void notVisibleByTimeout(PageElement element, long maxMillis);

    void elementPresentByTimeout(String locator);

    void elementPresentByTimeout(PageElement element);

    void elementPresentByTimeout(String locator, long maxMillis);

    void elementPresentByTimeout(PageElement element, long maxMillis);

    void elementPresentUntilTimeout(String locator);

    void elementPresentUntilTimeout(PageElement element);

    void elementPresentUntilTimeout(String locator, long maxMillis);

    void elementPresentUntilTimeout(PageElement element, long maxMillis);

    void elementNotPresentByTimeout(String locator);

    void elementNotPresentByTimeout(PageElement element);

    void elementNotPresentUntilTimeout(String locator);

    void elementNotPresentUntilTimeout(PageElement element);

    void elementNotPresentUntilTimeout(String locator, long maxMillis);

    void elementNotPresentUntilTimeout(PageElement element, long maxMillis);

    void textPresentByTimeout(String text, long maxMillis);

    void textPresentByTimeout(String text);

    void textNotPresentByTimeout(String text, long maxMillis);

    void textNotPresentByTimeout(String text);

    void elementNotPresentByTimeout(String locator, long maxMillis);

    void elementNotPresentByTimeout(PageElement element, long maxMillis);

    void byTimeout(Condition condition);

    void byTimeout(Condition condition, long maxWaitTime);

    void untilTimeout(Condition condition);

    void untilTimeout(Condition condition, long maxWaitTime);

    void textPresent(String text);

    void textNotPresent(String text);

    void formElementEquals(String locator, String value);

    void elementPresent(String locator);

    void elementPresent(PageElement element);

    void elementNotPresent(String locator);

    void elementNotPresent(PageElement element);

    void elementVisible(String locator);

    void elementVisible(PageElement element);

    void elementNotVisible(String locator);

    void elementNotVisible(PageElement element);

    void elementVisibleContainsText(String locator, String text);

    void elementVisibleContainsText(PageElement element, String text);

    void htmlPresent(String html);

    void htmlNotPresent(String html);

    void elementHasText(String locator, String text);

    void elementHasText(PageElement element, String text);

    void elementDoesntHaveText(String locator, String text);

    void elementDoesntHaveText(PageElement element, String text);

    void attributeContainsValue(String locator, String attribute, String value);

    void attributeContainsValue(PageElement element, String attribute, String value);

    void attributeDoesntContainValue(String locator, String attribute, String value);

    void attributeDoesntContainValue(PageElement element, String attribute, String value);

    void linkPresentWithText(String text);

    void linkNotPresentWithText(String text);

    void linkVisibleWithText(String text);

    void elementsVerticallyAligned(String locator1, String locator2, int deltaPixels);

    void elementsVerticallyAligned(PageElement element1, PageElement element2, int deltaPixels);

    void elementsSameHeight(String locator1, String locator2, int deltaPixels);

    void elementsSameHeight(PageElement element1, PageElement element2, int deltaPixels);

    void elementContainsText(String locator, String text);

    void elementContainsText(PageElement element, String text);

    void elementDoesNotContainText(String locator, String text);

    void elementDoesNotContainText(PageElement element, String text);

    void windowClosed(String windowName);

    void windowOpen(String windowName);
}
