package com.atlassian.pageobjects.internal.elements.search;

import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.WebDriverElement;
import com.atlassian.pageobjects.elements.WebDriverLocatable;
import com.atlassian.pageobjects.elements.WebDriverLocatable.LocateTimeout;
import com.atlassian.pageobjects.elements.WebDriverLocators;
import com.atlassian.pageobjects.elements.search.AnyQuery;
import com.atlassian.pageobjects.elements.search.PageElementQuery;
import com.atlassian.pageobjects.elements.timeout.TimeoutType;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang.mutable.MutableInt;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebDriverElementQuery<E extends PageElement> extends AbstractSearchQuery<E, PageElementQuery<E>>
        implements PageElementQuery<E>
{
    @Inject
    protected WebDriver webDriver;

    protected final Class<E> pageElementClass;
    protected final TimeoutType timeoutType;

    public WebDriverElementQuery(@Nonnull Supplier<Iterable<E>> querySupplier, @Nonnull Class<E> pageElementClass,
                                 @Nonnull TimeoutType timeoutType)
    {
        super(querySupplier);
        this.pageElementClass = checkNotNull(pageElementClass, "pageElementClass");
        this.timeoutType = checkNotNull(timeoutType, "timeoutType");
    }

    @Nonnull
    @Override
    public PageElementQuery<E> by(@Nonnull By by)
    {
        return by(by, Predicates.alwaysTrue());
    }

    @Nonnull
    @Override
    public PageElementQuery<E> by(@Nonnull By by, @Nonnull Predicate<? super PageElement> filter)
    {
        return newInstance(flatMapSupplier(searchAndFilter(by, filter)));
    }

    @Nonnull
    @Override
    public PageElementQuery<E> withTimeout(@Nonnull TimeoutType timeoutType)
    {
        return newInstance(querySupplier, pageElementClass, timeoutType).rebind();
    }

    @Nonnull
    @Override
    public <PE extends E> PageElementQuery<PE> as(@Nonnull Class<PE> pageElementClass)
    {
        return newInstance(querySupplier, pageElementClass, timeoutType).rebind();
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    protected <F> AnyQuery<F> newAnyQueryInstance(@Nonnull Supplier<Iterable<F>> supplier)
    {
        return pageBinder.bind(DefaultAnyQuery.class, supplier);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    protected PageElementQuery<E> newInstance(@Nonnull Supplier<Iterable<E>> supplier)
    {
        return newInstance(supplier, pageElementClass, timeoutType);
    }

    @SuppressWarnings("unchecked")
    private <PE extends E> WebDriverElementQuery<PE> newInstance(Supplier<Iterable<E>> supplier, Class<PE> pageElementClass,
                                                           TimeoutType timeoutType)
    {
        return pageBinder.bind(WebDriverElementQuery.class,
                checkNotNull(supplier, "supplier"),
                checkNotNull(pageElementClass, "pageElementClass"),
                checkNotNull(timeoutType, "timeoutType"));
    }

    private static Function<WebElement, WebDriverLocatable> toListLocatable(final By locator, final MutableInt index,
                                                                            final WebDriverLocatable parent)
    {
        return new Function<WebElement, WebDriverLocatable>()
        {
            @Override
            public WebDriverLocatable apply(WebElement element)
            {
                WebDriverLocatable result = WebDriverLocators.list(element, locator, index.intValue(), parent);
                index.increment();
                return result;

            }
        };
    }

    private PageElementQuery<E> rebind()
    {
        return newInstance(mapSupplier(rebindFunction()));
    }

    private Function<? super E, E> rebindFunction()
    {
        // rebind by turning into locatable (2nd function) and then back into WebDriverElement with local element class
        // and timeout type
        return Functions.compose(
                WebDriverElement.bind(pageBinder, pageElementClass, timeoutType),
                WebDriverElement.TO_LOCATABLE
        );
    }

    private Function<? super E, Iterable<E>> searchAndFilter(final By by, final Predicate<? super PageElement> filter)
    {
        return new Function<E, Iterable<E>>()
        {
            @Nullable
            @Override
            public Iterable<E> apply(E element)
            {
                try
                {
                    // we need to transform into locatable and then back to page element to apply the predicate - lame,
                    // but for now we cannot rely on PageElementFinder interface as the implementation has implicit
                    // waits (see SELENIUM-253)
                    WebDriverLocatable root = WebDriverElement.toLocatable(element);
                    SearchContext searchContext = root.waitUntilLocated(webDriver, LocateTimeout.zero());

                    return FluentIterable.from(searchContext.findElements(by))
                            .transform(toListLocatable(by, new MutableInt(), root))
                            .transform(WebDriverElement.bind(pageBinder, pageElementClass, timeoutType))
                            .filter(filter)
                            .toList();
                }
                // if search fails for WebDriver reasons (elements dynamically removed etc.), just return no results
                catch (NoSuchElementException e)
                {
                    return Collections.emptyList();
                }
                catch (StaleElementReferenceException e)
                {
                    return Collections.emptyList();
                }
            }
        };
    }
}
