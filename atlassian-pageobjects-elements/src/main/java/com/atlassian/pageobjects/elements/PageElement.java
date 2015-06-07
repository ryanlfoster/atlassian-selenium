package com.atlassian.pageobjects.elements;

import com.atlassian.annotations.PublicApi;
import com.atlassian.pageobjects.elements.search.PageElementSearch;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Represents an HTML element that is expected on a DOM of a page.
 *
 * @since 2.0
 */
@PublicApi
public interface PageElement extends PageElementFinder, PageElementSearch
{
    /**
     * Whether this element is currently on the DOM of the page
     *
     * @return True if this element tag exists in the DOM, false otherwise.
     */
    boolean isPresent();

    /**
     * Whether this element is visible on the page
     *
     * @return true if this element is visible on the page, false otherwise.
     */
    boolean isVisible();

    /**
     * Whether this element is enabled on the page
     *
     * @return True if this element is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Whether this element is selected on the page.  Useful on checkboxes and radio buttons.
     * For checkboxes, returns {@code true} if the checkbox is checked.
     * For radio buttons, returns {@code true}
     * if the radio button this class represents is selected.
     *
     * @return {@code true} if this element is selected, {@code false} otherwise.
     */
    boolean isSelected();

    /**
     * @return value of the "id" attribute of this element, or {@code null} if the "id" attribute is not defined
     *
     * @since 2.3
     */
    @Nullable
    String getId();

    /**
     * Get a set of CSS classes associated with this element.
     *
     * @return CSS classes of this element, or an empty set
     * @since 2.3
     */
    @Nonnull
    Set<String> getCssClasses();

    /**
     * Whether this element has the given class set
     * 
     * @param className The name of the class to check
     * @return true if this element's class attribute contains the given classname, false otherwise.
     */
    boolean hasClass(@Nonnull String className);

    /**
     * Get the value of the given attribute of this element.
     *
     * @param name The name of the attribute.
     * @return The attribute's current value, or null if the value is not set
     */
    @Nullable
    String getAttribute(@Nonnull String name);

    /**
     * Whether this element has an attribute set to a specific value
     * @param name The attribute name
     * @param value The expected value
     * @return true if attribute is set to the specific value, false otherwise or if attribute is not present
     */
    boolean hasAttribute(@Nonnull String name, @Nullable String value);

    /**
     * Get the visible innerText of this element, including sub-elements, without any leading or trailing whitespaces.
     *
     * @return The innerText of this element.
     */
    @Nullable
    String getText();

    /**
     * Get the tag name of this element
     * @return The tag name
     */
    @Nonnull
    String getTagName();

    /**
     * Get the value of this element's "value" attribute.
     *
     * @return The value of this element's "value" attribute, or null if the value is not set.
     */
    @Nullable
    String getValue();

    /**
     * Location of this element on the page (top-left corner)
     *
     * @return location of the element on the page
     */
    @Nonnull
    Point getLocation();

    /**
     * Dimension of this element on the page
     *
     * @return size of the element on the page
     */
    @Nonnull
    Dimension getSize();

    /**
     * Click this element
     *
     * @return The eleemnt that got clicked.
     */
    @Nonnull
    PageElement click();

    /**
     * Simulate typing into this element. This will append the keystrokes to the end of the text entry element.
     * 
     * @param keys keys to type
     * @return The Element that got typed in.
     */
    @Nonnull
    PageElement type(CharSequence... keys);

    /**
     * Select an element. This method will work against radio buttons, "option" elements within a "select" and checkboxes
     *
     * @return The Element that got selected
     */
    @Nonnull
    PageElement select();

    /**
     * If the element is a checkbox this will toggle the elements state from selected to not selected, or from not selected to selected.
     * 
     * @return The Element that got toggled
     */
    @Nonnull
    PageElement toggle();

    /**
     * Clear the value of the text entry element.
     *
     * @return The Element that got cleared.
     */
    @Nonnull
    PageElement clear();

    /**
     * Returns a list of element's that match the given locator within this element
     * @param locator The locator mecharnism
     * @return A list of elements that are located within this element.
     */
    @Nonnull
    List<PageElement> findAll(@Nonnull By locator);

    /**
     * <p/>
     * Returns an element that will match the given locator within this element.
     *
     * <p/>
     * This method will <i>always</i> return an element instance. Use {@link #isPresent()}
     * to check whether a corresponding element on the page actually exists.
     *
     * @param locator The locator mechanism
     * @return An element that will be located within this element.
     */
    @Nonnull
    PageElement find(@Nonnull By locator);

    /**
     * Creates a timed element based on this element's locator.
     *
     * @return A TimedElement that is based on this element's locator.
     */
    @Nonnull
    TimedElement timed();

    /**
     * Gets a <tt>PageElementJavascript</tt> for this element
     * 
     * @return API to execute javascript on this element.
     */
    @Nonnull
    PageElementJavascript javascript();

    /**
     * Returns an instance equivalent to this element, with a changed <tt>timeoutType</tt>.
     *
     * @param timeoutType new timeout
     * @return new element with given <tt>timeoutType</tt>
     */
    @Nonnull
    PageElement withTimeout(@Nonnull TimeoutType timeoutType);
}
