package com.atlassian.webdriver.waiter.webdriver;

import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.webdriver.waiter.webdriver.retriever.WebElementFieldRetriever;
import com.atlassian.webdriver.waiter.webdriver.function.ConditionFunction;
import com.atlassian.webdriver.waiter.ExecutableWaiterQuery;
import com.atlassian.webdriver.waiter.Query;
import com.atlassian.webdriver.waiter.StringValueQuery;
import com.atlassian.webdriver.waiter.webdriver.function.NotFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.ContainsFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.EndsWithFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.EqualsIgnoresCaseFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.IsEmptyFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.IsEqualFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.MatchesFunction;
import com.atlassian.webdriver.waiter.webdriver.function.field.StartsWithFunction;

/**
 *
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public class WebDriverFieldQuery implements StringValueQuery
{
    private final WebDriverQueryBuilder queryBuilder;
    private final WebElementFieldRetriever fieldRetriever;

    public WebDriverFieldQuery(WebDriverQueryBuilder queryBuilder,
            WebElementFieldRetriever fieldRetriever)
    {
        this.queryBuilder = queryBuilder;
        this.fieldRetriever = fieldRetriever;
    }

    public ExecutableWaiterQuery contains(final String value)
    {
        return createExecutableWaiterQuery(new ContainsFunction(fieldRetriever, value));
    }

    public ExecutableWaiterQuery notContains(final String value)
    {
        return createExecutableWaiterQuery(not(new ContainsFunction(fieldRetriever, value)));
    }

    public ExecutableWaiterQuery isEqual(final String value)
    {
        return createExecutableWaiterQuery(new IsEqualFunction(fieldRetriever, value));
    }

    public ExecutableWaiterQuery notEqual(final String value)
    {
        return createExecutableWaiterQuery(not(new IsEqualFunction(fieldRetriever, value)));
    }

    public ExecutableWaiterQuery isEmpty()
    {
        return createExecutableWaiterQuery(new IsEmptyFunction(fieldRetriever));
    }

    public ExecutableWaiterQuery isNotEmpty()
    {
        return createExecutableWaiterQuery(not(new IsEmptyFunction(fieldRetriever)));
    }

    public ExecutableWaiterQuery endsWith(final String value)
    {
        return createExecutableWaiterQuery(new EndsWithFunction(fieldRetriever, value));
    }

    public ExecutableWaiterQuery doesNotEndWith(final String value)
    {
        return createExecutableWaiterQuery(not(new EndsWithFunction(fieldRetriever, value)));
    }

    public ExecutableWaiterQuery matches(final String value)
    {
        return createExecutableWaiterQuery(new MatchesFunction(fieldRetriever, value));
    }

    public ExecutableWaiterQuery doesNotMatch(final String value)
    {
        return createExecutableWaiterQuery(not(new MatchesFunction(fieldRetriever, value)));
    }

    public ExecutableWaiterQuery startsWith(final String value)
    {
        return createExecutableWaiterQuery(new StartsWithFunction(fieldRetriever, value));
    }

    public ExecutableWaiterQuery doesNotStartWith(final String value)
    {
        return createExecutableWaiterQuery(not(new StartsWithFunction(fieldRetriever, value)));
    }

    public ExecutableWaiterQuery equalsIgnoresCase(final String value)
    {
        return createExecutableWaiterQuery(new EqualsIgnoresCaseFunction(fieldRetriever, value));
    }

    private NotFunction not(ConditionFunction func)
    {
        return new NotFunction(func);
    }

    private ExecutableWaiterQuery createExecutableWaiterQuery(final ConditionFunction func)
    {
        queryBuilder.add(new Query()
        {
            @Override
            public ConditionFunction build()
            {
                return func;
            }
        });
        return new WebDriverWaiterQuery.WebDriverExecutableWaiterQuery(queryBuilder);
    }

}
