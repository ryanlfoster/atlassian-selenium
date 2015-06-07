package com.atlassian.webdriver.utils.by;

import com.atlassian.webdriver.utils.JavaScriptUtils;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.webdriver.LifecycleAwareWebDriverGrid.getDriver;

/**
 * Provides an extension to By so that jQuery selectors can be used. By calling the ByJquery.$
 * method will ensure that jQuery get's loaded into the page. It is namespaced away within the
 * javascript so that it doens't override another version of jQuery on the page. This allows the
 * ByJquery locator to be dependent on it's own version of jQuery.
 * <p/>
 * same usages of ByJquery include: <code> ByJquery.$("div.className li");
 * ByJQuery.$("('div.className li a').parent('div')"); </code> It accepts simple searches like the
 * first example that don't need to be wrapped in brackets, but if you want to call another jQuery
 * method like <em>.parent</em> the first selector needs to be wrapped in brakcets.
 *
 * @since 2.0
 */
public abstract class ByJquery extends By
{

    // TODO: fix this so that can extract the simple selection out and run that and continue chaining
    // eg. $("#someid .class") -> By.id("someid"), By.className(class)
    private final Pattern SIMPLE_SELECTOR_PATTERN = Pattern.compile("^([#]|[.]|[a-zA-Z])[\\w-]+(\\s[^~]*)?$");
    private final Pattern ID_SELECTOR = Pattern.compile("^#(\\S+)(\\s?.*)$");
    private final Pattern CLASSNAME_SELECTOR = Pattern.compile("^[.](\\S+)(\\s?.*)$");
    private final Pattern TAGNAME_SELECTOR = Pattern.compile("^([A-Za-z]\\w+)(\\s?.*)$");

    private enum SelectorType {
        FIND,
        PARENT,
        PARENTS,
        SIBLINGS,
        CHILDREN,
        PREV,
        CLOSEST,
        FILTER;
    }

    private class Selector {

        private final String selector;
        private final SelectorType type;

        public Selector(String selector, SelectorType type) {
            this.selector = selector;
            this.type = type;
        }

        public String getSelector()
        {
            return selector;
        }

        public SelectorType getType()
        {
            return type;
        }
    }

    private final List<Selector> selectors;

    private ByJquery(String selector, SelectorType type)
    {
        this.selectors = new ArrayList<Selector>();
        this.selectors.add(new Selector(selector, type));
    }

    private ByJquery() {
        this.selectors = new ArrayList<Selector>();
    }

    private ByJquery(String selector, SelectorType type, List<Selector> selectors)
    {
        this.selectors = selectors;
        this.selectors.add(new Selector(selector, type));
    }

    List<WebElement> findElementsWithSelectors(SearchContext context)
    {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (Selector selector : selectors)
        {

            String selectorStr = fixSelector(selector.getSelector());

            Object[] args = null;

            switch (selector.type)
            {
                case FIND:
                    if (elements.isEmpty())
                    {
                        elements = $findElements(selector.getSelector(), context);
                    }
                    else {
                        elements = $findElements(selector.getSelector(), elements);
                    }
                    break;
                case PARENT:
                case SIBLINGS:
                case PARENTS:
                case CHILDREN:
                case PREV:
                case CLOSEST:
                    args = new Object[]{"ATLWD.$(context)." + selector.type.name().toLowerCase() + "(" + selectorStr + ")", elements };
                    elements = JavaScriptUtils.execute("return ATLWD.byJquery.execute(arguments[0],arguments[1])", getDriver(), args);
                    break;
                case FILTER:
                    args = new Object[]{"ATLWD.$(context)." + selector.type.name().toLowerCase() + "(" + selector.getSelector() + ")", elements };
                    elements = JavaScriptUtils.execute("return ATLWD.byJquery.execute(arguments[0],arguments[1])", getDriver(), args);
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown selector type: " + selector.type);
            }

        }

        return elements;
    }

    private String fixSelector(String selector)
    {
        if (selector == null)
        {
            return "";
        }
        else
        {
            return "'" + selector.replaceAll("'", "\"") + "'";
        }

        //return selector == null ? "" : "'" + selector + "'";
    }

    private boolean isSimpleSelector(String selector)
    {
        return SIMPLE_SELECTOR_PATTERN.matcher(selector).matches();
    }

    private List<WebElement> executeMatcher(Matcher matcher, SearchContext context, By by)
    {
        if (matcher.matches())
            {
                if (StringUtils.isEmpty(matcher.group(2)))
                {
                    return context.findElements(by);
                }
                else
                {
                    return $findElements(matcher.group(2), context.findElements(by));
                }
            }

            throw new IllegalStateException("ID Selector failed match");
    }

    private List<WebElement> executeIdMatcher(Matcher matcher, SearchContext context)
    {
        if (matcher.find()) {
            return executeMatcher(matcher, context, By.id(matcher.group(1)));
        }

        throw new IllegalArgumentException("Invalid matcher.");
    }

    private List<WebElement> executeClassNameMatcher(Matcher matcher, SearchContext context)
    {
        if (matcher.find())
        {
            return executeMatcher(matcher, context, By.className(matcher.group(1)));
        }

        throw new IllegalArgumentException("Invalid matcher.");
    }

    private List<WebElement> executeTagNameMatcher(Matcher matcher, SearchContext context)
    {
        if (matcher.find())
        {
            return executeMatcher(matcher, context, By.tagName(matcher.group(1)));
        }

        throw new IllegalArgumentException("Invalid matcher.");
    }

