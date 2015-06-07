package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.binder.WaitUntil;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.query.Poller;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestElementByInjection extends AbstractPageElementBrowserTest
{
    @Test
    public void testParentInjection()
    {
        ElementsPageWithParents elementsPage = product.visit(ElementsPageWithParents.class);

        assertTrue(elementsPage.parentList.isPresent());
        assertEquals("test4_parentList", elementsPage.parentList.getAttribute("id"));
        assertTrue(elementsPage.childList.isPresent());
        assertEquals("test4_childList", elementsPage.childList.getAttribute("id"));
        assertTrue(elementsPage.leafList.isPresent());
        assertEquals("test4_leafList", elementsPage.leafList.getAttribute("id"));
        assertEquals(3, Iterables.size(elementsPage.children));
        for (PageElement child : elementsPage.children)
        {
            assertTrue(child.isPresent());
            assertTrue(child.hasClass("test4_item"));
        }
    }

    @Test
    public void testParentInjectionWithNonExistingChildren()
    {
        ElementsPageWithNonExistingChildren elementsPage = product.visit(ElementsPageWithNonExistingChildren.class);

        assertTrue(elementsPage.parentList.isPresent());
        assertEquals("test4_parentList", elementsPage.parentList.getAttribute("id"));
        assertFalse(elementsPage.nonExistingTag.isPresent());
        assertFalse(elementsPage.nonExistingId.isPresent());
        assertTrue(Iterables.isEmpty(elementsPage.nonExistingClassName));
    }

    @Test
    public void testParentInjectionWithNonExistingParent()
    {
        try
        {
            product.visit(ElementsPageWithNonExistingParent.class);
        }
        catch(IllegalStateException expected)
        {
            assertTrue(expected.getMessage().contains("nonExistingParent"));
        }
    }

    @Test
    public void testParentInjectionWithParentOfWrongType()
    {
        try
        {
            product.visit(ElementsPageWithParentOfWrongType.class);
        }
        catch(IllegalStateException expected)
        {
            assertTrue(expected.getMessage().contains("childWithBadParents"));
            assertTrue(expected.getMessage().contains("iterableParent"));
        }
    }


    public static final class ElementsPageWithParents implements Page
    {
        @ElementBy(id = "test4_parentList")
        private PageElement parentList;

        @ElementBy(within = "parentList", id = "test4_childList")
        private PageElement childList;

        @ElementBy(within = "childList", tagName = "ul")
        private PageElement leafList;

        @ElementBy(within = "leafList", className = "test4_item")
        private Iterable<PageElement> children;


        public String getUrl()
        {
            return "/html/elements.html";
        }

        @WaitUntil
        private void waitUntilPresent()
        {
            Poller.waitUntilTrue(parentList.timed().isPresent());
        }
    }

    public static final class ElementsPageWithNonExistingChildren implements Page
    {
        @ElementBy(id = "test4_parentList")
        private PageElement parentList;

        @ElementBy(within = "parentList", tagName = "td")
        private PageElement nonExistingTag;

        @ElementBy(within = "parentList", id = "no-such-id")
        private PageElement nonExistingId;

        @ElementBy(within = "parentList", className = "no-such-class")
        private Iterable<PageElement> nonExistingClassName;


        public String getUrl()
        {
            return "/html/elements.html";
        }

        @WaitUntil
        private void waitUntilPresent()
        {
            Poller.waitUntilTrue(parentList.timed().isPresent());
        }
    }

    public static final class ElementsPageWithNonExistingParent implements Page
    {
        @ElementBy(id = "test4_parentList")
        private PageElement parentList;

        @ElementBy(within = "noSuchField", id = "test4_childList")
        private PageElement nonExistingParent;

        public String getUrl()
        {
            return "/html/elements.html";
        }

        @WaitUntil
        private void waitUntilPresent()
        {
            Poller.waitUntilTrue(parentList.timed().isPresent());
        }
    }

    public static final class ElementsPageWithParentOfWrongType implements Page
    {
        @ElementBy(id = "test4_parentList")
        private PageElement parentList;

        @ElementBy(within = "parentList", className = "test4_item")
        private Iterable<PageElement> iterableParent;

        @ElementBy(within = "iterableParent", tagName = "td")
        private PageElement childWithBadParents;

        public String getUrl()
        {
            return "/html/elements.html";
        }

        @WaitUntil
        private void waitUntilPresent()
        {
            Poller.waitUntilTrue(parentList.timed().isPresent());
        }
    }
}
