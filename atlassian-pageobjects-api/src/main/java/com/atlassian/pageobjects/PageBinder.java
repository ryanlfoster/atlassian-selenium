package com.atlassian.pageobjects;

/**
 * Creates and binds the page objects to the page.  Also supports special page navigation.  Implementations should use
 * any defined overrides in preference to passed class instances.
 */
public interface PageBinder
{
    /**
     * Constructs the page object, changes the browser URL to the desired page URL, then binds the object to the page.
     * @param pageClass The page class
     * @param args Arguments to pass to the page object constructor.
     * @param <P> The page type
     * @return The constructed and fully loaded page with the browser set accordingly
     */
    <P extends Page> P navigateToAndBind(Class<P> pageClass, Object... args);

    /**
     * Builds and binds the page object to the page.
     *
     * @param pageClass The page object class
     * @param args Arguments to pass to the page object constructor.
     * @param <P> The page type
     * @return The constructed and loaded page object with the browser set accordingly
     */
    <P> P bind(Class<P> pageClass, Object... args);

    /**
     * Creates a delayed binder that gives the caller full control over the lifecycle of the page object.  The page
     * object will not even be instantiated until the {@link DelayedBinder} methods are called.
     * @param pageClass The page object class
     * @param args The arguments to pass to the page object constructor
     * @param <P> The page type
     * @return A delayed binder instance
     */
    <P> DelayedBinder<P> delayedBind(Class<P> pageClass, Object... args);

    /**
     * Overrides a page object
     * @param oldClass The old class that would have normally been constructed
     * @param newClass An subclass of the old class to be substituted
     * @param <P> The old class type
     */
    <P> void override(Class<P> oldClass, Class<? extends P> newClass);
}
