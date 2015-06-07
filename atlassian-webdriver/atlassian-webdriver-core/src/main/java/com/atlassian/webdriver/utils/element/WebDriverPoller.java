package com.atlassian.webdriver.utils.element;

import com.atlassian.pageobjects.PageBinder;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p/>
 * A component that can be used to wait for certain conditions to happen on the tested page.
 *
 * <p/>
 * The conditions are expressed as a generic {@link Function} from {@link WebDriver} to {@code boolean}.
 * {@link ElementConditions} contains factory methods to easily create some commonly used conditions.
 *
 * <p/>
 * The {@link #DEFAULT_TIMEOUT} and {@link #DEFAULT_TIMEOUT_UNIT} specify the default timeout used when
 * no explicit timeout is provided by the client, which is currently 30 seconds. Clients are encouraged to use
 * their own timeout specific to the situation.
 *
 * <p/>
 * NOTE: the default poll interval used by this class is as in the underlying {@link WebDriverWait} and is currently
 * 500ms (subject to change as the underlying {@link WebDriverWait} implementation changes. This may be generally
 * acceptable, but may not be granular enough for some scenarios (e.g. performance testing).
 *
 * <p/>
 * Example usage:
 * <pre>
 *     {@code
 *     @literal @Inject private WebDriverPoller poller;
 *
 *     // ...
 *     // wait for 5s for a 'my-element' to be present on the page
 *     poller.waitUntil(ElementConditions.isPresent(By.id("my-element")), 5);
 *     }
 * </pre>
 *
 * <p/>
 * As of 2.3, {@code WebDriverPoller} also supports waiting for {@code WebElement}-specific predicates and matchers,
 * which require the web element to be already located.
 * <p/>
 * This component can be injected into page objects running within a {@link PageBinder} context.
 *
 * <p/>
 * For more sophisticated polling/waiting toolkit, check the {@code PageElement} API in the
 * atlassian-pageobjects-elements module.
 *
 * @since 2.2
 * @see ElementConditions
 * @see WebDriverWait
 */
public final class WebDriverPoller
{
    public static final long DEFAULT_TIMEOUT = 30;
    public static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;

    private final WebDriver webDriver;
    private final TimeUnit timeUnit;
    private final long timeout;

    @Inject
    public WebDriverPoller(@Nonnull WebDriver webDriver)
    {
        this(webDriver, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT);
    }

    public WebDriverPoller(@Nonnull WebDriver webDriver, long timeout, @Nonnull TimeUnit timeUnit)
    {
        checkArgument(timeout > 0, "Timeout must be >0");
        this.webDriver = checkNotNull(webDriver, "webDriver");
        this.timeout = timeout;
        this.timeUnit = checkNotNull(timeUnit, "timeUnit");
    }

    @Nonnull
    public WebDriverPoller withDefaultTimeout(long timeout, @Nonnull TimeUnit timeUnit)
    {
        return new WebDriverPoller(webDriver, timeout, timeUnit);
    }

    /**
     * Wait until {@literal condition} is {@literal true}, up to the default timeout. The default timeout depends
     * on the arguments supplied while creating this {@code WebDriverPoller}.
     *
     * @param condition condition that must evaluate to {@literal true}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @see #DEFAULT_TIMEOUT
     * @see #DEFAULT_TIMEOUT_UNIT
     */
    public void waitUntil(@Nonnull Function<WebDriver, Boolean> condition)
    {
        waitUntil(condition, timeout, timeUnit);
    }

    /**
     * Wait until {@literal condition} up to the {@literal timeoutInSeconds}.
     *
     * @param condition condition that must evaluate to {@literal true}
     * @param timeoutInSeconds timeout in seconds to wait for {@literal condition} to come {@code true}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     */
    public void waitUntil(@Nonnull Function<WebDriver, Boolean> condition, long timeoutInSeconds)
    {
        new WebDriverWait(webDriver, timeoutInSeconds).until(condition);
    }

    /**
     * Wait until {@literal condition} up to the {@literal timeout} specified by {@literal unit}.
     *
     * @param condition condition that must evaluate to {@literal true}
     * @param timeout timeout to wait for {@literal condition} to come true
     * @param unit unit of the {@literal timeout}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     */
    public void waitUntil(@Nonnull Function<WebDriver, Boolean> condition, long timeout, @Nonnull TimeUnit unit)
    {
        waitUntil(condition, unit.toSeconds(timeout));
    }

    /**
     * Wait until {@literal condition} is {@code true} for {@code element}, up to the default timeout. The default
     * timeout depends on the arguments supplied while creating this {@code WebDriverPoller}.
     *
     * @param element the element to examine
     * @param condition condition that must evaluate to {@literal true}, expressed by a {@link Predicate}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @since 2.3
     *
     * @see #DEFAULT_TIMEOUT
     * @see #DEFAULT_TIMEOUT_UNIT
     */
    public void waitUntil(@Nonnull WebElement element, @Nonnull Predicate<WebElement> condition)
    {
        waitUntil(element, condition, timeout, timeUnit);
    }

    /**
     * Wait until {@literal condition} is {@code true} for {@code element}, up to the {@literal timeoutInSeconds}.
     *
     * @param element the element to examine
     * @param condition condition that must evaluate to {@literal true}, expressed by a {@link Predicate}
     * @param timeoutInSeconds timeout in seconds to wait for {@literal condition} to come {@code true}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @since 2.3
     */
    public void waitUntil(@Nonnull WebElement element, @Nonnull Predicate<WebElement> condition, long timeoutInSeconds)
    {
        waitUntil(element, condition, timeoutInSeconds, TimeUnit.SECONDS);
    }

    /**
     * Wait until {@literal condition} is {@code true} for {@code element}, up to the {@literal timeout} specified
     * by {@literal unit}.
     *
     * @param element the element to examine
     * @param condition condition that must evaluate to {@literal true}, expressed by a {@link Predicate}
     * @param timeout timeout to wait for {@literal condition} to come true
     * @param unit unit of the {@literal timeout}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @since 2.3
     */
    public void waitUntil(@Nonnull WebElement element, @Nonnull Predicate<WebElement> condition,
                          long timeout, TimeUnit unit)
    {
        new FluentWait<WebElement>(checkNotNull(element, "element")).withTimeout(timeout, unit).until(condition);
    }

    /**
     * Wait until {@literal condition} is {@code true} for {@code element}, up to the default timeout. The default
     * timeout depends on the arguments supplied while creating this {@code WebDriverPoller}.
     *
     * @param element the element to examine
     * @param condition condition that must evaluate to {@code true}, expressed by a {@code Matcher}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @since 2.3
     *
     * @see #DEFAULT_TIMEOUT
     * @see #DEFAULT_TIMEOUT_UNIT
     */
    public void waitUntil(@Nonnull WebElement element, @Nonnull Matcher<? super WebElement> condition)
    {
        waitUntil(element, condition, timeout, timeUnit);
    }

    /**
     * Wait until {@code condition} is {@code true} for {@code element}, up to the {@code timeoutInSeconds}.
     *
     * @param element the element to examine
     * @param condition condition that must evaluate to {@code true}, expressed by a {@link Matcher}
     * @param timeoutInSeconds timeout in seconds to wait for {@code condition} to come {@code true}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @since 2.3
     */
    public void waitUntil(@Nonnull WebElement element, @Nonnull Matcher<? super WebElement> condition,
                          long timeoutInSeconds)
    {
        waitUntil(element, condition, timeoutInSeconds, TimeUnit.SECONDS);
    }

    /**
     * Wait until {@literal condition} is {@code true} for {@code element}, up to the {@code timeout} specified
     * by {@code unit}.
     *
     * @param element the element to examine
     * @param condition condition that must evaluate to {@code true}, expressed by a {@link Matcher}
     * @param timeout timeout to wait for {@code condition} to come true
     * @param unit unit of the {@code timeout}
     * @throws TimeoutException if the condition does not come true before the timeout expires
     * @since 2.3
     */
    public void waitUntil(@Nonnull WebElement element, @Nonnull Matcher<? super WebElement> condition,
                          long timeout, TimeUnit unit)
    {
        waitUntil(element, newMatcherPredicate(condition), timeout, unit);
    }

    static <E> Predicate<E> newMatcherPredicate(final Matcher<? super E> filter)
    {
        return new Predicate<E>()
        {
            @Override
            public boolean apply(E element)
            {
                return filter.matches(element);
            }
        };
    }
}
