package com.atlassian.webdriver.waiter.webdriver.function;

import com.google.common.base.Function;
import org.openqa.selenium.WebDriver;

/**
 * @since 2.1.0
 */
public interface ConditionFunction extends Function<WebDriver, Boolean>
{
}
