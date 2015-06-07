package com.atlassian.webdriver.testing.runner;

import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.pageobjects.util.InjectingTestedProducts;
import com.atlassian.util.concurrent.LazyReference;
import com.atlassian.webdriver.testing.annotation.TestedProductClass;
import org.junit.runners.model.InitializationError;

/**
 * <p/>
 * 'Default' product context runner that reads the tested product class from the
 * {@link com.atlassian.webdriver.testing.annotation.TestedProductClass} annotation that the test class
 * MUST be annotated with, and uses {@link com.atlassian.pageobjects.TestedProductFactory} to instantiate
 * the product and inject context into the test instance.
 *
 * @since 2.1
 */
public class ProductContextRunner extends AbstractProductContextRunner
{

    private final Class<? extends TestedProduct<?>> productClass;
    private final LazyReference<TestedProduct<?>> product;

    /**
     * Constructor compatible with the underlying default JUnit4 runner.
     *
     * @throws org.junit.runners.model.InitializationError
     *          if the test class is malformed.
     */
    public ProductContextRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        this.productClass = validateAndGetProductClass(klass);
        this.product = new LazyReference<TestedProduct<?>>()
        {
            @Override
            protected TestedProduct<?> create() throws Exception
            {
                final TestedProduct<?> theProduct = createProduct(productClass);
                if (!InjectingTestedProducts.supportsInjection(theProduct))
                {
                    throw new AssertionError("TestedProduct instance " + theProduct.getClass().getName() + " does not "
                            + "support injection");
                }
                return theProduct;
            }
        };

    }

    /**
     * Override to implement custom factory method for tested product.
     *
     * @param testedProductClass tested product class
     * @return tested product instance
     */
    protected TestedProduct<?> createProduct(Class<? extends TestedProduct<?>> testedProductClass)
    {
        return TestedProductFactory.create(testedProductClass);
    }

    private Class<? extends TestedProduct<?>> validateAndGetProductClass(Class<?> testClass) throws InitializationError
    {
        final TestedProductClass testedProductClass = testClass.getAnnotation(TestedProductClass.class);
        if (testedProductClass == null)
        {
            throw new InitializationError("Test class " + testClass.getName() + " is missing the "
                    + TestedProductClass.class.getName() + " annotation");
        }
        return testedProductClass.value();

    }

    @Override
    protected final TestedProduct<?> getProduct()
    {
        return product.get();
    }
}
