package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.search.PageElementSearch;
import com.atlassian.pageobjects.internal.elements.search.GlobalPageElementSearch;
import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Guice module that adds bindings for classes required by the elements
 */
public class ElementModule implements Module
{
    public void configure(Binder binder)
    {
        binder.bind(ElementByPostInjectionProcessor.class);
        binder.bind(PageElementFinder.class).to(GlobalElementFinder.class);
        binder.bind(PageElementSearch.class).to(GlobalPageElementSearch.class);
    }
}
