package com.atlassian.webdriver.waiter.webdriver.function.field;

import com.atlassian.webdriver.waiter.webdriver.retriever.WebElementFieldRetriever;
import com.atlassian.webdriver.waiter.webdriver.function.ConditionFunction;
import org.openqa.selenium.WebDriver;

/**
 * @since 2.1.0
 */
public class IsEmptyFunction implements ConditionFunction
{
    private final WebElementFieldRetriever fieldRetriever;

    public IsEmptyFunction(WebElementFieldRetriever fieldRetriever)
    {
        this.fieldRetriever = fieldRetriever;
    }

    public Boolean apply(WebDriver from)
    {
        String fieldValue = fieldRetriever.retrieveField();
        return fieldValue == null || "".equals(fieldValue);
    }
}
