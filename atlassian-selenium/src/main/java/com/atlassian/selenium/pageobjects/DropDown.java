package com.atlassian.selenium.pageobjects;

import com.atlassian.selenium.SeleniumClient;

public class DropDown extends PageElement {
    public DropDown(String locator, SeleniumClient client)
    {
        super(locator, client);
    }

    public String getSelectedId()
    {
        return client.getSelectedId(getLocator());
    }

    public String[] getSelectedIds()
    {
        return client.getSelectedIds(getLocator());
    }

    public String getSelectedIndex()
    {
        return client.getSelectedIndex(getLocator());
    }

    public String[] getSelectedIndexes()
    {
        return client.getSelectedIndexes(getLocator());
    }

    public String getSelectedLabel()
    {
        return client.getSelectedLabel(getLocator());
    }

    public String[] getSelectedLabels()
    {
        return client.getSelectedLabels(getLocator());
    }

    public String getSelectedValue()
    {
        return client.getSelectedValue(getLocator());
    }

    public String[] getSelectedValues()
    {
        return client.getSelectedValues(getLocator());
    }

    public String[] getSelectOptions(java.lang.String selectLocator)
    {
        return client.getSelectOptions(getLocator());
    }

    public boolean isSomethingSelected()
    {
        return client.isSomethingSelected(getLocator());
    }
}
