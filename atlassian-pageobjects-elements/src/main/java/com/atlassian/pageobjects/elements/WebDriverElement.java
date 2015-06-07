package com.atlassian.pageobjects.elements;

import com.atlassian.annotations.Internal;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.search.DefaultQuery;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.atlassian.pageobjects.elements.timeout.Timeouts;
import com.atlassian.pageobjects.internal.elements.search.DefaultWebDriverQuery;
import com.atlassian.webdriver.Elements;
import com.atlassian.webdriver.utils.Check;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.atlassian.webdriver.Elements.ATTRIBUTE_CLASS;
import static com.atlassian.webdriver.Elements.ATTRIBUTE_ID;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link PageElement} that waits for element to be
 * present before executing each actions.
 *
 * @since 2.0
 */
@Internal
public class WebDriverElement implements PageElement
{
    public static Function<PageElement, WebDriverLocatable> TO_LOCATABLE = new Function<PageElement, WebDriverLocatable>()
    {
        @Override
        public WebDriverLocatable apply(PageElement element)
        {
            return toLocatable(element);
        }
    };

    @Inject
    protected WebDriver driver;

    @Inject
    protected PageBinder pageBinder;

    @Inject
    protected Timeouts timeouts;

    protected final WebDriverLocatable locatable;
    protected final TimeoutType defaultTimeout;

    @Nonnull
    public static <P extends PageElement> Function<WebDriverLocatable, P> bind(@Nonnull final PageBinder pageBinder,
                                                                               @Nonnull final Class<P> elementType,
                                                                               @Nonnull final TimeoutType timeoutType)
    {
        checkNotNull(pageBinder, "pageBinder");
        checkNotNull(elementType, "elementType");
        checkNotNull(timeoutType, "timeoutType");

        return new Function<WebDriverLocatable, P>()
        {
            @Override
            public P apply(WebDriverLocatable locatable)
            {
                return pageBinder.bind(WebDriverElementMappings.findMapping(elementType), locatable, timeoutType);
            }
        };
    }

    @Nonnull
    public static WebDriverLocatable toLocatable(@Nonnull PageElement pageElement)
    {
        return cast(pageElement).locatable;
    }

    static WebElement getWebElement(PageElement element)
    {
        return cast(element).asWebElement();
    }

    private static WebDriverElement cast(PageElement element)
    {
        if (!WebDriverElement.class.isInstance(element))
        {
            throw new IllegalStateException("Unknown implementation of PageElement, cannot use to retrieve WebElement");
        }
        return WebDriverElement.class.cast(element);
    }


    /**
     * Creates a WebDriverElement within the driver's search context and default timeout
     * @param locator The locator mechanism to use.
     */
    public WebDriverElement(By locator)
    {
        this(locator, TimeoutType.DEFAULT);
    }

    /**
     * Creates a WebDriverElement within the driver's search context and given timeout type.
     * 
     * @param locator The locator mechanism to use.
     * @param timeoutType default timeout of this element
     */
    public WebDriverElement(By locator, TimeoutType timeoutType)
    {
        this(locator, WebDriverLocators.root(), timeoutType);
    }

    /**
     * Creates a WebDriverElement within a given parent and default timeout.
     *
     * @param locator The locator mechanism to use.
     * @param parent The locatable parent of this element.
     */
    public WebDriverElement(By locator, WebDriverLocatable parent)
    {
        this(locator, parent, TimeoutType.DEFAULT);
    }

    /**
     * Creates a WebDriverElement within a given parent and given timeout type.
     *
     * @param locator The locator mechanism to use.
     * @param parent The locatable parent of this element.
     * @param timeoutType default timeout of this element
     */
    public WebDriverElement(By locator, WebDriverLocatable parent, TimeoutType timeoutType)
    {
        this(WebDriverLocators.nested(locator, parent), timeoutType);
    }

    /**
     * Creates a WebDriverElement with the given locatable and timeout type.
     *
     * @param locatable WebDriverLocatable that that locate this element
     * @param timeoutType default timeout of this element
     */
    public WebDriverElement(WebDriverLocatable locatable, TimeoutType timeoutType)
    {
        this.locatable = checkNotNull(locatable, "locatable");
        this.defaultTimeout = checkNotNull(timeoutType, "timeoutType");
    }

    protected long timeout()
    {
        return timeouts.timeoutFor(defaultTimeout);
    }

    protected WebDriverLocatable.LocateTimeout createTimout()
    {
        return new WebDriverLocatable.LocateTimeout.Builder()
                .timeout(timeout())
                .pollInterval(timeouts.timeoutFor(TimeoutType.EVALUATION_INTERVAL))
                .build();
    }

    public boolean isPresent()
    {
        return locatable.isPresent(driver, createTimout());
    }

    public boolean isVisible()
    {
        WebElement element = waitForWebElement();
        return element.isDisplayed();
    }

    public boolean isEnabled()
    {
        return waitForWebElement().isEnabled();
    }

    public boolean isSelected()
    {
        return waitForWebElement().isSelected();
    }

    @Nullable
    @Override
    public String getId()
    {
        return getAttribute(ATTRIBUTE_ID);
    }

