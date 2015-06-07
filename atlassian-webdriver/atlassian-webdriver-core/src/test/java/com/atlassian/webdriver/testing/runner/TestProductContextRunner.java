package com.atlassian.webdriver.testing.runner;

import com.atlassian.pageobjects.Defaults;
import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.pageobjects.Tester;
import com.atlassian.pageobjects.inject.InjectionContext;
import com.atlassian.webdriver.matchers.LangMatchers;
import com.atlassian.webdriver.testing.annotation.TestedProductClass;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import javax.annotation.Nonnull;

import static com.atlassian.webdriver.matchers.ErrorMatchers.specificError;
import static com.atlassian.webdriver.matchers.ErrorMatchers.withCause;
import static com.atlassian.webdriver.matchers.ErrorMatchers.withCauses;
import static com.atlassian.webdriver.matchers.ErrorMatchers.withMessage;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link com.atlassian.webdriver.testing.runner.ProductContextRunner}
 *
 * @since 2.1
 */
public class TestProductContextRunner
{
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void doesNotSupportTestClassesNotAnnotatedWithTestedProductClass() throws InitializationError
    {
        expectedException.expect(specificError(InitializationError.class, withCauses(Matchers.<Throwable>hasItem(withMessage(
                NotAnnotated.class.getName(),
                "is missing the",
                TestedProductClass.class.getName(),
                "annotation"
        )))));
        new ProductContextRunner(NotAnnotated.class);
    }

    @Test
    public void supportsOnlyProductsThatAreInjectionContexts() throws InitializationError
    {
        expectedException.expect(withCause(Matchers.<Throwable>allOf(LangMatchers.<Throwable>isInstance(AssertionError.class), withMessage(
                "TestedProduct instance ",
                MockTestedProductNotInjectionContext.class.getName(),
                "does not support injection"
        ))));
        new ProductContextRunner(WithAnnotationNotInjectionContext.class).getProduct();
    }

    @Test
    public void createsTestClassAndInjectsProductImplementingInjectionContext() throws InitializationError
    {
        final ProductContextRunner runner = new ProductContextRunner(WithAnnotationProductThatIsInjectionContext.class);
        runner.run(new RunNotifier());
        assertThat(runner.getProduct(), instanceOf(MockTestedProductThatIsInjectionContext.class));
        MockTestedProductThatIsInjectionContext product = (MockTestedProductThatIsInjectionContext) runner.getProduct();
        assertThat(product.injectedClass, notNullValue());
        assertThat(product.injectedInstance, notNullValue());
    }

    @Test
    public void supportsTestedProductClassAnnotationInSuperclasses() throws InitializationError
    {
        final ProductContextRunner runner = new ProductContextRunner(ExtendingCorrectlyAnnotatedClass.class);
        runner.run(new RunNotifier());
        assertThat(runner.getProduct(), instanceOf(MockTestedProductThatIsInjectionContext.class));
        MockTestedProductThatIsInjectionContext product = (MockTestedProductThatIsInjectionContext) runner.getProduct();
        assertThat(product.injectedClass, notNullValue());
        assertThat(product.injectedInstance, notNullValue());
    }



    public static class NotAnnotated
    {
        @Test
        public void satisfiesJUnit() {}
    }

    @TestedProductClass(MockTestedProductNotInjectionContext.class)
    public static class WithAnnotationNotInjectionContext
    {

        @Test
        public void satisfiesJUnit() {}
    }

    @TestedProductClass(MockTestedProductThatIsInjectionContext.class)
    public static class WithAnnotationProductThatIsInjectionContext
    {

        @Test
        public void satisfiesJUnit() {}
    }

    public static class ExtendingCorrectlyAnnotatedClass extends WithAnnotationProductThatIsInjectionContext
    {

        @Test
        public void someOtherTest() {}
    }

    @Defaults(instanceId = "anapp", contextPath = "some-path", httpPort = 666)
    public static class MockTestedProductNotInjectionContext implements TestedProduct<Tester>
    {
        // TestedProductFactory-compatible
        public MockTestedProductNotInjectionContext(TestedProductFactory.TesterFactory<?> testerFactory, ProductInstance productInstance)
        {
        }

        @Override
        public <P extends Page> P visit(Class<P> pageClass, Object... args)
        {
            return null;
        }

        @Override
        public PageBinder getPageBinder()
        {
            return mock(PageBinder.class); // not an injection context
        }

        @Override
        public ProductInstance getProductInstance()
        {
            return null;
        }

        @Override
        public Tester getTester()
        {
            return null;
        }
    }


    @Defaults(instanceId = "anapp", contextPath = "some-path", httpPort = 666)
    public static class MockTestedProductThatIsInjectionContext implements TestedProduct<Tester>, InjectionContext
    {
        public Class<?> injectedClass;
        public Object injectedInstance;

        // TestedProductFactory-compatible
        public MockTestedProductThatIsInjectionContext(TestedProductFactory.TesterFactory<?> testerFactory, ProductInstance productInstance)
        {
        }

        @Override
        public <P extends Page> P visit(Class<P> pageClass, Object... args)
        {
            return null;
        }

        @Override
        public PageBinder getPageBinder()
        {
            return mock(PageBinder.class); // not an injection context
        }

        @Override
        public ProductInstance getProductInstance()
        {
            return null;
        }

        @Override
        public Tester getTester()
        {
            return null;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        @Nonnull
        public <T> T getInstance(@Nonnull Class<T> type)
        {
            return null;
        }

        @Override
        public void injectStatic(@Nonnull Class<?> targetClass)
        {
            this.injectedClass = targetClass;
        }

        @Override
        public void injectMembers(@Nonnull Object targetInstance)
        {
            this.injectedInstance = targetInstance;
        }

        @Nonnull
        @Override
        public <T> T inject(@Nonnull T target)
        {
            this.injectedInstance = target;
            return target;
        }
    }

}
