package com.atlassian.webdriver.testing.simpleserver;

import com.atlassian.pageobjects.DefaultProductInstance;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.testing.runner.ProductContextRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Product context runner extension that starts an embedded server before instantiating the product.
 *
 * @since 2.1
 * @see SimpleServer
 */
public class SimpleServerRunner extends ProductContextRunner
{

    private final SimpleServer server = new SimpleServer();

    /**
     * Constructor compatible with the underlying default JUnit4 runner.
     *
     * @throws org.junit.runners.model.InitializationError
     *          if the test class is malformed.
     */
    public SimpleServerRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    @Override
    protected TestedProduct<?> createProduct(Class<? extends TestedProduct<?>> testedProductClass)
    {
        final int port = startServer();
        return TestedProductFactory.create(testedProductClass, createProductInstance(port), null);
    }

    @Override
    protected Statement classBlock(RunNotifier notifier)
    {
        final Statement original = super.classBlock(notifier);
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                try
                {
                    original.evaluate();
                }
                finally
                {
                    shutdownServer();
                }
            }
        };
    }

    private int startServer()
    {
        try
        {
            server.startServer();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return server.getPort();
    }

    private ProductInstance createProductInstance(int port)
    {
        return new DefaultProductInstance("http://localhost:" + port, "testapp", port, "/");
    }

    private void shutdownServer() throws Exception
    {
        server.stopServer();
    }
}
