package com.atlassian.webdriver.jira.component.dashboard;

import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.jira.JiraTestedProduct;
import org.openqa.selenium.*;

import javax.inject.Inject;
import java.util.List;

/**
 * GadgetView is an implentation of the internal view for a gadget and just extends a
 * WebElement so all the normal methods that WebDriver exposes on web elements is available.
 */
public class GadgetView implements WebElement
{
    @Inject
    AtlassianWebDriver driver;

    private final WebElement view;

    public GadgetView(WebElement view)
    {
        this.view = view;
    }

    /**
     * Closes the Gadget view and returns the WebDriver context back to the default content
     * Which is usually the Dashboard content.
     */
    public void close()
    {
        driver.switchTo().defaultContent();
    }

    public void click()
    {
        view.click();
    }

    public void submit()
    {
        view.submit();
    }

    /**
     * getValue has been removed from WebElement
     * @deprecated Use {@link WebElement#getAttribute(String)}
     */
    @Deprecated
    public String getValue()
    {
        return view.getAttribute("value");
    }

    public void sendKeys(final CharSequence... charSequences)
    {
        view.sendKeys();
    }

    public void clear()
    {
        view.clear();
    }

    public String getTagName()
    {
        return view.getTagName();
    }

    public String getAttribute(final String s)
    {
        return view.getAttribute(s);
    }

    /**
     * This has been removed from WebElement.
     * @deprecated Use {@link WebElement#click()}
     */
    @Deprecated
    public boolean toggle()
    {
        view.click();
        return view.isSelected();
    }

    public boolean isSelected()
    {
        return view.isSelected();
    }

    /**
     * setSelected has been removed from WebElement
     * @deprecated Use {@link WebElement#click()}
     */
    @Deprecated
    public void setSelected()
    {
        if (!view.isSelected()) {
            view.click();
        }
    }

    public boolean isEnabled()
    {
        return view.isEnabled();
    }

    public String getText()
    {
        return view.getText();
    }

    public List<WebElement> findElements(final By by)
    {
        return view.findElements(by);
    }

    public WebElement findElement(final By by)
    {
        return view.findElement(by);
    }

    public boolean isDisplayed()
    {
        return view.isDisplayed();
    }

    public Point getLocation()
    {
        return view.getLocation();
    }

    public Dimension getSize()
    {
        return view.getSize();
    }

    public String getCssValue(String propertyName)
    {
        return view.getCssValue(propertyName);
    }
}