package com.atlassian.webdriver.confluence.component.macro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * TODO: Document this class / interface here
 *
 */
public class UserMacro
{

    private WebElement vCardElement;

    private WebElement userLogoLink;
    private WebElement userLogo;
    private WebElement usernameLink;
    private WebElement emailLink;

    private String username;
    private String fullName;
    private String email;

    /**
     * 
     */
    public UserMacro(final By componentLocator, WebElement profileElement)
    {

        vCardElement = profileElement.findElement(componentLocator);

        userLogoLink = vCardElement.findElement(By.className("userLogoLink"));
        userLogo = vCardElement.findElement(By.className("userLogo"));
        usernameLink = vCardElement.findElement(By.className("confluence-userlink"));
        emailLink = vCardElement.findElement(By.className("email"));

        username = usernameLink.getAttribute("data-username");
        fullName = usernameLink.getText();
        email = emailLink.getText();

    }

    public String getFullName()
    {
        return fullName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUsername()
    {
        return username;
    }

}
