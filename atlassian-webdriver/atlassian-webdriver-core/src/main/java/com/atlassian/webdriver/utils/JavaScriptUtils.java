package com.atlassian.webdriver.utils;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;

/**
 * JavaScript Utilities for executing specific javascript events.
 */
public class JavaScriptUtils
{

    private JavaScriptUtils() {}

    /**
     * @deprecated Use {@link WebElementUtil#getInnerHtml(WebElement, WebDriver)}
     */
    @Deprecated
    public static String innerHtml(WebElement element, WebDriver driver)
    {
        return WebElementUtil.getInnerHtml(element, driver);
    }


    public static void dispatchEvent(String eventType, WebElement element, WebDriver webDriver)
    {
        loadScript("js/atlassian/events/customevents.js", webDriver);
        execute("ATLWD.events.fireEventForElement(arguments[0],'" + eventType + "');", webDriver, element);
    }

    /**
     * Dispatches a javascript mouse event in the browser on a specified element
     *
     * @param event The name of the event to dispatch. eg. load.
     * @param el The element to fire the event on.
     * @param driver the webdriver instance that executes the javascript event.
     */
    public static void dispatchMouseEvent(String event, WebElement el, WebDriver driver)
    {
        dispatchEvent(event, el, driver);
    }

    /**
     * Will load a javascript file from the class loader.
     * @param jsScriptName the name of the javascipt file
     * @param driver the webdriver to execute the javascript
     * @return true if the file was loaded. false if it was already loaded.
     */
    public static boolean loadScript(String jsScriptName, WebDriver driver)
    {
        if (!isScriptLoaded(jsScriptName,driver))
        {
            doLoadScript(jsScriptName, driver, true);
            return true;
        }

        return false;
    }

    public static boolean isScriptLoaded(String jsScriptName, WebDriver webDriver)
    {
        loadCoreScript(webDriver);
        return execute(Boolean.class, "return window.ATLWD.scriptloader.isLoaded('" + jsScriptName + "');" , webDriver);
    }

    private static void doLoadScript(String jsScriptName, WebDriver driver, boolean addToLoaded)
    {
        InputStream scriptStream = null;
        try
        {
            scriptStream = loadResource(jsScriptName);
            String jsSource = IOUtils.toString(scriptStream);
            if (addToLoaded)
            {
                jsSource = "window.ATLWD.scriptloader.scriptLoaded('" + jsScriptName + "');" + jsSource;
            }
            JavaScriptUtils.execute(jsSource, driver);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to load the javascript file: " + jsScriptName, e);
        }
        finally
        {
            IOUtils.closeQuietly(scriptStream);
        }

    }

    private static void loadCoreScript(WebDriver webDriver)
    {
        if (Boolean.FALSE.equals(isCoreLoaded(webDriver)))
        {
            doLoadScript("js/atlassian/atlassian-webdriver-core.js", webDriver, false);
            doLoadScript("js/atlassian/scriptloader/scriptloader.js", webDriver, false);
            loadScript("js/jquery/jquery-1.4.2.min.js", webDriver);
            execute("ATLWD.loadJquery()", webDriver);
        }
    }

    private static boolean isCoreLoaded(WebDriver webDriver)
    {
        return execute(Boolean.class, "return window.ATLWD != undefined && window.ATLWD.scriptloader != undefined", webDriver);
    }

    private static InputStream loadResource(String jsScriptName)
    {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(jsScriptName);
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(jsScriptName);
        }
        if (is == null) {
            throw new RuntimeException("javascript resource " + jsScriptName
                    + " not found by system or context classloaders.");
        }
        return is;
    }

    public static <T> T execute(String js, WebDriver driver, Object... arguments)
    {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (T) jsExecutor.executeScript(js, arguments);
    }


    public static <T> T execute(Class<T> expectedReturn, String js, WebDriver driver, Object... arguments)
    {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        final Object result = jsExecutor.executeScript(js, arguments);
        if (result != null && !expectedReturn.isInstance(result))
        {
            throw new ClassCastException("Expected result type " + expectedReturn.getName() + " but was: "
                    + result.getClass().getName());
        }
        return expectedReturn.cast(result);
    }
}