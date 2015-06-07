package com.atlassian.pageobjects.elements.search;

import com.atlassian.annotations.PublicApi;

import javax.annotation.Nonnull;

/**
 * <h3>Search Overview</h3>
 * Access point to advanced search within the context of the whole page, or a particular
 * {@link com.atlassian.pageobjects.elements.PageElement DOM element}. The search query object provided by this method
 * allows for issuing a sophisticated, multi-level search across the DOM and returning the result in a desired form.
 * The following paragraphs discuss the search query API.
 *
 * <h3>Search query</h3>
 * The {@code SearchQuery} object is essentially a sequence of search/transform/filter steps that are applied
 * sequentially, starting with the root context (either the entire page, or a specific DOM element, depending on what
 * this {@code PageElementSearch} refers to). Each single step will be applied to <i>all parents</i> that were found
 * by the previous steps, resulting in a new list of elements that will be roots for the next step, and so on until the
 * result is requested.
 *
 * <p/>
 * Each search query starts off as an instance of {@link PageElementQuery} - with element type
 * {@link com.atlassian.pageobjects.elements.PageElement}, however they may be converted into a more generic
 * {@link AnyQuery} by transforming/mapping the results, which limits the applicable operations to only those
 * applicable to any object (see {@link AnyQuery} and {@link SearchQuery}).
 *
 * <h4>Search Steps</h4>
 * Each search step <i>must</i> be specified using a {@link org.openqa.selenium.By by locator}, using one of the
 * {@code by()} methods on {@link PageElementQuery}. The search steps are only applicable if the element type of the
 * query is {@link com.atlassian.pageobjects.elements.PageElement}.
 *
 * <h4>Filter Steps</h4>
 * Each query supports {@code filters} to further narrow down the results. Filters can be specified by
 * {@link com.google.common.base.Predicate Guava predicates} - the
 * {@link com.atlassian.pageobjects.elements.PageElements} class contains a collection of common predicates to use.
 * Clients can also use {@link com.atlassian.webdriver.testing.matcher.MatcherPredicate} to convert {@code Hamcrest}
 * matchers into {@code Guava} predicates. The filter steps are applicable to any object, however filter steps preserve
 * the element type of the query.
 *
 * <h4>Mapping Steps</h4>
 * Each query supports {@code mappers} to map or flat map query results into some other objects (or collections of
 * thereof). Map steps are applicable to any object, however they do not preserve element type, and therefore can turn
 * {@link PageElementQuery} into a more generic {@link AnyQuery}. Some specialized page-element specific mapping steps
 * preserve the element type (see {@link PageElementQuery}).
 *
 * <h3>Query results</h3>
 * The search query object offers multiple methods to obtain the results, depending on what the client wants to achieve:
 * <ul>
 *     <li>{@link SearchQuery#get()} - get the list of resulting objects now. Note that search query thus implements a
 *     specific Guava supplier, which makes it usable in all contexts where supplier can be used</li>
 *     <li>{@link SearchQuery#now()}} - synonym to {@link SearchQuery#get()}</li>
 *     <li>{@link SearchQuery#first()} - obtain the first element of the result, or {@code null} if the results is empty
 *     <li>{@link SearchQuery#hasResult()} - obtain a {@link com.atlassian.pageobjects.elements.query.TimedCondition}
 *     that will periodically execute the search and allow the client to wait until there is any result/there is no
 *     result matching the query, with a desired timeout - using
 *     {@link com.atlassian.pageobjects.elements.query.Poller}</li>
 *     <li>{@link SearchQuery#timed()} - obtain a {@link com.atlassian.pageobjects.elements.query.TimedQuery} that will
 *     periodically execute the search and allow the client to wait until the result matches its expectations, with a
 *     desired timeout - using {@link com.atlassian.pageobjects.elements.query.Poller}. This is <b>the most recommended
 *     way</b> of obtaining the search query results</li>
 * </ul>
 *
 * <h4>Example</h4>
 * The following method (presumably in a page object) searches for all the {@code span} elements in a drop-down that
 * represent users, and subsequently retrieves the username value from those elements, returning them as a
 * {@link com.atlassian.pageobjects.elements.query.TimedQuery}. The subsequent call to {@code Poller} (presumably in a
 * test) executes timed assertion that validates that the list contains 3 usernames ("alice", "bob" and "charlie")
 * within the timeout of an AJAX load (expecting the hypothetical drop-down to issue an AJAX call to populate its
 * contents).
 * <p/>
 * <pre>
 * {@code
 * public TimedQuery<String> getUsernames() {
 *    return mySearch.search()
 *              .by(By.id("user-dropdown"))
 *              .by(By.tagName("li")) // can also use Elements.TAG_LI here
 *                  .filter(isVisible()) // from PageElements
 *                  .filter(hasClass("user-element")) // from PageElements
 *              .by(By.tagName("span"), hasDataAttribute("username")) // from PageElements, check for "data-username" attribute
 *              .withTimeout(TimeoutType.AJAX_ACTION)
 *              .transform(getDataAttribute("username")) // from PageElements
 *              .timed()
 * }
 *
 * // ...
 * Poller.waitUntil(myDropdown.getUsernames(), Matchers.contains("alice", "bob", "charlie"));
 * }
 * </pre>
 *
 * @since 2.3
 *
 * @see SearchQuery
 * @see AnyQuery
 * @see PageElementQuery
 * @see DefaultQuery
 */
@PublicApi
public interface PageElementSearch
{
    /**
     * @return a new search query object that allows execute the search in the current context
     */
    @Nonnull
    DefaultQuery search();
}
