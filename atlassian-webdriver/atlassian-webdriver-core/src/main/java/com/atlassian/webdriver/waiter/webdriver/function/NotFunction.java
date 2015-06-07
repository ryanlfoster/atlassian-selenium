package com.atlassian.webdriver.waiter.webdriver.function;

import org.openqa.selenium.WebDriver;

/**
 * @since 2.1.0
 */
public class NotFunction implements ConditionFunction
{
    private final ConditionFunction func;

    public NotFunction(ConditionFunction func)
    {
        this.func = func;
    }

    public Boolean apply(WebDriver driver)
    {
        return !func.apply(driver);
    }
}
