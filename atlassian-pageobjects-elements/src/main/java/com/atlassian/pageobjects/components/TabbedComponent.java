package com.atlassian.pageobjects.components;


import com.atlassian.pageobjects.elements.PageElement;

import java.util.List;

/**
 * Represents a components that has multiple tabs with content.
 */
public interface TabbedComponent
{
    /**
     * Returns element that represents the selected tab.
     */
    PageElement selectedTab();

    /**
     * Returns element that represents the selected tab's view.
     */
    PageElement selectedView();

    /**
     * Returns all elements that represent the tabs.
     */
    List<PageElement> tabs();

    /**
     * Open's the given tab's view.
     */
    PageElement openTab(PageElement tab);
}
