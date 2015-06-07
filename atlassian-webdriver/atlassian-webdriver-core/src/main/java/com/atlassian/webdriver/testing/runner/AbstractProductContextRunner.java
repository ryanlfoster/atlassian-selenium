package com.atlassian.webdriver.testing.runner;

import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.inject.InjectionContext;
import com.atlassian.pageobjects.util.InjectingTestedProducts;
import org.junit.runners.model.InitializationError;

/**
 * <p/>
 * Given a tested product supporting injection, injects framework members into the test class and object
 * before running the test.
 *
 * <p/>
 * The underlying product, or its binder, MUST implement the {@link com.atlassian.pageobjects.inject.InjectionContext}
 * for this to work.
 *
 * @since 2.1
 */
public abstract class AbstractProductContextRunner extends AbstractInjectingRunner
{
    /**
     * Constructor compatible with the underlying default JUnit4 runner.
     *
     * @throws org.junit.runners.model.InitializationError
     *          if the test class is malformed.
     */
    public AbstractProductContextRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    @Override
    protected final InjectionContext getInjectionContext()
    {
        return InjectingTestedProducts.asInjectionContext(getProduct());
    }

    /**
     * The product must support injection, such that
     * {@link com.atlassian.pageobjects.util.InjectingTestedProducts#supportsInjection(com.atlassian.pageobjects.TestedProduct)}
     * returns <code>true</code>. Otherwise this runner will fail at runtime when trying to
     * perform injection.
     *
     * @return product instance
     */
    protected abstract TestedProduct<?> getProduct();
}