    @Nonnull
    @Override
    public Set<String> getCssClasses()
    {
        return Elements.getCssClasses(getAttribute(ATTRIBUTE_CLASS));
    }

    public boolean hasClass(@Nonnull final String className)
    {
        checkNotNull(className, "className");
        return Check.hasClass(className, waitForWebElement());
    }

    public String getAttribute(@Nonnull final String name)
    {
        return waitForWebElement().getAttribute(name);
    }

    public boolean hasAttribute(@Nonnull final String name, final String value)
    {
        return value.equals(getAttribute(name));
    }

    public String getText()
    {
        return waitForWebElement().getText();
    }

    @Nonnull
    public String getTagName()
    {
        return waitForWebElement().getTagName();
    }

    public String getValue()
    {
        return waitForWebElement().getAttribute("value");
    }

    @Nonnull
    @Override
    public Point getLocation()
    {
        return waitForWebElement().getLocation();
    }

    @Nonnull
    @Override
    public Dimension getSize()
    {
        return waitForWebElement().getSize();
    }

    @Nonnull
    public PageElement click()
    {
        waitForWebElement().click();
        return this;
    }

    @Nonnull
    public PageElement type(final CharSequence... keysToSend)
    {
        waitForWebElement().sendKeys(keysToSend);
        return this;
    }

    @Nonnull
    public PageElement select()
    {
        WebElement el = waitForWebElement();
        if (!el.isSelected()) {
            el.click();
        }
        return this;
    }

    @Nonnull
    public PageElement toggle()
    {
        WebElement el = waitForWebElement();
        el.click();
        return this;
    }

    @Nonnull
    public PageElement clear()
    {
        waitForWebElement().clear();
        return this;
    }

    @Nonnull
    public TimedElement timed()
    {
       return pageBinder.bind(WebDriverTimedElement.class, locatable, defaultTimeout);
    }

    @Nonnull
    public PageElementJavascript javascript()
    {
        return new WebDriverElementJavascript(this);
    }

    @Nonnull
    public PageElement find(@Nonnull By locator)
    {
        return pageBinder.bind(WebDriverElement.class, locator, locatable);
    }

    @Nonnull
    public PageElement find(@Nonnull By locator, @Nonnull TimeoutType timeoutType)
    {
        return pageBinder.bind(WebDriverElement.class, locator, locatable, timeoutType);
    }

    @Nonnull
    public <T extends PageElement> T find(@Nonnull By locator, @Nonnull Class<T> elementClass)
    {
        return pageBinder.bind(WebDriverElementMappings.findMapping(elementClass), locator, locatable);
    }

    @Nonnull
    public <T extends PageElement> T find(@Nonnull By locator, @Nonnull Class<T> elementClass, @Nonnull TimeoutType timeoutType)
    {
        return pageBinder.bind(WebDriverElementMappings.findMapping(elementClass), locator, locatable, timeoutType);
    }

    @Nonnull
    public List<PageElement> findAll(@Nonnull final By locator)
    {
        return findAll(locator, defaultTimeout);
    }

    @Nonnull
    public List<PageElement> findAll(@Nonnull By locator, @Nonnull TimeoutType timeoutType)
    {
        return findAll(locator, PageElement.class, timeoutType);
    }

    @Nonnull
    public <T extends PageElement> List<T> findAll(@Nonnull By locator, @Nonnull Class<T> elementClass)
    {
        return findAll(locator, elementClass, defaultTimeout);
    }

    @Nonnull
    public <T extends PageElement> List<T> findAll(@Nonnull By locator, @Nonnull Class<T> elementClass,
                                                   @Nonnull TimeoutType timeoutType)
    {
        List<T> elements = Lists.newLinkedList();
        List<WebElement> webElements = waitForWebElement().findElements(locator);

        for(int i = 0; i < webElements.size(); i++)
        {
            elements.add(pageBinder.bind(WebDriverElementMappings.findMapping(elementClass),
                    WebDriverLocators.list(webElements.get(i), locator, i, locatable), timeoutType));
        }
        return elements;
    }

    @Nonnull
    @Override
    public PageElement withTimeout(@Nonnull TimeoutType timeoutType)
    {
        if (this.defaultTimeout == timeoutType)
        {
            return this;
        }
        return pageBinder.bind(WebDriverElement.class, locatable, checkNotNull(timeoutType));
    }

    @Nonnull
    @Override
    public DefaultQuery search()
    {
        return pageBinder.bind(DefaultWebDriverQuery.class, this);
    }

    /**
     * This allows retrieving the web element from the page element.
     *
     * @return the web element that represents the page element.
     */
    @Nonnull
    public WebElement asWebElement()
    {
        return waitForWebElement();
    }

    @Nonnull
    public TimeoutType getDefaultTimeout()
    {
        return defaultTimeout;
    }

    @Override
    public String toString()
    {
        return "WebDriverElement[locatable=" + locatable + ",defaultTimeout=" + defaultTimeout + "]";
    }

    protected WebElement waitForWebElement()
    {
        return waitForWebElement(createTimout());
    }

    protected WebElement waitForWebElement(WebDriverLocatable.LocateTimeout timeout)
    {
        return (WebElement) locatable.waitUntilLocated(driver, timeout);
    }
}
