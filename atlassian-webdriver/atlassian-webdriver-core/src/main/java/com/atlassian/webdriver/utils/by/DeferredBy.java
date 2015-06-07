package com.atlassian.webdriver.utils.by;

import com.atlassian.webdriver.utils.element.ElementLocated;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.atlassian.webdriver.LifecycleAwareWebDriverGrid.getDriver;

/**
 * Looks for an element by using the WebDriver wait and ElementLocated condition.
 * It will wait up to 20 seconds for the element to be located.
 *
 * @since 2.0
 */
public abstract class DeferredBy extends By
{

    private static final Logger log = LoggerFactory.getLogger(DeferredBy.class);

    private static final int DEFERRED_BY_WAIT = 20;


    private static WebDriverWait getWait()
    {
        return new WebDriverWait(getDriver(), DEFERRED_BY_WAIT);
    }

    private static DeferredBy by(final By selector) {

        if (selector == null)
        {
            throw new IllegalArgumentException("Cannot find elements with a null by selector");
        }

        return new DeferredBy() {

            @Override
            public List<WebElement> findElements(final SearchContext context)
            {
                try
                {
                    getWait().until(new ElementLocated(selector));
                    return context.findElements(selector);
                }
                catch(TimeoutException e)
                {
                    log.error("DeferredBy was unable to findElements using the selector: " + selector);
                    throw e;
                }
            }

            @Override
            public WebElement findElement(final SearchContext context)
            {
                try
                {
                    getWait().until(new ElementLocated(selector));
                    return context.findElement(selector);
                }
                catch(TimeoutException e)
                {
                    log.error("DeferredBy was unable to findElement using the selector: " + selector);
                    throw e;
                }
            }

            @Override
            public String toString()
            {
                return "DeferredBy(" + super.toString() + ")";
            }
        };

    }

    /**
     * @param id The value of the "id" attribute to search for
     * @return a By which locates elements by the value of the "id" attribute.
     */
    public static DeferredBy id(final String id) {
        return by(By.id(id));
    }

    /**
     * @param linkText The exact text to match against
     * @return a By which locates A elements by the exact text it displays
     */
    public static By linkText(final String linkText) {
        return by(By.linkText(linkText));
    }

    /**
     * @param linkText The text to match against
     * @return a By which locates A elements that contain the given link text
     */
    public static By partialLinkText(final String linkText) {
      return by(By.partialLinkText(linkText));
    }

    /**
     * @param name The value of the "name" attribute to search for
     * @return a By which locates elements by the value of the "name" attribute.
     */
    public static By name(final String name) {
        return by(By.name(name));
    }

    /**
     * @param name The element's tagName
     * @return a By which locates elements by their tag name
     */
    public static By tagName(final String name) {
      return by(By.tagName(name));
    }

    /**
   * @param xpathExpression The xpath to use
   * @return a By which locates elements via XPath
   */
    public static By xpath(final String xpathExpression) {
      return by(By.xpath(xpathExpression));
    }

    /**
   * Finds elements based on the value of the "class" attribute. If an element has many classes
   * then this will match against each of them. For example if the value is "one two onone", then the
   * following "className"s will match: "one" and "two"
   *
   * @param className The value of the "class" attribute to search for
   * @return a By which locates elements by the value of the "class" attribute.
   */
    public static By className(final String className) {
       return by(By.className(className));
    }

    /**
     * Finds elements via the driver's underlying W3 Selector engine. If the browser does not
     * implement the Selector API an exception will be thrown.
     */
    public static By cssSelector(final String selector) {
        return by(By.cssSelector(selector));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeferredBy by = (DeferredBy) o;

        return toString().equals(by.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
