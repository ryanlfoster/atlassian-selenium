package com.atlassian.webdriver.waiter.webdriver.function.element;

import com.atlassian.webdriver.waiter.webdriver.retriever.WebElementRetriever;
import com.atlassian.webdriver.waiter.webdriver.function.ConditionFunction;
import org.openqa.selenium.WebDriver;

/**
 * @since 2.1.0
 */
public class IsEnabledFunction implements ConditionFunction
{
    private final WebElementRetriever elementRetriever;

    public IsEnabledFunction(final WebElementRetriever elementRetriever)
    {
        this.elementRetriever = elementRetriever;
    }
    
    public Boolean apply(WebDriver from)
    {
        return elementRetriever.retrieveElement().isEnabled();
    }
}
