package com.atlassian.pageobjects;

/**
 * The product being tested.  Provides access to key services to be used during testing.
 */
public interface TestedProduct<T extends Tester>
{
    /**
     * Constructs the page object, changes the browser URL to the desired page URL, then binds the object to the page.
     * @param pageClass The page class
     * @param args Arguments to pass to the page object constructor.
     * @param <P> The page type
     * @return The constructed and fully loaded page with the browser set accordingly
     */
    <P extends Page> P visit(Class<P> pageClass, Object... args);

    /**
     * @return The page binder for visiting pages and binding page objects
     */
    PageBinder getPageBinder();

    /**
     * @return Information about the instance being tested
     */
    ProductInstance getProductInstance();

    /**
     * @return The tester being used
     */
    T getTester();
}
