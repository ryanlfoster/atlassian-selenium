package com.atlassian.pageobjects.elements;

/**
 * <p/>
 * Encapsulates Javascript mouse events of the {@link com.atlassian.pageobjects.elements.PageElement}.
 *
 * <p/>
 * {@link PageElement#isPresent()} of the corresponding page element must return <code>true</code>
 * before any of the methods of this interface are invoked, otherwise {@link org.openqa.selenium.NoSuchElementException}
 * will be raised.
 *
 * <p/>
 * NOTE: all events invoked via this class are pure Javascript events and don't simulate the real user-browser
 * interaction. E.g. in real interaction a mouse click would trigger 'mousedown', 'mouseup' and 'click' events,
 * whereas the {@link #click()} in this class only produces the 'click' event.
 *
 * <p/>
 * Use this class primarily as workaround when standard methods in
 * {@link com.atlassian.pageobjects.elements.PageElement} simulating user interaction don't produce desired results.
 *
 * @since 2.1
 */
public interface PageElementMouseJavascript
{
    /**
     * Dispatches a click event to the associated element. This is different to {@link PageElement#click()}
     * in that this is invoking the Javascript 'click' event as opposed to simulating user click action.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript click();

    /**
     * <p/>
     * Dispatches a 'double click' event to the associated element.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript doubleClick();

    /**
     * <p/>
     * Dispatches a 'mouseup' event to the associated element.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript mouseup();

    /**
     * <p/>
     * Dispatches a 'mousedown' event to the associated element.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript mousedown();

    /**
     * Dispatches a 'mouseover' event to the associated element, commonly referred to as 'hover'.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript mouseover();

    /**
     * Dispatches a 'mousemove' event to the associated element.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript mousemove();

    /**
     * Dispatches a 'mouseout' event to the associated element.
     *
     * @return the associated Javascript object
     */
    PageElementJavascript mouseout();

}
