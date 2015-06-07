package com.atlassian.webdriver.waiter.webdriver.retriever;

import com.google.common.base.Preconditions;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @since 2.1.0
 */
public class WebElementRetriever
{
    private final Supplier<WebElement> elementSupplier;

    private WebElementRetriever(Supplier<WebElement> elementSupplier)
    {
        this.elementSupplier = Preconditions.checkNotNull(elementSupplier, "The element supplier can not be null.");
    }

    public static WebElementRetriever newLocatorRetriever(final By locator, final SearchContext context)
    {
        Preconditions.checkNotNull(locator, "The locator can not be null.");
        Preconditions.checkNotNull(context, "The search context can not be null.");
        return new WebElementRetriever(new Supplier<WebElement>()
        {
            @Override
            public WebElement supply()
            {
                return context.findElement(locator);
            }
        });
    }

    public static WebElementRetriever newWebElementRetriever(final WebElement webElement)
    {
        Preconditions.checkNotNull(webElement, "The webelement can not be null.");
        return new WebElementRetriever(new Supplier<WebElement>()
        {
            @Override
            public WebElement supply()
            {
                return webElement;
            }
        });
    }

    public WebElement retrieveElement()
    {
        return elementSupplier.supply();
    }
}
