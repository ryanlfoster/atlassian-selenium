package com.atlassian.pageobjects.binder;

import com.atlassian.pageobjects.Tester;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
*
*/
class SetTester implements Tester
{
    private final Set<Object> injectables = new HashSet<Object>();

    public Iterable<Object> getInjectables()
    {
        return injectables;
    }

    public void gotoUrl(String url)
    {

    }

    public SetTester add(Object value)
    {
        injectables.add(value);
        return this;
    }
}
