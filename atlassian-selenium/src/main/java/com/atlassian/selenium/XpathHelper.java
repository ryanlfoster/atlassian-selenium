package com.atlassian.selenium;

import com.thoughtworks.selenium.SeleniumException;

import java.util.List;
import java.util.LinkedList;

public class XpathHelper
{
    public static void clickButtonWithClass(String className, SeleniumClient client)
    {
        clickElementWithClass("button", className, client);
    }

    public static void clickElementWithClass(String elementName, String className, SeleniumClient client)
    {
        client.click("//" + elementWithClass(elementName, className));
    }

    public static String elementWithClass(String className)
    {
        return elementWithClass("*", className);
    }

    public static String elementWithClass(String elementName, String className)
    {
        return elementName + "[contains(@class, '" + className + "')]";
    }

    public static String[] getAllMatchingAttributes(String query, String attribute, SeleniumClient client)
    {
        List<String> matches = new LinkedList<String>();
        try
        {
            int i = 1;
            for(String attr = getAttribute(client, query, attribute, i++);
                attr != null;
                attr = getAttribute(client, query, attribute, i++))
            {
                matches.add(attr);
            }
        }
        catch (SeleniumException se)
        {
           // This exception will be thrown to break the for loop
           //  the implementation of xpathcount seems to return too many matches.
            // So using it to work out how many times to loop was problematic

        }
        return matches.toArray(new String[matches.size()]);
    }

    private static String getAttribute(SeleniumClient client, String query, String attribute, int i)
    {
        return client.getAttribute(query + "[" + i + "]/@" + attribute);
    }
}