    private List<WebElement> executeSimpleSelector(String selector, SearchContext context)
    {
        if (selector.startsWith("#"))
        {
            Matcher matcher = ID_SELECTOR.matcher(selector);
            return executeIdMatcher(matcher, context);
        }
        else if (selector.startsWith("."))
        {
            Matcher matcher = CLASSNAME_SELECTOR.matcher(selector);
            return executeClassNameMatcher(matcher, context);
        }
        else
        {
            Matcher matcher = TAGNAME_SELECTOR.matcher(selector);
            return executeTagNameMatcher(matcher, context);
        }
    }

    List<WebElement> $findElements(final String selector, final SearchContext context)
    {
        if (isSimpleSelector(selector))
        {
            return executeSimpleSelector(selector, context);
        }

        String fixedSelector = fixSelector(selector);

        if (context instanceof WebElement)
        {
            Object[] args = { "ATLWD.$(context).find(" + fixedSelector + ")", (WebElement) context };
            return JavaScriptUtils.execute("return ATLWD.byJquery.execute(arguments[0],arguments[1])", getDriver(), args);
        }
        else
        {
            Object[] args = { "ATLWD.$(document).find(" + fixedSelector  + ")"};
            return JavaScriptUtils.execute("return ATLWD.byJquery.execute(arguments[0])", getDriver(), args);
        }
    }

    List<WebElement> $findElements(final String selector, List<WebElement> els)
    {
        // Chrome Bug(http://code.google.com/p/selenium/issues/detail?id=934):
        // if pass in elements in the args object then chrome fails to run the selector properly.
        // So intead have to iterate over the elements and run the selector one at a time.
        List<WebElement> newElements = new ArrayList<WebElement>();
        for (WebElement element : els)
        {
            Object[] args = { "ATLWD.$(context).find(" + fixSelector(selector) + ")", element };
            List<WebElement> temp = JavaScriptUtils.execute("return ATLWD.byJquery.execute(arguments[0],arguments[1])", getDriver(), args);
            newElements.addAll(temp);
        }

        return newElements;
        //Object[] args = { "ATLWD.byJquery.$(context).find('" + selector + "')", els };
        //return JavaScriptUtils.execute("return ATLWD.byJquery.execute(arguments[0],arguments[1])", driver, args);
    }

    public static ByJquery $(final WebElement element)
    {
        loadJqueryLocator(getDriver());

        return new ByJquery() {
            @Override
            public List<WebElement> findElements(final SearchContext context)
            {
                return findElementsWithSelectors(element);
            }

            @Override
            public WebElement findElement(final SearchContext context)
            {
                List<WebElement> els = findElementsWithSelectors(element);
                if(!els.isEmpty())
                {
                    return els.get(0);
                }
                else
                {
                    throw new NoSuchElementException("Element not found by jQuery.");
                }
            }

        };
    }

    public static ByJquery $(final String selector)
    {
        if (selector == null)
        {
            throw new IllegalArgumentException("The jquery selector cannot be null.");
        }
        loadJqueryLocator(getDriver());

        return new ByJquery(selector, SelectorType.FIND)
        {
            @Override
            public List<WebElement> findElements(final SearchContext context)
            {
                return findElementsWithSelectors(context);
            }

            @Override
            public WebElement findElement(final SearchContext context)
            {
                List<WebElement> els = findElementsWithSelectors(context);
                if(!els.isEmpty())
                {
                    return els.get(0);
                }
                else
                {
                    throw new NoSuchElementException(this.toString());
                }
            }

            @Override
            public String toString()
            {
                return "jQuery selector: " + selector;
            }
        };

    }

    private void addSelector(Selector selector)
    {
        this.selectors.add(selector);
    }

    public ByJquery find(String selector) {
        Validate.notNull(selector, "The find selector cannot be null");
        addSelector(new Selector(selector, SelectorType.FIND));

        return this;
    }

    public ByJquery parent()
    {
        return parent(null);
    }

    public ByJquery parent(String selector)
    {

        addSelector(new Selector(selector, SelectorType.PARENT));

        return this;
    }

    public ByJquery parents()
    {
        return parents(null);
    }

    public ByJquery parents(String selector)
    {

        addSelector(new Selector(selector, SelectorType.PARENTS));

        return this;
    }

    public ByJquery siblings()
    {
        return siblings(null);
    }

    public ByJquery siblings(String selector)
    {

        addSelector(new Selector(selector, SelectorType.SIBLINGS));

        return this;
    }

    public ByJquery children()
    {
        return children(null);
    }

    public ByJquery children(String selector)
    {
        addSelector(new Selector(selector, SelectorType.CHILDREN));

        return this;
    }

    public ByJquery prev() {
        return prev(null);
    }

    public ByJquery prev(String selector)
    {
        addSelector(new Selector(selector, SelectorType.PREV));

        return this;
    }

    public ByJquery closest(String selector)
    {
        addSelector(new Selector(selector, SelectorType.CLOSEST));
        return this;
    }

    /**
     * This allows filtering results based on a selector or a function.
     * elements are currently not supported.
     * @param selector can represent a selector expression or a function
     * @return
     */
    // TODO(jwilson): SELENIUM-173 Fix up filter method.
    public ByJquery filter(String selector)
    {
        addSelector(new Selector(selector, SelectorType.FILTER));
        return this;
    }

    /**
     * Find a single element. Override this method if necessary.
     *
     * @param context A context to use to find the element
     * @return The WebElement that matches the selector
     */
    public WebElement findElement(SearchContext context)
    {
        List<WebElement> allElements = findElements(context);
        if (allElements == null || allElements.size() == 0)
        {
            throw new NoSuchElementException("Cannot locate an element using " + toString());
        }
        return allElements.get(0);
    }

    private static void loadJqueryLocator(WebDriver driver)
    {
        JavaScriptUtils.loadScript("js/byjquery/byJquery.js", driver);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        By by = (By) o;

        return toString().equals(by.toString());
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

}
