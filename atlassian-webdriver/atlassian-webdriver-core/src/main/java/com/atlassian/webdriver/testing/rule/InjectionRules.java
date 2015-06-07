package com.atlassian.webdriver.testing.rule;

import com.atlassian.pageobjects.TestedProduct;
import com.google.common.base.Supplier;
import org.junit.rules.TestRule;

import static com.atlassian.pageobjects.TestedProductFactory.fromFactory;
import static com.google.common.base.Suppliers.memoize;

/**
 * <p/>
 * Static factory for a set of JUnit4-compatible injection rules.
 *
 * <p/>
 * Similar to {@link TestedProductRule}, but more powerful, this set of rules allows for injection of components
 * living in the context of a tested product or its associated page binder, into test classes and objects.
 *
 * <p/>
 * The product, or its page binder, MUST implement the {@link com.atlassian.pageobjects.inject.InjectionContext}
 * interface, otherwise no injection will be performed.
 *
 * <p/>
 * To achieve both static and member injection (which also implies that a single tested product instance will
 * be associated with test class, rather than each test object), combine {@link org.junit.ClassRule}
 * and @{@link org.junit.Rule} in the following pattern:
 *
 * <pre>
 *     &#064;ClassRule
 *     public static TestRule myProductStaticInjectionRule = InjectionRules.forTestClass(MyProduct.class);
 *
 *     &#064;Rule
 *     public TestRule myProductMemberInjectionRule = InjectionRules.forTestInContext(this);
 *
 *
 *     // (fields that will get injected)
 *     &#064;Inject
 *     private static MyProduct myProduct; // per class
 *
 *     &#064;Inject
 *     private PageBinder myPageBinder; // per test object instance, but the same binder instance
 *
 *     // etc...
 * </pre>
 *
 * <p/>
 * You may also choose to use the class rule only, if all you need is static members injection.
 *
 * <p/>
 * If you prefer having a new tested product created for each test method, use the following factory in conjunction with
 * {@link org.junit.Rule} annotation:
 *<pre>
 *     &#064;Rule
 *     public TestRule myProductMemberInjectionRule = TestInjectionRule.forTest(this, MyProduct.class);
 *
 *      // (fields that will get injected)
 *
 *     &#064;Inject
 *     private PageBinder myPageBinder; // per test object instance, but the same binder instance
 *
 *     // etc...
 * </pre>
 *
 * <p/>
 * An example test class
 *
 * <p/>
 * IMPORTANT: the methods {@link #forTestInContext(Object)} and {@link #forTest(Object, Class)} provide different
 * implementations of rules, the first of which depends on a class rule created via {@link #forTestClass(Class)}
 * being present on the test class. Please choose one of the above options for your test (or base test) depending whether
 * you need just static or just member injection, or both.
 *
 * @since 2.1
 */
public final class InjectionRules
{

    private InjectionRules()
    {
        throw new AssertionError("Don't instantiate me");
    }

    public static <T extends TestedProduct<?>> InjectingTestRule forTestClass(Class<T> productClass)
    {
        return new ClassInjectionRule<T>(memoize(fromFactory(productClass)));
    }

    public static <T extends TestedProduct<?>> InjectingTestRule forTestClass(Supplier<T> productSupplier)
    {
        return new ClassInjectionRule<T>(productSupplier);
    }

    public static TestRule forTestInContext(Object testInstance)
    {
        return new InstanceInjectionRules.InstanceCollaboratingInjectionRule(testInstance);
    }

    public static <T extends TestedProduct<?>> TestRule forTest(Object testInstance, Class<T> testedProductClass)
    {
        return new InstanceInjectionRules.InstanceStandaloneInjectionRule<T>(testInstance, memoize(fromFactory(testedProductClass)));
    }

    public static <T extends TestedProduct<?>> TestRule forTest(Object testInstance, Supplier<T> productSupplier)
    {
        return new InstanceInjectionRules.InstanceStandaloneInjectionRule<T>(testInstance, productSupplier);
    }
}
