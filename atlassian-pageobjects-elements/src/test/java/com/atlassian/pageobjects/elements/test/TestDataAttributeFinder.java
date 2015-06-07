package com.atlassian.pageobjects.elements.test;

import com.atlassian.pageobjects.elements.GlobalElementFinder;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import com.atlassian.pageobjects.elements.test.pageobjects.page.DataAttributePage;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.Map;

import static com.atlassian.pageobjects.elements.DataAttributeFinder.query;
import static com.atlassian.pageobjects.elements.query.Poller.waitUntilEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link com.atlassian.webdriver.utils.by.ByDataAttribute}.
 *
 * @since 2.1
 */
public class TestDataAttributeFinder extends AbstractPageElementBrowserTest
{

    private PageElementFinder elementFinder;

    @Before
    public void init()
    {
        product.visit(DataAttributePage.class);
        elementFinder = product.getPageBinder().bind(GlobalElementFinder.class);
    }


    @Test
    public void testRegularQuery()
    {
        PageElement main = elementFinder.find(By.id("mainContainer"));
        assertTrue(query(main).hasDataAttribute("type", "main container"));
        assertNull(query(main).getDataAttribute("rubbish"));
        final Map<String,String> expectedOrderings = ImmutableMap.of(
                "1", "a",
                "2", "b",
                "3", "c",
                "4", "d"
        );
        for (PageElement li : main.findAll(By.tagName("li")))
        {
            final String ordering = query(li).getDataAttribute("ordering");
            assertEquals(li.getText(), expectedOrderings.get(ordering));
        }
    }

    @Test
    public void testTimedQuery()
    {
        PageElement main = elementFinder.find(By.id("mainContainer"));
        Poller.waitUntilTrue(query(main).timed().hasDataAttribute("type", "main container"));
        final Map<String,String> expectedOrderings = ImmutableMap.of(
                "a", "1",
                "b", "2",
                "c", "3",
                "d", "4"
        );
        for (PageElement li : main.findAll(By.tagName("li")))
        {
            waitUntilEquals(expectedOrderings.get(li.getText()), query(li).timed().getDataAttribute("ordering"));
        }
    }

}
