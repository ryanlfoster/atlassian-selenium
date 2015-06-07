package com.atlassian.webdriver.waiter;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.webdriver.waiter.webdriver.function.ConditionFunction;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public interface WaiterQuery
{
    FunctionQuery function(ConditionFunction func);
    ElementQuery element(By locator);
    ElementQuery element(By locator, SearchContext context);
    ElementQuery element(WebElement element);
}
