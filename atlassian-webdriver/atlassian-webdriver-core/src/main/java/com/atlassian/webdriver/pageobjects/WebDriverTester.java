package com.atlassian.webdriver.pageobjects;

import com.atlassian.pageobjects.Tester;
import com.atlassian.webdriver.AtlassianWebDriver;

/**
 * Tester to be used by Atlassian pageobject tests.
 */
public interface WebDriverTester extends Tester
{
    AtlassianWebDriver getDriver();
}
