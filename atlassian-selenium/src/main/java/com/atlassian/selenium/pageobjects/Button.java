package com.atlassian.selenium.pageobjects;

import com.atlassian.selenium.SeleniumClient;

public class Button extends PageElement
{
    public Button(String locator, SeleniumClient client)
    {
        super(locator, client);
    }

    public String getText()
    {
        return client.getAttribute(locator + "/@value");
    }

}