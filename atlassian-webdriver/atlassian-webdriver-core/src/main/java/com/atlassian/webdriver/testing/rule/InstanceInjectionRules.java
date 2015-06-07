package com.atlassian.webdriver.testing.rule;

import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.inject.InjectionContext;
import com.atlassian.pageobjects.util.InjectingTestedProducts;
import com.google.common.base.Supplier;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Injections rules for test instances. Access via {@link com.atlassian.webdriver.testing.rule.InjectionRules}.
 *
 * @since 2.1
 */
final class InstanceInjectionRules
{
    private static final Logger logger = LoggerFactory.getLogger(InstanceInjectionRules.class);

    static abstract class AbstractInstanceInjectionRule implements TestRule
    {
        // we need to have this guy provided by the test due to https://github.com/KentBeck/junit/issues/351
        private final Object target;

        public AbstractInstanceInjectionRule(Object target)
        {
            this.target = checkNotNull(target, "target");
        }

        @Override
        public final Statement apply(final Statement base, final Description description)
        {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable
                {
                    if (supportsInjection(base, description))
                    {
                        injectionContext(base, description).injectMembers(target);
                    }
                    base.evaluate();
                }
            };
        }

        protected abstract boolean supportsInjection(Statement base, Description description);

        protected abstract InjectionContext injectionContext(Statement base, Description description);

    }

    static final class InstanceStandaloneInjectionRule<T extends TestedProduct<?>> extends AbstractInstanceInjectionRule
    {
        private final Supplier<T> product;

        InstanceStandaloneInjectionRule(Object target, Supplier<T> productSupplier)
        {
            super(target);
            this.product = checkNotNull(productSupplier, "productSupplier");
        }

        @Override
        protected boolean supportsInjection(Statement base, Description description)
        {
            return InjectingTestedProducts.supportsInjection(product.get());
        }

        @Override
        protected InjectionContext injectionContext(Statement base, Description description)
        {
            return InjectingTestedProducts.asInjectionContext(product.get());
        }
    }

    /**
     * An injection rule requiring the class injection rule to be present in the context.
     */
    static final class InstanceCollaboratingInjectionRule extends AbstractInstanceInjectionRule
    {
        InstanceCollaboratingInjectionRule(Object target)
        {
            super(target);
        }

        @Override
        protected boolean supportsInjection(Statement base, Description description)
        {
            return findClassInjectionRule(description).supportsInjection();
        }

        @Override
        protected InjectionContext injectionContext(Statement base, Description description)
        {
            return findClassInjectionRule(description).injectionContext();
        }

        private ClassInjectionRule<?> findClassInjectionRule(Description description)
        {
            final List<ClassInjectionRule> rules = getClassInjectionRules(description);
            if (rules.isEmpty())
            {
                throw new IllegalStateException("Test class configuration invalid: to use context-aware members injection " +
                        "rule you must declare a class rule as well. See JavaDoc of the " +
                        "com.atlassian.webdriver.testing.rule.InjectionRules class");
            }
            if (rules.size() > 1)
            {
                logger.warn("More than one field of type " + ClassInjectionRule.class.getName() + " annotated with " +
                        "@ClassRule. Using the first one to retrieve injection context");
            }
            return rules.get(0);
        }

        private List<ClassInjectionRule> getClassInjectionRules(Description description)
        {
            return new TestClass(description.getTestClass())
                    .getAnnotatedFieldValues(null, ClassRule.class, ClassInjectionRule.class);
        }
    }

}
