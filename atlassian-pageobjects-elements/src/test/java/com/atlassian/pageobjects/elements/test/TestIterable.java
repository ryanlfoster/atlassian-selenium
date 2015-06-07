package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.elements.CheckboxElement;
import com.atlassian.pageobjects.elements.MultiSelectElement;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.SelectElement;
import com.atlassian.pageobjects.elements.test.pageobjects.page.PageElementIterablePage;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Tests that {@code @ElementBy} works on {@code Iterable<PageElement>}
 * @since 2.1
 */
public class TestIterable extends AbstractPageElementBrowserTest
{
    private PageElementIterablePage elementsPage;

    @Before
    public void initFinder()
    {
        elementsPage = product.visit(PageElementIterablePage.class);
    }

    @Test
    public void testIterableNoElement()
    {
        Iterator<PageElement> elements = elementsPage.getNoElements().iterator();
        assertFalse(elements.hasNext());
    }

    @Test
    public void testIterableOneElement()
    {
        Iterator<PageElement> elements = elementsPage.getOneElements().iterator();
        assertTrue(elements.hasNext());
        assertEquals("valid1", elements.next().getText());
    }

    @Test
    public void testIterableTwoElements()
    {
        Iterator<PageElement> elements = elementsPage.getTwoElements().iterator();
        assertTrue(elements.hasNext());
        assertEquals("valid1", elements.next().getText());
        assertTrue(elements.hasNext());
        assertEquals("valid2", elements.next().getText());
    }

    @Test
    @IgnoreBrowser(Browser.HTMLUNIT_NOJS)
    public void testIterableThreeElements()
    {
        Iterator<PageElement> elements = elementsPage.getThreeElements().iterator();
        assertTrue(elements.hasNext());
        assertEquals("valid1", elements.next().getText());
        assertTrue(elements.hasNext());
        assertEquals("valid2", elements.next().getText());
        assertTrue(elements.hasNext());
        assertEquals("valid3", elements.next().getText());

        // We need to insert a fourth element dynamically
        elementsPage.addNewItem();
        
        elements = elementsPage.getThreeElements().iterator();
        assertTrue(elements.hasNext());
        assertEquals("valid1", elements.next().getText());
        assertTrue(elements.hasNext());
        assertEquals("valid2", elements.next().getText());
        assertTrue(elements.hasNext());
        assertEquals("valid3", elements.next().getText());
        assertTrue(elements.hasNext());
        assertEquals("valid4", elements.next().getText());
    }
    
    @Test
    public void testSubclasses()
    {
        CheckboxElement checkbox = (CheckboxElement) elementsPage.getOneCheckbox();
        assertEquals("CheckboxValue", checkbox.getValue());

        SelectElement select = (SelectElement) elementsPage.getOneSelect();
        assertEquals("option2_Value", select.getSelected().value());

        MultiSelectElement multiselect = (MultiSelectElement) elementsPage.getOneMultiSelect();
        assertEquals(2, multiselect.getSelected().size());
    }

    @Test
    public void testIterableCheckbox()
    {
        Iterator<CheckboxElement> elements = elementsPage.getTwoCheckboxes().iterator();
        assertTrue(elements.hasNext());
        assertEquals("koala", elements.next().getValue());
        assertTrue(elements.hasNext());
        assertEquals("kangaroo", elements.next().getValue());
    }

    @Test
    public void testIterableSelect()
    {
        Iterator<SelectElement> elements = elementsPage.getTwoSelects().iterator();
        assertTrue(elements.hasNext());
        assertEquals("tree", elements.next().getSelected().value());
        assertTrue(elements.hasNext());
        assertEquals("forest", elements.next().getSelected().value());
    }

    @Test
    public void testIterableMultiSelect()
    {
        Iterator<MultiSelectElement> elements = elementsPage.getTwoMultiSelects().iterator();
        assertTrue(elements.hasNext());
        assertEquals("tree", elements.next().getSelected().get(0).value());
        assertTrue(elements.hasNext());
        assertEquals("forest", elements.next().getSelected().get(0).value());
    }
}
