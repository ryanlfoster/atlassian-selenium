package com.atlassian.webdriver.confluence.page;

import com.atlassian.pageobjects.page.AdminHomePage;
import com.atlassian.webdriver.confluence.component.header.ConfluenceHeader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * TODO: Document this class / interface here
 *
 */
public class ConfluenceAdminHomePage extends ConfluenceAbstractPage implements AdminHomePage<ConfluenceHeader>
{
    private static final String URI = "/admin/console.action";

    @FindBy (linkText = "Plugins")
    private WebElement pluginsLink;

    @FindBy (linkText = "License Details")
    private WebElement licenseDetailsLink;

    public String getUrl()
    {
        return URI;
    }

    public PluginsPage gotoPluginsPage()
    {
        pluginsLink.click();

        return pageBinder.bind(PluginsPage.class);
    }

    public LicenseDetailsPage gotoLicenseDetailsPage()
    {
        licenseDetailsLink.click();

        return pageBinder.bind(LicenseDetailsPage.class);
    }
}
