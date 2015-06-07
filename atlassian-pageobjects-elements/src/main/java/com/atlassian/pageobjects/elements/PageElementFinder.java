package com.atlassian.pageobjects.elements;

import com.atlassian.annotations.PublicApi;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import org.openqa.selenium.By;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * <p/>
 * Encapsulates functionality for finding instances and collections of {@link com.atlassian.pageobjects.elements.PageElement}
 * on the tested pages, or parts of pages.
 *
 * <p/>
 * A finder is associated with some search scope - it will find elements within this scope (e.g. globally, or within
 * a parent page element).
 *
 * @since 2.0
 */
@PublicApi
public interface PageElementFinder {

    /**
     * Creates {@link com.atlassian.pageobjects.elements.PageElement} implementation
     * using the specified <tt>locator</tt> and default timeout.
     *
     * @param by Locator mechanism to use
     * @return Element that waits until its present in the DOM before executing actions.
     */
    @Nonnull
    PageElement find(@Nonnull By by);

    /**
     * Creates {@link com.atlassian.pageobjects.elements.PageElement} implementation
     * using the specified <tt>locator</tt> and given <tt>timeoutType</tt>.
     *
     * @param by Locator mechanism to use
     * @param timeoutType timeout for the element's timed operations
     * @return Element that waits until its present in the DOM before executing actions.
     */
    @Nonnull
    PageElement find(@Nonnull By by, @Nonnull TimeoutType timeoutType);

    /**
     * Creates  a {@link com.atlassian.pageobjects.elements.PageElement} for each element that matches the given <tt>locator</tt>
     * using default timeout.
     *
     * @param by Locator mechanism to use
     * @return List of PageElements that match the given locator
     */
    @Nonnull
    List<PageElement> findAll(@Nonnull By by);

    /**
     * Creates  a {@link com.atlassian.pageobjects.elements.PageElement} for each element that matches the given <tt>locator</tt>
     * using <tt>timeoutType</tt>.
     *
     * @param by Locator mechanism to use
     * @param timeoutType timeout for the element's timed operations
     * @return List of PageElements that match the given locator
     */
    @Nonnull
    List<PageElement> findAll(@Nonnull By by, @Nonnull TimeoutType timeoutType);

    /**
     * Creates {@link com.atlassian.pageobjects.elements.PageElement} extension of type <tt>T</tt> using the specified
     * <tt>locator</tt> and default timeout.
     *
     * @param by Locator mechanism to use
     * @param elementClass The class of the element to create
     * @return An instance that implements specified PageElement interface
     */
    @Nonnull
    <T extends PageElement> T find(@Nonnull By by, @Nonnull Class<T> elementClass);

    /**
     * Creates {@link com.atlassian.pageobjects.elements.PageElement} extension of type <tt>T</tt> using the specified
     * <tt>locator</tt> and given <tt>timeoutType</tt>
     *
     * @param by Locator mechanism to use
     * @param elementClass The class of the element to create
     * @param timeoutType timeout for the element's timed operations
     * @return An instance that implements specified PageElement interface
     */
    @Nonnull
    <T extends PageElement> T find(@Nonnull By by, @Nonnull Class<T> elementClass, @Nonnull TimeoutType timeoutType);

    /**
     * Creates (@Link PageElement) extension of type <tt>T</tt> for each element that matches the given
     * <tt>locator</tt> with default timeout
     * @param by Locator mechanism to use
     * @param elementClass The class of the element to create
     * @return A list of objects that implement specified PageElement interface
     */
    @Nonnull
    <T extends PageElement> List<T> findAll(@Nonnull By by, @Nonnull Class<T> elementClass);

    /**
     * Creates (@Link PageElement) extension of type <tt>T</tt> for each element that matches the given
     * <tt>locator</tt> with <tt>timeoutType</tt>
     * @param by Locator mechanism to use
     * @param elementClass The class of the element to create
     * @param timeoutType timeout for the element's timed operations
     * @return A list of objects that implement specified PageElement interface
     */
    @Nonnull
    <T extends PageElement> List<T> findAll(@Nonnull By by, @Nonnull Class<T> elementClass,
                                            @Nonnull TimeoutType timeoutType);
}
