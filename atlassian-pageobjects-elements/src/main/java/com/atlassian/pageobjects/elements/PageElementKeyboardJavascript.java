package com.atlassian.pageobjects.elements;

/**
 * <p/>
 * Encapsulates Javascript keyboard element events of the {@link com.atlassian.pageobjects.elements.PageElement}.
 *
 * <p/>
 * {@link PageElement#isPresent()} of the corresponding page element must return <code>true</code>
 * before any of the methods of this interface are invoked, otherwise {@link org.openqa.selenium.NoSuchElementException}
 * will be raised.
 *
 * <p/>
 * NOTE: all events invoked via this class are pure Javascript events and don't simulate the real user-browser
 * interaction. The standard real-browser interaction is that for each pressed key a set of 'keydown', 'keypress' and
 * 'keyup' events is triggered.
 *
 * <p/>
 * NOTE: clients are responsible for providing appropriate key codes for spacial keys for each event. See e.g.
 * http://unixpapa.com/js/key.html.
 *
 * <p/>
 * Use this class primarily as workaround when standard methods in
 * {@link com.atlassian.pageobjects.elements.PageElement} simulating user interaction don't produce desired results.
 *
 * @since 2.1
 */
public interface PageElementKeyboardJavascript
{
    /**
     * Dispatches a 'keydown' event to the associated element.
     *
     * @param keyCode key code
     * @return the associated Javascript object
     */
    PageElementJavascript keyDown(int keyCode);

    /**
     * <p/>
     * Dispatches a 'keypress' event to the associated element.
     *
     * @param keyCode key code
     * @return the associated Javascript object
     */
    PageElementJavascript keyPress(int keyCode);

    /**
     * <p/>
     * Dispatches a 'keydown' event to the associated element.
     *
     * @param keyCode key code
     * @return the associated Javascript object
     */
    PageElementJavascript keyUp(int keyCode);

}
