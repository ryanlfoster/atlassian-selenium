package com.atlassian.webdriver.confluence.page;

import com.atlassian.webdriver.utils.by.ByJquery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: Document this class / interface here
 *
 */
public class LicenseDetailsPage extends ConfluenceAbstractPage
{

    private static String URI = "/admin/license.action";

    @FindBy (className = "confluenceTable")
    WebElement licenseTable;

    @FindBy (name = "licenseString")
    WebElement updateLicenseTextArea;

    @FindBy (name = "update")
    WebElement submitLicenseButton;


    public String getUrl()
    {
        return URI;
    }

    public String getOrganisation()
    {
        return licenseTable.findElement(ByJquery.$("td:contains(Organisation) ~ td")).getText();
    }

    public Date getDatePurchased()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        String datePurchased = licenseTable.findElement(ByJquery.$("td:contains(Date Purchased) ~ td")).getText();

        Date date;
        try
        {
            date = format.parse(datePurchased);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }

        return date;
    }

    // TODO: fix this
    public boolean isEvaluation() {
        throw new UnsupportedOperationException("isEvaluation hasn't been implemented yet");
    }

    public String getLicenseType()
    {
        return licenseTable.findElement(ByJquery.$("td:contains(License Type) ~ td")).getText();
    }

    public String getSupportEntitlementNumber()
    {
        return licenseTable.findElement(ByJquery.$("td:contains(Support Entitlement Number) ~ td")).getText();
    }

    public String getServerID()
    {
        return licenseTable.findElement(ByJquery.$("td:contains(Server ID) ~ td strong")).getText();
    }

    public int getUserLimit()
    {
        return Integer.valueOf(licenseTable.findElement(ByJquery.$("td:contains(Licensed Users) ~ td > strong")).getText());
    }

    public int getActiveUsers()
    {
        String userLimit = licenseTable.findElement(ByJquery.$("td:contains(Licensed Users) ~ td")).getText();

        Pattern re = Pattern.compile("[(]([0-9]+) signed up currently[)]");
        Matcher m = re.matcher(userLimit);

        if (m.find())
        {
            String activeUSers = m.group(1);
            return Integer.valueOf(activeUSers);
        }

        return -1;
    }

    public LicenseDetailsPage updateLicense(String license)
    {

        updateLicenseTextArea.sendKeys(license);
        submitLicenseButton.click();

        return pageBinder.bind(LicenseDetailsPage.class);
    }

}
