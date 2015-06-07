package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.elements.CheckboxElement;
import com.atlassian.pageobjects.elements.GlobalElementFinder;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.test.pageobjects.page.ElementsPage;
import org.junit.Test;
import org.openqa.selenium.By;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestCheckboxElement extends AbstractPageElementBrowserTest
{
    @Test
    public void testCheck()
    {
        PageElementFinder elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
        product.visit(ElementsPage.class);

        CheckboxElement element1 = elementFinder.find(By.id("test8_checkbox1"), CheckboxElement.class);
        CheckboxElement element2 = elementFinder.find(By.id("test8_checkbox2"), CheckboxElement.class);

        assertFalse(element1.isSelected());
        assertTrue(element2.isSelected());

        element1.check();
        element2.check();

        assertTrue(element1.isSelected());
        assertTrue(element2.isSelected());
    }

    @Test
    public void testUncheck()
    {
        PageElementFinder elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
        product.visit(ElementsPage.class);

        CheckboxElement element1 = elementFinder.find(By.id("test8_checkbox1"), CheckboxElement.class);
        CheckboxElement element2 = elementFinder.find(By.id("test8_checkbox2"), CheckboxElement.class);

        assertFalse(element1.isSelected());
        assertTrue(element2.isSelected());

        element1.uncheck();
        element2.uncheck();

        assertFalse(element1.isSelected());
        assertFalse(element2.isSelected());
    }
}
