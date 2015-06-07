package com.atlassian.pageobjects.elements.test.pageobjects.page;

import javax.inject.Inject;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.elements.CheckboxElement;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.MultiSelectElement;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.SelectElement;

/**
 * Represents iterable.html
 * @since 2.1
 */
public class PageElementIterablePage implements Page
{
    @Inject
    PageElementFinder elementFinder;

    // Test 0, 1, several elements
    @ElementBy(className = "no-element")
    Iterable<PageElement> noElements;

    @ElementBy(className = "one-element")
    Iterable<PageElement> oneElements;

    @ElementBy(className = "two-elements")
    Iterable<PageElement> twoElements;

    @ElementBy(className = "three-elements")
    Iterable<PageElement> threeElements;
    
    @ElementBy(id = "add-new-item")
    PageElement addNewItemButton;

    // Test subclasses for 1 element
    @ElementBy(className = "one-checkbox", pageElementClass = CheckboxElement.class)
    PageElement oneCheckbox;

    @ElementBy(className = "one-select", pageElementClass = SelectElement.class)
    PageElement oneSelect;

    @ElementBy(className = "one-multiselect", pageElementClass = MultiSelectElement.class)
    PageElement oneMultiSelect;

    // Test subclasses for an iterator
    @ElementBy(className = "two-checkbox", pageElementClass = CheckboxElement.class)
    Iterable<CheckboxElement> twoCheckboxes;

    @ElementBy(className = "two-select", pageElementClass = SelectElement.class)
    Iterable<SelectElement> twoSelects;

    @ElementBy(className = "two-multiselect", pageElementClass = MultiSelectElement.class)
    Iterable<MultiSelectElement> twoMultiSelects;

    public String getUrl()
    {
        return "/html/iterable.html";
    }
    
    /**
     * Adds a new item which will appear in 'three-elements'
     */
    public void addNewItem()
    {
        addNewItemButton.click();
    }

    public PageElementFinder getElementFinder()
    {
        return elementFinder;
    }

    public Iterable<PageElement> getNoElements()
    {
        return noElements;
    }

    public Iterable<PageElement> getOneElements()
    {
        return oneElements;
    }

    public Iterable<PageElement> getTwoElements()
    {
        return twoElements;
    }

    public Iterable<PageElement> getThreeElements()
    {
        return threeElements;
    }

    public PageElement getOneCheckbox()
    {
        return oneCheckbox;
    }

    public PageElement getOneSelect()
    {
        return oneSelect;
    }

    public PageElement getOneMultiSelect()
    {
        return oneMultiSelect;
    }

    public Iterable<CheckboxElement> getTwoCheckboxes()
    {
        return twoCheckboxes;
    }

    public Iterable<SelectElement> getTwoSelects()
    {
        return twoSelects;
    }

    public Iterable<MultiSelectElement> getTwoMultiSelects()
    {
        return twoMultiSelects;
    }
   
    
}
