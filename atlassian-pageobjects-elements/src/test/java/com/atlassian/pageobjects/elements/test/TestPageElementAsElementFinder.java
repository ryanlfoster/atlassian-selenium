package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.elements.GlobalElementFinder;
import com.atlassian.pageobjects.elements.MultiSelectElement;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.SelectElement;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import com.atlassian.pageobjects.elements.timeout.DefaultTimeouts;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPageElementAsElementFinder extends AbstractPageElementBrowserTest
{
    private PageElementFinder elementFinder;

    @Before
    public void initFinder()
    {
        elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
    }

    @Test
    public void testFind()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("test4_childList"));
        PageElement existingChild = parent.find(By.tagName("ul"));
        assertTrue(existingChild.isPresent());
        assertEquals("test4_leafList", existingChild.getAttribute("id"));

        PageElement notExistingChild = parent.find(By.tagName("a"));
        assertFalse(notExistingChild.isPresent());
    }

    @Test
    public void testFindCustomType()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("select-parent-div"));
        SelectElement select = parent.find(By.className("nested-select-marker"), SelectElement.class);
        assertTrue(select.isPresent());
        assertEquals("nested-select", select.getAttribute("id"));
        assertEquals(4, select.getAllOptions().size());

        MultiSelectElement multiSelect = parent.find(By.className("nested-select-marker"), MultiSelectElement.class);
        assertTrue(multiSelect.isPresent());
        assertEquals("nested-select", multiSelect.getAttribute("id"));
        assertEquals(4, multiSelect.getAllOptions().size());
    }

    @Test
    public void testFindWithCustomTimeout()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("test4_childList"));
        PageElement child = parent.find(By.tagName("ul"), TimeoutType.PAGE_LOAD);
        assertTrue(child.isPresent());
        assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, child.timed().isPresent().defaultTimeout());
    }

    @Test
    public void testFindWithCustomTypeAndTimeout()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("select-parent-div"));
        SelectElement select = parent.find(By.className("nested-select-marker"), SelectElement.class, TimeoutType.PAGE_LOAD);
        assertTrue(select.isPresent());
        assertEquals("nested-select", select.getAttribute("id"));
        assertEquals(4, select.getAllOptions().size());
        assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, select.timed().isPresent().defaultTimeout());
    }

    @Test
    public void testFindAll()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("test4_childList"));
        List<PageElement> items = parent.findAll(By.className("test4_item"));
        assertEquals(3, items.size());
        assertEquals("Item 1", items.get(0).getText());
        assertEquals("Item 2", items.get(1).getText());
        assertEquals("Item 3", items.get(2).getText());
    }

    @Test
    public void testFindAllWithCustomType()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("select-parent-div"));
        List<SelectElement> singleSelects = parent.findAll(By.tagName("select"), SelectElement.class);

        assertEquals(2, singleSelects.size());
        assertTrue(singleSelects.get(0).isPresent());
        assertEquals("nested-select", singleSelects.get(0).getAttribute("id"));
        assertTrue(singleSelects.get(1).isPresent());
        assertEquals("nested-select2", singleSelects.get(1).getAttribute("id"));

        List<MultiSelectElement> multiSelects = parent.findAll(By.tagName("select"), MultiSelectElement.class);

        assertEquals(2, multiSelects.size());
        assertTrue(multiSelects.get(0).isPresent());
        assertEquals("nested-select", multiSelects.get(0).getAttribute("id"));
        assertTrue(multiSelects.get(1).isPresent());
        assertEquals("nested-select2", multiSelects.get(1).getAttribute("id"));

        // TODO checkboxes should be tested as well
    }

    @Test
    public void testFindAllWithCustomTimeout()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("test4_childList"));
        List<PageElement> items = parent.findAll(By.className("li"), TimeoutType.PAGE_LOAD);
        for (PageElement element : items)
        {
            assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, element.timed().isPresent().defaultTimeout());
        }
    }

    @Test
    public void testFindAllWithCustomTypeAndTimeout()
    {
        product.visit(ElementsPage.class);

        PageElement parent = elementFinder.find(By.id("select-parent-div"));
        List<SelectElement> singleSelects = parent.findAll(By.tagName("select"), SelectElement.class, TimeoutType.PAGE_LOAD);
        for (PageElement select : singleSelects)
        {
            assertTrue(select.isPresent());
            assertEquals(DefaultTimeouts.DEFAULT_PAGE_LOAD, select.timed().isPresent().defaultTimeout());
        }
    }
}
