package com.atlassian.webdriver.confluence.component.sherpa;

import com.atlassian.pageobjects.binder.Init;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.utils.Search;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

/**
 * TODO: Document this class / interface here
 *
 */
public class WelcomeScreen
{
    private WebElement dialogComponent;
    private WebElement dialogPanel;
    private WebElement closeButton;
    private WebElement title;
    private WebElement showAtStartupTickbox;

    @Inject
    AtlassianWebDriver driver;

    @Init
    public void init()
    {
        By dialogComponentBy = By.id("second-user-dialog");

        driver.waitUntilElementIsVisible(dialogComponentBy);
        dialogComponent = driver.findElement(dialogComponentBy);
        dialogPanel = dialogComponent.findElement(By.className("dialog-button-panel"));
        closeButton = Search.findElementWithText(By.tagName("button"), "Close", dialogPanel);
        title = dialogComponent.findElement(By.className("dialog-title"));
        showAtStartupTickbox = dialogPanel.findElement(By.id("hide-seconduser"));

        
    }

    public void showAtStartup(boolean show)
    {
        if (show)
        {
            if (!showAtStartupTickbox.isSelected()) {
                showAtStartupTickbox.click();
            }
        }
        else if (showAtStartupTickbox.isSelected())
        {
            showAtStartupTickbox.click();
        }
    }

    public String getTitle()
    {
        return title.getText();
    }

    public void close()
    {
        closeButton.click();
    }

}
