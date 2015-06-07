package com.atlassian.selenium.pageobjects;

import com.atlassian.pageobjects.Tester;
import com.atlassian.selenium.SeleniumAssertions;
import com.atlassian.selenium.SeleniumClient;

/**
 *
 */
public interface SeleniumTester extends Tester
{
    SeleniumClient getClient();
    SeleniumAssertions getAssertions();
}
