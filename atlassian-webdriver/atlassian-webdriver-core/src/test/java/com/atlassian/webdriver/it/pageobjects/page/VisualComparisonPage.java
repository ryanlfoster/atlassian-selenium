package com.atlassian.webdriver.it.pageobjects.page;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.webdriver.utils.element.WebDriverPoller;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class VisualComparisonPage implements Page
{
    @Inject
    private WebDriver driver;

    @Inject
    private WebDriverPoller poller;

    private final int pageNumber;

    public VisualComparisonPage(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }


    public String getUrl()
    {
        return "/html/visualcomparison/page" + pageNumber + ".html";
    }

    @WaitUntil
    public void waitUntilLoaded()
    {
        assertEquals(Integer.toString(pageNumber),
                driver.findElement(By.tagName("body")).getAttribute("data-page-number"));
    }
}
