package com.atlassian.webdriver.testing.rule;

import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A template for a {@link TestRule} allowing to execute code around the {@link Statement#evaluate()} call.
 * <p>
 * Same as {@link ExternalResource}, but does not allow {@link #after()} to escape in case <i>Statement#evaluate()}</i>
 * escaped. A potential exception caused an <i>after()</i> call is logged instead.
 * 
 * @since 2.1
 */
public class FailsafeExternalResource extends ExternalResource
{
    private static final Logger log = LoggerFactory.getLogger(FailsafeExternalResource.class);

    public final Statement apply(final Statement base, Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                before();
                try
                {
                    base.evaluate();
                }
                catch (Throwable t)
                {
                    try
                    {
                        after();
                    }
                    catch (RuntimeException e)
                    {
                        log.error(e.getMessage(), e);
                    }
                    throw t;
                }
                // evaluation didn't escape, after is allowed to escape
                after();
            }
        };
    }

}
