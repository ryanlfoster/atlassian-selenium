package com.atlassian.pageobjects.elements;

/**
 * <p/>
 * Encapsulates Javascript form element events of the {@link PageElement}.
 *
 * <p/>
 * {@link com.atlassian.pageobjects.elements.PageElement#isPresent()} of the corresponding page element must return <code>true</code>
 * before any of the methods of this interface are invoked, otherwise {@link org.openqa.selenium.NoSuchElementException}
 * will be raised.
 *
 * <p/>
 * NOTE: all events invoked via this class are pure Javascript events and don't simulate the real user-browser
 * interaction.
 *
 * <p/>
 * Use this class primarily as workaround when standard methods in
 * {@link PageElement} simulating user interaction don't produce desired results.
 *
 * @since 2.1
 */
public interface PageElementFormJavascript
{
    /**
     * Dispatches a 'select' event to the associated element, as if a text in this element was selected.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript select();

    /**
     * <p/>
     * Dispatches a 'change' event to the associated element, as if the value of this element has changed.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript change();

    /**
     * <p/>
     * Dispatches a 'submit' event to the associated element, as if its form (or the form it represents) was submitted.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript submit();

    /**
     * <p/>
     * Dispatches a 'focus' event to the associated element, as if it received input focus.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript focus();

    /**
     * Dispatches a 'blur' event to the associated element, as if it lost input focus.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript blur();

}
