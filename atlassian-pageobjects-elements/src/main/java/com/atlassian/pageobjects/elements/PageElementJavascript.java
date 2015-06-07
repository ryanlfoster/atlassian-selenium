package com.atlassian.pageobjects.elements;

import com.atlassian.pageobjects.elements.query.TimedQuery;

/**
 * <p/>
 * Encapsulates Javascript functionality of the {@link com.atlassian.pageobjects.elements.PageElement}.
 *
 * <p/>
 * {@link PageElement#isPresent()} of the corresponding page element must return <code>true</code>
 * before any of the methods of this interface are invoked, otherwise {@link org.openqa.selenium.NoSuchElementException}
 * will be raised.
 *
 * @since 2.1
 */
public interface PageElementJavascript
{

    /**
     * Access to mouse events for the associated page element.
     *
     * @return mouse events for the element
     */
    PageElementMouseJavascript mouse();


    /**
     * Access to form events for the associated page element.
     *
     * @return form events for the element
     */
    PageElementFormJavascript form();


    // TODO NOT including this. This is low-priority and tricky to implement
//    /**
//     * Access to keyboard events for the associated page element.
//     *
//     * @return keyboard events for the element
//     */
//    PageElementKeyboardJavascript keyboard();

    /**
     * <p/>
     * Executes custom script on this element.
     *
     * <p/>
     * The corresponding HTML element will be available in the executing script under <tt>arguments[0]</tt> entry.
     * The provided <tt>arguments</tt> will be available under subsequents entries in the 'arguments' magic variable, as
     * stated in {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...)}.
     *
     * <p/>
     * The arguments and return type are as in
     * {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...)} with addition of
     * {@link com.atlassian.pageobjects.elements.PageElement}s as valid argument type.
     *
     * <p/>
     * When a DOM element is returned from the script, a corresponding
     * {@link com.atlassian.pageobjects.elements.PageElement}
     * instance will be returned from this method
     *
     * @param script javascript to execute
     * @param arguments custom arguments to the script. a number, a boolean, a String,
     * a {@link com.atlassian.pageobjects.elements.PageElement}, a {@link org.openqa.selenium.WebElement} or a List of
     * any combination of the above
     * @return One of Boolean, Long, String, List or {@link com.atlassian.pageobjects.elements.PageElement},
     * or <code>null</code>.
     * @throws NullPointerException if <tt>script</tt> is <code>null</code>
     *
     * @see org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...)
     */
    public Object execute(String script, Object... arguments);

    /**
     * <p/>
     * Provides the same functionality as {@link #execute(String, Object...)}, but lets the client specify the
     * expected result type. The expected result type must not be <code>null</code> and must match the actual
     * result from the executed script.
     *
     * @param resultType expected type of the result. One of Boolean, Long, String, List or
     * {@link com.atlassian.pageobjects.elements.PageElement} . Must not be <code>null</code>
     * @param script javascript to execute
     * @param arguments custom arguments to the script. a number, a boolean, a String,
     * a {@link com.atlassian.pageobjects.elements.PageElement}, a {@link org.openqa.selenium.WebElement} or a List of
     * any combination of the above
     * @return result of the script converted to the <tt>resultType</tt>
     * @throws NullPointerException if <tt>script</tt> or <tt>resultType</tt> is <code>null</code>
     * @throws IllegalArgumentException if <tt>resultType</tt> is not one of the expected types
     * @throws ClassCastException if the actual result type does not match <tt>resultType</tt>
     *
     * @see #execute(String, Object...)
     */
    public <T> T execute(Class<T> resultType, String script, Object... arguments);


    /**
     * <p/>
     * Executes custom script on this element in a periodic manner, allowing the client to wait for a particular
     * expected result to occur (via the returned {@link TimedQuery}).
     *
     * <p/>
     * All rules (in particular with regards to the result type) of {@link #execute(String, Object...)} apply to this
     * method.
     *
     * <p/>
     * The caller must provide the expected return type as <tt>resultType</tt>. It must be one of the valid result types
     * or an exception will be raised.
     *
     * @param resultType expected type of the result. One of Boolean, Long, String or List. Must not be <code>null</code>
     * @param script javascript to execute
     * @param arguments custom arguments to the script. a number, a boolean, a String,
     * a {@link com.atlassian.pageobjects.elements.PageElement}, a {@link org.openqa.selenium.WebElement} or a List of
     * any combination of the above
     * @return {@link com.atlassian.pageobjects.elements.query.TimedQuery} to query for the expected result
     * @throws NullPointerException if <tt>script</tt> or <tt>resultType</tt> is <code>null</code>
     * @throws IllegalArgumentException if <tt>resultType</tt> is not one of the expected types
     * @throws ClassCastException if the actual result type does not match <tt>resultType</tt>
     *
     * @see org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...)
     * @see #execute(String, Object...)
     *
     */
    public <T> TimedQuery<T> executeTimed(Class<T> resultType, String script, Object... arguments);


    /**
     * <p/>
     * Executes custom script on this element asynchronously.
     *
     * <p/>
     * All rules and conditions of {@link #execute(String, Object...)} apply, except the last argument in the
     * 'arguments' magic variable in the script is a callback that has to be invoked and given result of the script
     * so that this method returns. See {@link org.openqa.selenium.JavascriptExecutor#executeAsyncScript(String, Object...)}
     * for details.
     *
     * <p/>
     * Consider using {@link #executeTimed(Class, String, Object...)} instead.
     *
     * @param script javascript to execute
     * @param  arguments custom arguments to the script. a number, a boolean, a String,
     * a {@link com.atlassian.pageobjects.elements.PageElement}, a {@link org.openqa.selenium.WebElement} or a List of
     * any combination of the above
     * @return One of Boolean, Long, String, List or {@link com.atlassian.pageobjects.elements.PageElement}. Or null.
     * @throws NullPointerException if <tt>script</tt> is <code>null</code>
     *
     * @see org.openqa.selenium.JavascriptExecutor#executeAsyncScript(String, Object...) (String, Object...)
     * @see #execute(String, Object...)
     */
    public Object executeAsync(String script, Object... arguments);

    /**
     * <p/>
     * Provides the same functionality as {@link #executeAsync(Class, String, Object...)}, but lets the client specify
     * the expected result type. The expected result type must not be <code>null</code> and must match the actual
     * result from the executed script.
     *
     * <p/>
     * Consider using {@link #executeTimed(Class, String, Object...)} instead.
     *
     * @param resultType expected type of the result. One of Boolean, Long, String, List or
     * {@link com.atlassian.pageobjects.elements.PageElement} . Must not be <code>null</code>
     * @param script javascript to execute
     * @param arguments custom arguments to the script. a number, a boolean, a String,
     * a {@link com.atlassian.pageobjects.elements.PageElement}, a {@link org.openqa.selenium.WebElement} or a List of
     * any combination of the above
     * @return result of the script converted to the <tt>resultType</tt>
     * @throws NullPointerException if <tt>script</tt> or <tt>resultType</tt> is <code>null</code>
     * @throws IllegalArgumentException if <tt>resultType</tt> is not one of the expected types
     * @throws ClassCastException if the actual result type does not match <tt>resultType</tt>
     *
     * @see #executeAsync(String, Object...) (String, Object...)
     */
    public <T> T executeAsync(Class<T> resultType, String script, Object... arguments);
}
