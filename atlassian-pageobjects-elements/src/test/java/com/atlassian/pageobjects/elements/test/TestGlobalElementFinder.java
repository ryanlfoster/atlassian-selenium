package com.atlassian.pageobjects.elements.test;


import com.atlassian.pageobjects.elements.GlobalElementFinder;
import com.atlassian.pageobjects.elements.MultiSelectElement;
import com.atlassian.pageobjects.elements.Options;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.SelectElement;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import com.atlassian.pageobjects.elements.test.pageobjects.page.SelectElementPage;
import com.atlassian.pageobjects.elements.timeout.DefaultTimeouts;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestGlobalElementFinder extends AbstractPageElementBrowserTest
{
    PageElementFinder elementFinder;

    @Before
    public void initFinder()
    {
        elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
    }

    @Test
    public void testFind()
    {
        product.visit(ElementsPage.class);

        PageElement existing = elementFinder.find(By.id("test4_childList"));
        assertTrue(existing.isPresent());

        PageElement notExisting = elementFinder.find(By.id("not-really"));
        assertFalse(notExisting.isPresent());
    }

    @Test
    public void testFindCustomType()
    {
        product.visit(ElementsPage.class);

        SelectElement select = elementFinder.find(By.id("awesome-select"), SelectElement.class);
        assertTrue(select.isPresent());
        assertEquals(4, select.getAllOptions().size());
    }

    @Test
    public void testFindWithCustomTimeout()
    {
        product.visit(ElementsPage.class);

        PageElement withTimeout = elementFinder.find(By.id("test4_childList"), TimeoutType.PAGE_LOAD);
        assertTrue(withTimeout.isPresent());
        assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, withTimeout.timed().isPresent().defaultTimeout());
    }

    @Test
    public void testFindWithCustomTypeAndTimeout()
    {
        product.visit(ElementsPage.class);

        SelectElement select = elementFinder.find(By.id("awesome-select"), SelectElement.class, TimeoutType.PAGE_LOAD);
        assertTrue(select.isPresent());
        assertEquals(4, select.getAllOptions().size());
        assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, select.timed().isPresent().defaultTimeout());
    }

    @Test
    public void testFindAll()
    {
        product.visit(ElementsPage.class);

        List<PageElement> items = elementFinder.findAll(By.className("test4_item"));
        assertEquals(3, items.size());
        assertEquals("Item 1", items.get(0).getText());
        assertEquals("Item 2", items.get(1).getText());
        assertEquals("Item 3", items.get(2).getText());
    }

    @Test
    public void testFindAllWithCustomClass()
    {
        product.visit(SelectElementPage.class);

        List<SelectElement> singleSelects = elementFinder.findAll(By.tagName("select"), SelectElement.class);
        assertEquals(6, singleSelects.size()); // Collection will contain select and multiselect

        SelectElement singleSelect = singleSelects.get(0);
        assertEquals("Option1_Text", singleSelect.getSelected().text());
        singleSelect.select(Options.text("Option2_Text"));
        assertEquals("Option2_Text", singleSelect.getSelected().text());

        List<MultiSelectElement> multiSelects = elementFinder.findAll(By.tagName("select"), MultiSelectElement.class);
        assertEquals(6, multiSelects.size()); // Collection will contain select and multiselect

        MultiSelectElement multiSelect = multiSelects.get(3);
        assertEquals(0, multiSelect.getSelected().size());
        multiSelect.select(Options.text("Option2_Text"));
        assertEquals(1, multiSelect.getSelected().size());
    }

    @Test
    public void testFindAllWithCustomTimeout()
    {
        product.visit(ElementsPage.class);

        List<PageElement> items = elementFinder.findAll(By.className("test4_item"), TimeoutType.PAGE_LOAD);
        for (PageElement element : items)
        {
            assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, element.timed().isPresent().defaultTimeout());
        }
    }

    @Test
    public void testFindAllWithCustomTypeAndTimeout()
    {
        product.visit(SelectElementPage.class);

        List<SelectElement> singleSelects = elementFinder.findAll(By.tagName("select"), SelectElement.class, TimeoutType.PAGE_LOAD);
        for (PageElement select : singleSelects)
        {
            assertTrue(select.isPresent());
            assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, select.timed().isPresent().defaultTimeout());
        }
    }
}
