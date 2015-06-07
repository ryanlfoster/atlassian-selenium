package com.atlassian.webtest.ui.keys;

/**
 * <p>
 * Enumeration of possible type modes. Surprisingly, there are many ways to insert a text into a HTML
 * input on a page. They differ in speed, reliability and events raised by the browser. Every element
 * in the framework capable of inserting a text into it should document its default type mode and
 * optionally expose methods for different type modes.
 *
 * <p>
 * <b>NOTE:</b> use this facility sparingly, most of the time you should be relying on the default
 * type mode of any element. Implementations are not obliged to honour this setting. 
 */
public enum TypeMode
{

    /**
     * The type target decides itself about the type mode. This is the recommended option in most cases!
     *
     */
    DEFAULT,

    /**
     * Simply set the value of a HTML element. This is the fastest way, but <b>will not</b> invoke any events.
     *
     */
    INSERT,

    /**
     * Insert all characters and invoke all events for the last typed key.
     *
     */
    INSERT_WITH_EVENT,

    /**
     * Type into the component invoking all associated key events for each stroke.
     *
     */
    TYPE
}
