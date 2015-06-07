package com.atlassian.webdriver.waiter.webdriver.function.element;

import com.atlassian.webdriver.waiter.webdriver.retriever.WebElementRetriever;
import com.atlassian.webdriver.waiter.webdriver.function.ConditionFunction;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

/**
 * @since 2.1.0
 */
public class ExistsFunction implements ConditionFunction
{
    private final WebElementRetriever elementRetriever;

    public ExistsFunction(final WebElementRetriever elementRetriever)
    {
        this.elementRetriever = elementRetriever;
    }

    public Boolean apply(WebDriver from)
    {
        try
        {
            // Make any call on the element.
            elementRetriever.retrieveElement().isDisplayed();
            return true;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
        catch (StaleElementReferenceException e)
        {
            return false;
        }
    }
}
