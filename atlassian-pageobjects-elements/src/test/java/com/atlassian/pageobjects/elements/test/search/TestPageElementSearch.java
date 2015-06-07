package com.atlassian.pageobjects.elements.test.search;

import com.atlassian.pageobjects.elements.CheckboxElement;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.PageElements;
import com.atlassian.pageobjects.elements.WebDriverElement;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import com.atlassian.pageobjects.elements.search.AnyQuery;
import com.atlassian.pageobjects.elements.search.PageElementQuery;
import com.atlassian.pageobjects.elements.search.PageElementSearch;
import com.atlassian.pageobjects.elements.search.SearchQuery;
import com.atlassian.pageobjects.elements.test.AbstractPageElementBrowserTest;
import com.atlassian.pageobjects.elements.test.pageobjects.page.PageElementSearchPage;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import javax.inject.Inject;

import static com.atlassian.pageobjects.elements.PageElements.getDataAttribute;
import static com.atlassian.pageobjects.elements.PageElements.hasClass;
import static com.atlassian.pageobjects.elements.PageElements.hasDataAttribute;
import static com.atlassian.pageobjects.elements.query.Poller.waitUntil;
import static com.atlassian.pageobjects.elements.query.Poller.waitUntilTrue;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.asMatcherOf;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.isChecked;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.withAttribute;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.withClass;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.withDataAttribute;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.withId;
import static com.atlassian.pageobjects.elements.testing.PageElementMatchers.withText;
import static com.atlassian.webdriver.testing.matcher.MatcherPredicate.withMatcher;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.tagName;

@SuppressWarnings("unchecked")
public class TestPageElementSearch extends AbstractPageElementBrowserTest
{
    @Inject
    private PageElementSearch page;

    @Inject
    private PageElementFinder elementFinder;

    private PageElementSearchPage searchPage;

    @Before
    public void goToElementSearchPage()
    {
        searchPage = product.visit(PageElementSearchPage.class);
    }

    @Test
    public void shouldFindSinglePageElementById()
    {
        PageElement result = page.search()
                .by(id("single-element"))
                .first();

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("single-element", result.getAttribute("id"));
    }

    @Test
    public void shouldFindSinglePageElementByIdAsIterable()
    {
        assertResult(page.search().by(id("single-element")), contains(withAttribute("id", "single-element")));
    }

    @Test
    public void shouldReturnEmptyImmediatelyWhenSearchingFromNotExistingRoot()
    {
        Iterable<PageElement> result = elementFinder.find(By.id("no-such-id")).search()
                .by(id("nested-id"))
                .by(tagName("ul"))
                .by(tagName("li")).filter(hasDataAttribute("pick-me"))
                .now();

        assertThat(result, emptyIterable());
    }

    @Test
    public void shouldFindRowsWithFilter()
    {
        PageElementQuery<PageElement> result = page.search()
                .by(id("table-list"))
                .by(id("the-table"))
                .by(tagName("tr"), hasClass("even-row"));

        assertResult(result, contains(
                withDataAttribute("name", "Even Row 1"),
                withDataAttribute("name", "Even Row 2"),
                withDataAttribute("name", "Even Row 3")
        ));
    }

    @Test
    public void shouldApplyFlatMap()
    {
        AnyQuery<String> result = page.search()
                .by(id("table-list"))
                .by(id("the-table"))
                .by(tagName("tr"), hasClass("even-row"))
                .flatMap(PageElements.getCssClasses());

        assertResult(result, contains("even-row", "two", "even-row", "four", "even-row", "six"));
    }

    @Test
    public void shouldFindRowsFromTableRoot()
    {
        Iterable<PageElement> result = searchPage.getTableRoot().search()
                .by(id("the-table"))
                .by(tagName("tr"))
                .now();

        assertThat(result, Matchers.<PageElement>iterableWithSize(7));
    }

    @Test
    public void shouldFindRowsWithFilterAndTransformOnResult()
    {
        AnyQuery<String> result = page.search()
                .by(id("table-list"))
                .by(id("the-table"))
                .by(tagName("tr"))
                .filter(hasClass("uneven-row"))
                .map(getDataAttribute("name"));

        assertResult(result, contains("Uneven Row 1", "Uneven Row 2", "Uneven Row 3"));
    }


    @Test
    public void shouldFindRowsWithFilterOnResultUsingMatcher()
    {
        AnyQuery<String> result = page.search()
                .by(id("table-list"))
                .by(id("the-table"))
                .by(tagName("tr"))
                .filter(withMatcher(withClass("uneven-row")))
                .map(getDataAttribute("name"));

        assertResult(result, contains("Uneven Row 1", "Uneven Row 2", "Uneven Row 3"));
    }

