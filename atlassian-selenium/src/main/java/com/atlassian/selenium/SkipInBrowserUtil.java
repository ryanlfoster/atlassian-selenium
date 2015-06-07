package com.atlassian.selenium;

import java.util.ArrayList;
import java.util.List;

public class SkipInBrowserUtil {
    public static List<Class> filter(Browser browser, List<Class> list)
    {
        List<Class> filteredList = new ArrayList<Class>(list.size());
        for (Class<?> c : list)
        {
            //Only added to the list if its not marked as to being to skip
            if(!skip(browser, c))
            {
                filteredList.add(c);
            }
        }
        return filteredList;
    }

    public static boolean skip(Browser browser, Class<?> c)
    {
        SkipInBrowser skipAnnotation = c.getAnnotation(SkipInBrowser.class);
        return (skipAnnotation != null) && arrayContains(skipAnnotation.browsers(), browser);
    }
    
    static boolean arrayContains(Browser[] browsers, Browser searchBrowser)
    {
        for (Browser browser : browsers) {
            if(searchBrowser == browser)
            {
                return true;
            }
            
        }
        return false;
    }
    
}
