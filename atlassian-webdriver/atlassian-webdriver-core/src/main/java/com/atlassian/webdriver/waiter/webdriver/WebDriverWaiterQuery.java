package com.atlassian.webdriver.waiter.webdriver;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.webdriver.waiter.webdriver.retriever.WebElementRetriever;
import com.atlassian.webdriver.waiter.WaiterQuery;
import com.atlassian.webdriver.waiter.webdriver.function.ConditionFunction;
import com.atlassian.webdriver.waiter.ElementQuery;
import com.atlassian.webdriver.waiter.ExecutableWaiterQuery;
import com.atlassian.webdriver.waiter.FunctionQuery;
import com.atlassian.webdriver.waiter.Query;
import com.google.common.base.Function;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
class WebDriverWaiterQuery implements WaiterQuery
{
    private final WebDriverQueryBuilder queryBuilder;

    public WebDriverWaiterQuery(WebDriverQueryBuilder builder)
    {
        this.queryBuilder = builder;
    }

    public FunctionQuery function(ConditionFunction func)
    {
        return new WebDriverFunctionQuery(queryBuilder, func);
    }

    public ElementQuery element(final By locator)
    {
        return element(locator, queryBuilder.getDriver());
    }

    public ElementQuery element(final By locator, final SearchContext context)
    {
        return new WebDriverElementQuery(queryBuilder, WebElementRetriever.newLocatorRetriever(locator, context));
    }

    public ElementQuery element(final WebElement element)
    {
        return new WebDriverElementQuery(queryBuilder, WebElementRetriever.newWebElementRetriever(element));
    }

    static class WebDriverExecutableWaiterQuery
            implements ExecutableWaiterQuery
    {

        private final WebDriverQueryBuilder queryBuilder;

        public WebDriverExecutableWaiterQuery(WebDriverQueryBuilder queryBuilder) {
            this.queryBuilder = queryBuilder;
        }

        public void execute()
        {
            Function<WebDriver,Boolean> func = queryBuilder.build();
            new AtlassianWebDriverWait(queryBuilder.getDriver(), queryBuilder.getTimeout()).until(func);
        }

        public WaiterQuery and()
        {
            queryBuilder.add(new AndQuery());
            return new WebDriverWaiterQuery(queryBuilder);
        }

        public WaiterQuery or()
        {
            queryBuilder.add(new OrQuery());
            return new WebDriverWaiterQuery(queryBuilder);
        }
    }

    static class AndQuery implements Query {
        public ConditionFunction build()
        {
            throw new NotImplementedException("This should not be called.");
        }
    }

    static class OrQuery implements Query {
        public ConditionFunction build()
        {
            throw new NotImplementedException("This should not be called.");
        }
    }
}
