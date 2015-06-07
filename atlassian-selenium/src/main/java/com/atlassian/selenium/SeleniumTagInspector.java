package com.atlassian.selenium;

/**
 * Performs different checks over HTML tags located by Selenium locators.
 *
 * @since v1.21
 */
public final class SeleniumTagInspector extends AbstractSeleniumDriver
{
    private static final String INPUT_TAG_NAME = "input";

    public SeleniumTagInspector(final SeleniumClient client)
    {
        super(client);
    }

    /**
     * Get tag name for a given locator.
     *
     * @param locator Selenium locator to check
     * @return tag name of the located element, or <code>null</code>, if element is not present
     */
    public String getTagName(String locator)
    {
        if (!client.isElementPresent(locator))
        {
            return null;
        }
        String name = client.getEval("this.getTagName('" + locator + "')");
        if (name == null || name.equals("null"))
        {
            return null;
        }
        return name;
    }

    public boolean isInput(String locator)
    {
        return INPUT_TAG_NAME.equalsIgnoreCase(getTagName(locator));
    }
}
