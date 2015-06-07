package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.query.AbstractTimedQuery;
import com.atlassian.pageobjects.elements.query.ExpirationHandler;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.webdriver.utils.JavaScriptUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.atlassian.pageobjects.elements.WebDriverLocators.staticElement;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Executes javascript on an element
 */
final class WebDriverElementJavascript implements PageElementJavascript
{
    private static final Iterable<String> SIMPLE_JS_RESULT_TYPES = ImmutableSet.of(
            Boolean.class.getName(),
            String.class.getName(),
            Long.class.getName(),
            List.class.getName()
    );


    private final WebDriver driver;
    private final PageBinder pageBinder;
    private final WebDriverElement element;

    private final JSMouse jsMouse = new JSMouse();
    private final JSForm jsForm = new JSForm();



    WebDriverElementJavascript(WebDriverElement element)
    {
        this.element = checkNotNull(element, "element");
        this.driver = element.driver;
        this.pageBinder = element.pageBinder;
    }

    public PageElementMouseJavascript mouse()
    {
        return jsMouse;
    }

    public PageElementFormJavascript form()
    {
        return jsForm;
    }


    public Object execute(String script, Object... arguments)
    {
        checkNotNull(script, "script");
        return convertResult(getExecutor().executeScript(script, convertArguments(arguments)));
    }

    public <T> T execute(Class<T> resultType, String script, Object... arguments)
    {
        checkNotNull(script, "script");
        validateResultType(resultType);
        return executeConverted(resultType, script, arguments);
    }

    private <T> T executeConverted(Class<T> resultType, String script, Object[] arguments)
    {
        return convertResult(getExecutor().executeScript(script, convertArguments(arguments)), resultType);
    }

    @Override
    public <T> TimedQuery<T> executeTimed(final Class<T> resultType, final String script, final Object... arguments)
    {
        checkNotNull(script, "script");
        validateSimpleResultType(resultType);
        return new AbstractTimedQuery<T>(element.timed().isPresent(), ExpirationHandler.RETURN_CURRENT)
        {
            @Override
            protected boolean shouldReturn(T currentEval)
            {
                return true;
            }

            @Override
            protected T currentValue()
            {
                return executeConverted(resultType, script, arguments);
            }
        };
    }

    @Override
    public <T> T executeAsync(Class<T> resultType, String script, Object... arguments)
    {
        checkNotNull(script, "script");
        validateResultType(resultType);
        return executeAsyncConverted(resultType, script, arguments);
    }

    public Object executeAsync(String script, Object... arguments)
    {
        checkNotNull(script, "script");
        return convertResult(getExecutor().executeAsyncScript(script, convertArguments(arguments)));
    }

    private <T> T executeAsyncConverted(Class<T> resultType, String script, Object[] arguments)
    {
        return convertResult(getExecutor().executeAsyncScript(script, convertArguments(arguments)), resultType);
    }

    private Object[] convertArguments(Object[] arguments)
    {
        for (int i=0; i<arguments.length;i++)
        {
            if (arguments[i] instanceof PageElement)
            {
                arguments[i] = WebDriverElement.getWebElement((PageElement) arguments[i]);
            }
        }
        return ArrayUtils.add(arguments, 0, element.waitForWebElement());
    }

    private Object convertResult(Object result)
    {
        if (result instanceof WebElement)
        {
            final WebElement webElement = (WebElement) result;
            if (element.getAttribute("id") != null && element.getAttribute("id").equals(webElement.getAttribute("id")))
            {
                // result same as owning element
                return element;
            }
            return pageBinder.bind(WebDriverElement.class, staticElement(webElement), element.defaultTimeout);
        }
        else
        {
            return result;
        }
    }

    private <T> T convertResult(Object result, Class<T> expected)
    {
        if (result instanceof WebElement)
        {
            if (!PageElement.class.isAssignableFrom(expected))
            {
                throw new ClassCastException("Expected instance of PageElement, was: " + expected);
            }
            final WebElement webElement = (WebElement) result;
            if (element.getAttribute("id") != null && element.getAttribute("id").equals(webElement.getAttribute("id")))
            {
                // result same as owning element
                return expected.cast(element);
            }
            @SuppressWarnings("unchecked") final Object stupidWorkaroundsForStupidGenericsProblemsHaaaaaateIt =
                    pageBinder.bind(WebDriverElementMappings.findMapping((Class)expected), staticElement(webElement), element.defaultTimeout);
            return expected.cast(stupidWorkaroundsForStupidGenericsProblemsHaaaaaateIt);
        }
        else
        {
            return expected.cast(result);
        }
    }

    private JavascriptExecutor getExecutor()
    {
        checkState(driver instanceof JavascriptExecutor, driver + " does not support Javascript");
        return (JavascriptExecutor) driver;
    }

    private <T> void validateSimpleResultType(Class<T> resultType)
    {
        checkNotNull(resultType, "resultType");
        if (!Iterables.contains(SIMPLE_JS_RESULT_TYPES, resultType.getName()))
        {
            throw new IllegalArgumentException("Class '" + resultType.getName() + "' is not a simple JS return type");
        }
    }

    private <T> void validateResultType(Class<T> resultType)
    {
        if (Iterables.contains(SIMPLE_JS_RESULT_TYPES, resultType.getName()))
        {
            return;
        }
        if (PageElement.class.isAssignableFrom(resultType))
        {
            return;
        }
        throw new IllegalArgumentException("Class '" + resultType.getName() + "' is not a supported JS return type");
    }

    private class JSMouse implements PageElementMouseJavascript
    {

        public PageElementJavascript click()
        {
            return dispatch("click");
        }

        public PageElementJavascript doubleClick()
        {
            return dispatch("dblclick");
        }

        public PageElementJavascript mouseup()
        {
            return dispatch("mouseup");
        }

        public PageElementJavascript mousedown()
        {
            return dispatch("mousedown");
        }

        public PageElementJavascript mouseover()
        {
            return dispatch("mouseover");
        }

        public PageElementJavascript mousemove()
        {
            return dispatch("mousemove");
        }

        public PageElementJavascript mouseout()
        {
            return dispatch("mouseout");
        }

    }


    private class JSForm implements PageElementFormJavascript
    {

        public PageElementJavascript select()
        {
            return dispatch("select");
        }

        public PageElementJavascript change()
        {
            return dispatch("change");
        }

        public PageElementJavascript submit()
        {
            return dispatch("submit");
        }

        public PageElementJavascript focus()
        {
            return dispatch("focus");
        }

        public PageElementJavascript blur()
        {
            return dispatch("blur");
        }
    }

    private PageElementJavascript dispatch(String eventType)
    {
        JavaScriptUtils.dispatchEvent(eventType, element.waitForWebElement(), driver);
        return this;
    }
}
