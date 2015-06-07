package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.query.TimedCondition;
import com.atlassian.pageobjects.elements.query.TimedQuery;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Represents an HTML element that is expected in the DOM of a page, all queries return TimedQueries.
 *
 */
public interface TimedElement
{
    /**
     * Query representing the existence of this element on a page.
     *
     * @return TimedQuery that true if element is present on the page, false if element is not visible or timeout
     * expires.
     */
    TimedCondition isPresent();

    /**
     * Query representing visibility of this element on a page.
     *
     * @return TimedQuery that returns true if element is visible on the page, false if element is not visible 
     * or timeout expires.
     */
    TimedCondition isVisible();

    /**
     * Query representing whether this element is enabled on a page.
     *
     * @return TimedQuery that returns true if element is enabled on the page, false if element is disabled or
     * timeout expires.
     */
    TimedCondition isEnabled();

    /**
     * Query representing whether this element is selected on a page.
     *
     * @return TimedQuery that returns true if element is selected on the page, false if element is not selected or
     * timeout expires.
     */
    TimedCondition isSelected();

    /**
     * Query for the "id" attribute of this element. The query will return {@code null} if the element is not present,
     * or the "id" attribute is not specified
     *
     * @return query for the "id" attribute of this element
     * @see PageElement#getId()
     * @since 2.3
     */
    @Nonnull
    TimedQuery<String> getId();

    /**
     * Query for a set of CSS classes associated with this element.
     *
     * @return CSS classes of this element, or an empty set
     * @return TimedQuery for CSS classes of this element - returns a set of CSS classes, or an empty set otherwise,
     * incl. if the element does not exist
     * @see PageElement#getCssClasses()
     * @since 2.3
     */
    @Nonnull
    TimedQuery<Set<String>> getCssClasses();

    /**
     * Query representing whether this element has the given classname set.
     * @param className The name of the class to check
     * @return TimedQuery that returns true if element has given classname set, false if element does not have the
     * given classname set or timeout expires.
     */
    TimedCondition hasClass(String className);

    /**
     * Query representing the element's given attribute.
     *
     * @param name Name of the attribute
     *
     * @return TimedQuery that returns the value of the given attribute, null if element does not have given attribute
     * or timeout expires.
     */
    TimedQuery<String> getAttribute(String name);

    /**
     * Query representing whether this element has the given attribute set
     * @param name Name of the attribute
     * @param value expected attribute value
     * @return TimedQuery that returns true if element has given attribute set, false if element does not  have the
     * given attribute set or timeout expires
     */
    TimedCondition hasAttribute(String name, String value);

    /**
     * Query representing the element's inner text.
     *
     * @return TimedQuery that returns the inner text of the element, null if element does not have inner text
     * or timeout expires.
     */
    TimedQuery<String> getText();

    /**
     * Query representing whether this element's innerText is equal to the provided string.
     *
     * @param text The expected innerText string
     * @return timed condition that returns <code>true</code> if this element has given innerText equal to expected,
     * <code>false</code> otherwise
     */
    TimedCondition hasText(String text);

    /**
     * Query representing the element's tag name
     *
     * @return TimedQuery that returns the tagname of the element
     */
    TimedQuery<String> getTagName();

    /**
     * Query representing the element's 'value' attribute
     *
     * @return TimedQuery that returns the value of the 'value' attribute, null if element does not have a 'value'
     * attribute or timeout expires.
     */
    TimedQuery<String> getValue();

    /**
     * Query representing whether this element's value attribute is equal to the provided string.
     *
     * @param value The expected value attribute
     * @return timed condition that returns <code>true</code> if this element has given value attribute equal to
     * expected, <code>false</code> otherwise
     */
    TimedCondition hasValue(String value);

    /**
     * Timed query representing the location of the element on the page.
     *
     * @return query for location of the element on the page. Returns <code>null</code>, if the element cannot be located
     * by given timeout
     */
    TimedQuery<Point> getLocation();

    /**
     * Timed query representing the dimension of the element.
     *
     * @return query for dimension of the element on the page. Returns <code>null</code>, if the element cannot be located
     * by given timeout
     */
    TimedQuery<Dimension> getSize();
}
