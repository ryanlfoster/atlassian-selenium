package com.atlassian.webdriver.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @since 2.1
 */
public final class WebElementUtil
{
    private WebElementUtil() {}

    public static String getInnerHtml(WebElement el, WebDriver driver)
    {
        return JavaScriptUtils.execute("return arguments[0].innerHTML", driver, el);
    }
}
