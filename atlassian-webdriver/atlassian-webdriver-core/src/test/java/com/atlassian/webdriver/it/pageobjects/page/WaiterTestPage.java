package com.atlassian.webdriver.it.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.webdriver.waiter.Waiter;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

/**
 * @since 2.1.0
 */
public class WaiterTestPage implements Page
{
    @Inject
    Waiter waiter;

    @Inject
    WebDriver driver;

    public String getUrl()
    {
        return "/html/waiter-test-page.html";
    }

    public Waiter getWaiter()
    {
        return waiter;
    }
}
