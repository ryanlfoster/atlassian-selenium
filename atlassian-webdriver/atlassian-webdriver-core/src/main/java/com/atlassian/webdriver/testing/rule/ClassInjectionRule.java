package com.atlassian.webdriver.testing.rule;

import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.inject.InjectionContext;
import com.atlassian.pageobjects.util.InjectingTestedProducts;
import com.google.common.base.Supplier;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p/>
 * Class injection rule. Access via {@link com.atlassian.webdriver.testing.rule.InjectionRules}.
 *
 * <p/>
 * Also implement {@link com.atlassian.pageobjects.inject.InjectionContext} for convenience in tests.
 *
 * @since 2.1
 */
public final class ClassInjectionRule<P extends TestedProduct<?>> implements InjectingTestRule
{
    private final Supplier<P> product;

    ClassInjectionRule(Supplier<P> productSupplier)
    {
        this.product = checkNotNull(productSupplier, "productSupplier");
    }


    @Override
    public Statement apply(final Statement base, final Description description)
    {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable
            {
                if (supportsInjection())
                {
                    injectionContext().injectStatic(description.getTestClass());
                }
                base.evaluate();
            }
        };
    }

    boolean supportsInjection()
    {
        return InjectingTestedProducts.supportsInjection(product.get());
    }

    InjectionContext injectionContext()
    {
        return InjectingTestedProducts.asInjectionContext(product.get());
    }

    @Override
    @Nonnull
    public <T> T getInstance(@Nonnull Class<T> type)
    {
        return injectionContext().getInstance(type);
    }

    @Override
    public void injectStatic(@Nonnull Class<?> targetClass)
    {
        injectionContext().injectStatic(targetClass);
    }

    @Override
    public void injectMembers(@Nonnull Object targetInstance)
    {
        injectionContext().inject(targetInstance);
    }

    @Nonnull
    @Override
    public <T> T inject(@Nonnull T target)
    {
        return injectionContext().inject(target);
    }
}