    @Test
    public void shouldFindRowsWithFilterAndBindOnResult()
    {
        AnyQuery<RowWrapper> result = page.search()
                .by(id("table-list"))
                .by(id("the-table"))
                .by(tagName("tr"))
                .filter(hasClass("even-row"))
                .bindTo(RowWrapper.class);

        assertResult(result, contains(
                rowWithName("Even Row 1"),
                rowWithName("Even Row 2"),
                rowWithName("Even Row 3")));
    }

    @Test
    public void findFirstNestedElement()
    {
        PageElementQuery<PageElement> query = page.search()
                .by(id("parent-1"))
                .by(className("parent-2-class")).filter(hasDataAttribute("find-me"))
                .by(tagName("ul"))
                .by(tagName("li")).filter(withMatcher(withDataAttribute("pick-me", "yes")));

        PageElement element = findFirst(query);
        assertTrue(element.isPresent());
        assertEquals("Yes-1", element.getText());
    }

    @Test
    public void findMergeMultipleDomBranches()
    {
        PageElementQuery<PageElement> result = page.search()
                .by(id("parent-1"))
                .by(className("parent-2-class"))
                .by(tagName("ul"))
                .by(tagName("li"));

        assertResultStrict(result, Matchers.<PageElement>iterableWithSize(5));
        assertResultStrict(result, hasItems(
                withText("WRONG!"),
                withText("No"),
                withText("Yes-1"),
                withText("Yes-2"))
        );
    }

    @Test
    public void shouldWaitForSearchResultsToMatch() {
        TimedQuery<Iterable<PageElement>> result = searchPage.getAsyncRoot().search()
                .by(className("async-parent-level-2-class"))
                .by(tagName("li"), hasDataAttribute("state", "2"))
                .timed();
        waitUntil(result, emptyIterable());

        // switch to state-2
        searchPage.clickAsyncButton(2);
        waitUntil(result, Matchers.<PageElement>iterableWithSize(2));

        // reset back to state-1
        searchPage.clickAsyncButton(1);
        waitUntil(result, emptyIterable());
    }

    @Test
    public void shouldAssignCustomTimeoutType()
    {
        PageElementQuery<PageElement> result = page.search()
                .by(id("table-list"))
                .by(id("the-table"))
                .by(tagName("tr"), hasClass("even-row"))
                .withTimeout(TimeoutType.PAGE_LOAD);

        assertResultStrict(result, everyItem(hasTimeoutType(TimeoutType.PAGE_LOAD)));
    }

    @Test
    public void shouldReturnResultAsCheckboxes()
    {
        PageElementQuery<CheckboxElement> result = page.search()
                .by(id("checkbox-parent"))
                .by(tagName("input"))
                .as(CheckboxElement.class)
                .filter(withMatcher(not(isChecked())));

        // only unchecked checkboxes should be returned
        assertResult(result, contains(
                asMatcherOf(CheckboxElement.class, withId("checkbox-2")),
                asMatcherOf(CheckboxElement.class, withId("checkbox-3"))
        ));
    }

    private static <R> void assertResult(SearchQuery<R, ?> result, Matcher<Iterable<? extends R>> matcher)
    {
        waitUntil(result.timed(), matcher);
        assertThat(result.get(), matcher);
        assertThat(result.now(), matcher);
    }

    private static <R> void assertResultStrict(SearchQuery<R, ?> result, Matcher<Iterable<R>> matcher)
    {
        waitUntil(result.timed(), matcher);
        assertThat(result.get(), matcher);
        assertThat(result.now(), matcher);
    }

    private static PageElement findFirst(PageElementQuery<?> query)
    {
        waitUntilTrue(query.hasResult());

        return query.first();
    }

    private static Matcher<PageElement> hasTimeoutType(TimeoutType expectedTimeout)
    {
        return new FeatureMatcher<PageElement, TimeoutType>(is(expectedTimeout), "timeout type", "timeoutType")
        {
            @Override
            protected TimeoutType featureValueOf(PageElement pageElement)
            {
                return WebDriverElement.class.cast(pageElement).getDefaultTimeout();
            }
        };
    }

    private static Matcher<RowWrapper> rowWithName(final String name)
    {
        return new FeatureMatcher<RowWrapper, String>(equalTo(name), "row name", "name")
        {
            @Override
            protected String featureValueOf(RowWrapper rowWrapper)
            {
                return rowWrapper.getName();
            }
        };
    }

    public static class RowWrapper
    {
        private final PageElement row;

        public RowWrapper(PageElement element)
        {
            this.row = element;
        }

        String getName()
        {
            return getDataAttribute("name").apply(row);
        }
    }
}
