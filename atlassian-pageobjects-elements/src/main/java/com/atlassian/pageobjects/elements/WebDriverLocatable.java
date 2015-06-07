package com.atlassian.pageobjects.elements;

import com.atlassian.annotations.Internal;
import com.atlassian.pageobjects.elements.timeout.DefaultTimeouts;
import com.atlassian.util.concurrent.Nullable;
import com.google.common.base.Preconditions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * A {@code SearchContext} that can be located by WebDriver, capable of re-locating. A locatable consists of locator
 * used to locate itself, and the parent locatable that forms a search context, in which we locate this locatable.
 *
 * <p/>
 * Locatables form a list representing the parent-child relationship of SearchContexts. The root locatable
 * is always the locatable for WebDriver itself (think of it as a global search context).
 * To locate a SearchContext, first the parent is located then the locator is applied to it.
 *
 * @since 2.0
 */
@Internal
public interface WebDriverLocatable
{
    /**
     * Gets the WebDriver locator for this SearchContext.
     *
     * @return Locator, null if root.
     */
    @Nullable
    By getLocator();

    /**
     * The parent of this SearchContext.
     *
     * @return The locatable for the parent, null if root.
     */
    @Nullable
    WebDriverLocatable getParent();

    /**
     * Wait until this SearchContext represented by this locatable is located.
     *
     * @param driver           the {@link WebDriver} instance.
     * @param timeoutInSeconds timeout to wait until located, must be >= 0.
     * @return SearchContext
     * @throws NoSuchElementException if context could not be located before timeout expired
     * @deprecated use {@link #waitUntilLocated(WebDriver, WebDriverLocatable.LocateTimeout)} instead. Scheduled for
     * removal in 3.0
     */
    @Deprecated
    @Nonnull
    SearchContext waitUntilLocated(@Nonnull WebDriver driver, int timeoutInSeconds) throws NoSuchElementException;

    /**
     * Whether this SearchContext is present by given <tt>timeout</tt>.
     *
     * @param driver           the {@link WebDriver} instance.
     * @param timeoutInSeconds timeout to wait until parent is located, must be >= 0
     * @return <code>true</code> if SearchContext is located before the timeout expires, <code>false</code> otherwise.
     * @deprecated use {@link #isPresent(WebDriver, WebDriverLocatable.LocateTimeout)} instead. Scheduled for removal
     * in 3.0
     */
    @Deprecated
    boolean isPresent(@Nonnull WebDriver driver, int timeoutInSeconds);

    /**
     * Wait until the {@link SearchContext} represented by this locatable is located.
     *
     * @param driver  the {@link WebDriver} instance.
     * @param timeout LocateTimeout instance specifying the time to wait until located, must be >= 0.
     * @return SearchContext
     * @throws NoSuchElementException if context could not be located before timeout expired
     * @see LocateTimeout
     * @see SearchContext
     * @since 2.2
     */
    @Nonnull
    SearchContext waitUntilLocated(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout)
            throws NoSuchElementException;

    /**
     * Whether this {@link SearchContext} is present, given its parent is located by {@code timeout}.
     *
     * @param driver  the {@link WebDriver} instance.
     * @param timeout LocateTimeout instance specifying the time to wait until the parent is located, must be >= 0.
     * @return {@literal true} if the {@link SearchContext} is located before the timeout expires, {@literal false}
     * otherwise
     * @see LocateTimeout
     * @since 2.2
     */
    boolean isPresent(@Nonnull WebDriver driver, @Nonnull LocateTimeout timeout);


    /**
     * Provides information about timeout and poll interval to use while performing locatable operations.
     *
     * @since 2.2
     */
    public static final class LocateTimeout
    {
        public static Builder builder() {
            return new Builder();
        }

        public static LocateTimeout zero() {
            return new LocateTimeout(0, 1); // if timeout is 0, no poll should happen anyway
        }

        private final long timeout;
        private final long pollInterval;

        private LocateTimeout(long timeout, long pollInterval)
        {
            Preconditions.checkArgument(timeout >= 0, "timeout >= 0");
            Preconditions.checkArgument(pollInterval > 0, "pollInterval > 0");
            this.timeout = timeout;
            this.pollInterval = pollInterval;
        }

        public long timeout()
        {
            return timeout;
        }

        public long pollInterval()
        {
            return pollInterval;
        }

        public static final class Builder
        {
            private long timeout;
            private long pollInterval = DefaultTimeouts.DEFAULT_INTERVAL;


            public Builder timeout(long millis)
            {
                this.timeout = millis;
                return this;
            }

            public Builder timeout(long amount, TimeUnit unit)
            {
                return timeout(unit.toMillis(amount));
            }

            public Builder pollInterval(long millis)
            {
                this.pollInterval = millis;
                return this;
            }

            public Builder pollInterval(long amount, TimeUnit unit)
            {
                return pollInterval(unit.toMillis(amount));
            }

            public LocateTimeout build()
            {
                return new LocateTimeout(timeout, pollInterval);
            }
        }
    }
}
