package com.atlassian.webdriver.rule;

import org.junit.runners.model.Statement;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * For testing rules we need to be able invoke statements created by them and ignoring the resulting errors.
 *
 * @since 2.1
 */
public final class SafeStatementInvoker
{
    private boolean success = true;
    private Throwable error = null;

    private final Statement statement;

    public SafeStatementInvoker(Statement statement)
    {
        this.statement = checkNotNull(statement);
    }

    public void invokeSafely()
    {
        try
        {
            statement.evaluate();
        }
        catch (Throwable throwable)
        {
            this.error = throwable;
            this.success = false;
        }
    }

    public boolean isSuccess()
    {
        return success;
    }

    public Throwable getError()
    {
        return error;
    }
}
