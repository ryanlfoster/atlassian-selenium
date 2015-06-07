package com.atlassian.webdriver.utils;

import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import static org.junit.Assert.*;

public class WebDriverUtilTest
{
    @Test
    public void testCreateCapabilitiesFromStringOneItem() throws Exception
    {
        assertEquals("osx", WebDriverUtil.createCapabilitiesFromString("so=osx").getCapability("so"));
    }

    @Test
    public void testCreateCapabilitiesFromStringMoreThanOneItem() throws Exception
    {
        DesiredCapabilities capabilities = WebDriverUtil.createCapabilitiesFromString("so=osx;browser=safari");
        assertEquals("osx", capabilities.getCapability("so"));
        assertEquals("safari", capabilities.getCapability("browser"));
    }

    @Test
    public void testCreateCapabilitiesFromStringDuplicatedItems() throws Exception
    {
        DesiredCapabilities capabilities = WebDriverUtil.createCapabilitiesFromString("so=osx;browser=safari;browser=firefox");
        assertEquals("osx", capabilities.getCapability("so"));
        assertEquals("firefox", capabilities.getCapability("browser")); // it will pick up the latest
    }

    @Test
    public void testCreateCapabilitiesFromNullString() throws Exception
    {
        DesiredCapabilities capabilities = WebDriverUtil.createCapabilitiesFromString(null);
        assertEquals(0, capabilities.asMap().size());
    }

    @Test
    public void testCreateCapabilitiesFromEmptyString() throws Exception
    {
        DesiredCapabilities capabilities = WebDriverUtil.createCapabilitiesFromString("");
        assertEquals(0, capabilities.asMap().size());
    }
}
