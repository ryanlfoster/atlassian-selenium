package com.atlassian.pageobjects.binder;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.Tester;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.browser.RequireBrowser;
import com.atlassian.pageobjects.inject.ConfigurableInjectionContext;
import com.google.inject.Binder;
import com.google.inject.Module;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class TestInjectPageBinder
{
    private MyTestedProduct product;

    @Mock
    private ProductInstance productInstance;

    @Mock
    private Tester tester;

    @Before
    public void setUp() throws Exception
    {
        product = new MyTestedProduct(new SetTester());
    }

    @Test
    public void testInject()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        OneFieldPage page = binder.bind(OneFieldPage.class);
        assertEquals("Bob", page.name.getValue());
    }

    @Test
    public void testInjectDefaults()
    {
        PageBinder binder = createBinder();
        DefaultsPage page = binder.bind(DefaultsPage.class);
        assertNotNull(page.testedProduct);
        assertNotNull(page.myTestedProduct);
        assertNotNull(page.tester);
        assertNotNull(page.setTester);
        assertNotNull(page.pageBinder);
        assertNotNull(page.productInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInjectMissing()
    {
        PageBinder binder = createBinder();
        binder.bind(OneFieldPage.class);
    }

    @Test
    public void testInjectWithArgument()
    {
        PageBinder binder = createBinder();
        ConstructorArgumentPage page = binder.bind(ConstructorArgumentPage.class, "foo");
        assertEquals("foo", page.name);
    }

    @Test
    public void testInstantiateWithPrimitiveArguments()
    {
        PageBinder binder = createBinder();
        ConstructorArgumentPrimitive object = binder.bind(ConstructorArgumentPrimitive.class, 5, true);
        assertNotNull(object);
        assertEquals(5, object.intField);
        assertTrue(object.booleanField);
    }

    @Test
    public void testInjectWithArgumentSubclass()
    {
        PageBinder binder = createBinder();
        ConstructorArgumentPage page = binder.bind(ConstructorArgumentPage.class, 43);
        assertEquals(43, page.age);
    }

    @Test
    public void testInitAfterInject()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        OneFieldWithInitPage page = binder.bind(OneFieldWithInitPage.class);
        assertEquals("Bob Jones", page.name);
    }

    @Test
    public void testPrivateInitAfterInject()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        OneFieldWithPrivateInitPage page = binder.bind(OneFieldWithPrivateInitPage.class);
        assertEquals("Bob Private", page.name);
    }

    @Test
    public void testOneFieldWithSuperClassInit()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        OneFieldWithSuperClassInitPage page = binder.bind(OneFieldWithSuperClassInitPage.class);
        assertEquals("Bob Private", page.getName());

    }

    @Test
    public void testProtectedInitAfterInject()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        OneFieldWithProtectedInitPage page = binder.bind(OneFieldWithProtectedInitPage.class);
        assertEquals("Bob Protected", page.name);
    }

    @Test
    public void testParentInject()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        ChildNoNamePage page = binder.bind(ChildNoNamePage.class);
        assertEquals("Bob", page.name.getValue());
    }

    @Test
    public void shouldImplementConfigurableInjectionContext()
    {
        final PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        assertThat(binder, CoreMatchers.instanceOf(ConfigurableInjectionContext.class));
        assertEquals("Bob", binder.bind(OneFieldPage.class).name.getValue());
        ConfigurableInjectionContext.class.cast(binder)
                .configure()
                .addImplementation(StringField.class, AnotherStringFieldImpl.class)
                .finish();
        assertEquals("Rob", binder.bind(OneFieldPage.class).name.getValue());
    }

    @Test
    public void shouldAllowConfiguringNewSingletonInstanceThatIsSubclassOfInterfaceType()
    {
        final PageBinder binder = createBinder();
        ConfigurableInjectionContext.class.cast(binder)
                .configure()
                .addSingleton(StringField.class, new StringFieldImpl())
                .finish();
        assertEquals("Bob", binder.bind(OneFieldPage.class).name.getValue());
    }

    @Test
    public void shouldAllowConfiguringNewImplementationInstance()
    {
        PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        assertEquals("Bob", binder.bind(OneFieldPage.class).name.getValue());
        ConfigurableInjectionContext.class.cast(binder)
                .configure()
                .addSingleton(StringField.class, new StringField()
                {
                    @Override
                    public String getValue()
                    {
                        return "Boom!";
                    }
                })
                .finish();
        assertEquals("Boom!", binder.bind(OneFieldPage.class).name.getValue());
    }

    @Test
    public void shouldIncludePostInjectionProcessorsAddedViaInjectionContext()
    {
        PageBinder binder = createBinder();
        assertEquals("Default", binder.bind(MutablePage.class).getValue());

        ConfigurableInjectionContext.class.cast(binder)
                .configure()
                .addSingleton(MutablePageProcessor.class, new MutablePageProcessor())
                .finish();

        // post processor should be invoked
        assertEquals("Post processed", binder.bind(MutablePage.class).getValue());
    }

    @Test
    public void visitUrlShouldRemoveExtraSlashAfterHostname() throws Exception
    {
        when(productInstance.getBaseUrl()).thenReturn("http://localhost/");

        final PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        binder.navigateToAndBind(OneFieldPage.class);

        verify(tester).gotoUrl("http://localhost/path");
    }

    @Test
    public void visitUrlShouldAddMissingSlashAfterHostname() throws Exception
    {
        when(productInstance.getBaseUrl()).thenReturn("http://localhost");

        final PageBinder binder = createBinder(StringField.class, StringFieldImpl.class);
        binder.navigateToAndBind(PageWithNoLeadingSlash.class);

        verify(tester).gotoUrl("http://localhost/path");
    }

    @Test
    public void shouldCheckForIgnoreBrowserAndRequireBrowserAnnotation()
    {
        // should invoke all init methods
        PageObjectWithRequiredAndIgnoredBrowsers pageObject = createBinderWithBrowser(Browser.FIREFOX)
                .bind(PageObjectWithRequiredAndIgnoredBrowsers.class);

        assertTrue(pageObject.initIgnoredInvoked);
        assertTrue(pageObject.initRequiredInvoked);

        // should _not_ invoke init ignored
        pageObject = createBinderWithBrowser(Browser.HTMLUNIT).bind(PageObjectWithRequiredAndIgnoredBrowsers.class);

        assertFalse(pageObject.initIgnoredInvoked);
        assertTrue(pageObject.initRequiredInvoked);

        // should _not_ invoke init required
        pageObject = createBinderWithBrowser(Browser.SAFARI).bind(PageObjectWithRequiredAndIgnoredBrowsers.class);

        assertTrue(pageObject.initIgnoredInvoked);
        assertFalse(pageObject.initRequiredInvoked);

        // should _not_ invoke any init
        pageObject = createBinderWithBrowser(Browser.HTMLUNIT_NOJS).bind(PageObjectWithRequiredAndIgnoredBrowsers.class);

        assertFalse(pageObject.initIgnoredInvoked);
        assertFalse(pageObject.initRequiredInvoked);
    }

    @Test
    public void shouldSupportIgnoreAllAndRequireAll()
    {
        for (Browser browser : Browser.values())
        {
            PageObjectWithRequiredAndIgnoredBrowsers pageObject = createBinderWithBrowser(browser)
                    .bind(PageObjectWithRequiredAndIgnoredBrowsers.class);

            assertFalse(pageObject.initIgnoreAllInvoked);
            assertTrue(pageObject.initRequireAllInvoked);
        }
    }

    private InjectPageBinder createBinder()
    {
        return createBinder(null, null);
    }

    private InjectPageBinder createBinder(final Class<?> key, final Class impl)
    {
        return new InjectPageBinder(productInstance, tester, new StandardModule(product),
                new Module()
                {
                    public void configure(Binder binder)
                    {
                        if (key != null)
                        {
                            binder.bind(key).to(impl);
                        }
                    }
                });
    }

    private InjectPageBinder createBinderWithBrowser(Browser browser)
    {
        InjectPageBinder pageBinder = createBinder();
        ConfigurableInjectionContext.class.cast(pageBinder).configure()
                .addSingleton(Browser.class, browser).finish();

        return pageBinder;
    }

    static class AbstractPage implements Page
    {
        public String getUrl()
        {
            return "/path";
        }
    }

    static class OneFieldPage extends AbstractPage
    {
        @Inject
        private StringField name;
    }

    static class PageWithNoLeadingSlash extends AbstractPage
    {
        @Override
        public String getUrl()
        {
            return "path";
        }
    }

    static class ConstructorArgumentPrimitive
    {
        private final int intField;
        private final boolean booleanField;

        public ConstructorArgumentPrimitive(int intArg, boolean booleanArg)
        {
            this.intField = intArg;
            this.booleanField = booleanArg;
        }
    }

    static class ConstructorArgumentPage extends AbstractPage
    {
        private final String name;
        private final Number age;

        @SuppressWarnings("UnusedDeclaration")
        public ConstructorArgumentPage(String name)
        {
            this.name = name;
            this.age = null;
        }

        @SuppressWarnings("UnusedDeclaration")
        public ConstructorArgumentPage(Number age)
        {
            this.age = age;
            this.name = null;
        }
    }

    static class ParentNamePage extends AbstractPage
    {
        @Inject
        protected StringField name;
    }

    static class ChildNoNamePage extends ParentNamePage
    {
    }

    static class DefaultsPage extends AbstractPage
    {
        @Inject
        private ProductInstance productInstance;

        @Inject
        private TestedProduct testedProduct;

        @Inject
        private MyTestedProduct myTestedProduct;

        @Inject
        private Tester tester;

        @Inject
        private SetTester setTester;

        @Inject
        private PageBinder pageBinder;
    }

    static class OneFieldWithInitPage extends AbstractPage
    {
        @Inject
        private StringField field;

        private String name;
        @Init
        public void init()
        {
            name = field.getValue() + " Jones";
        }
    }

    static interface StringField
    {
        String getValue();
    }

    static class StringFieldImpl implements StringField
    {
        public String getValue()
        {
            return "Bob";
        }
    }

    static class AnotherStringFieldImpl implements StringField
    {
        public String getValue()
        {
            return "Rob";
        }
    }

    static class OneFieldWithPrivateInitPage extends AbstractPage
    {
        @Inject
        private StringField field;

        private String name;

        @Init
        @SuppressWarnings("UnusedDeclaration")
        private void init()
        {
            name = field.getValue() + " Private";
        }

        public String getName()
        {
            return name;
        }
    }

    static class OneFieldWithProtectedInitPage extends AbstractPage
    {
        @Inject
        private StringField field;

        private String name;

        @Init
        protected void init()
        {
            name = field.getValue() + " Protected";
        }
    }

    static class OneFieldWithSuperClassInitPage extends OneFieldWithPrivateInitPage
    {
    }

    static class MutablePage
    {
        private String value = "Default";

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }

    static class MutablePageProcessor implements PostInjectionProcessor
    {
        @Override
        public <T> T process(T pageObject)
        {
            if (pageObject instanceof MutablePage)
            {
                MutablePage.class.cast(pageObject).setValue("Post processed");
            }
            return pageObject;
        }
    }

    static class PageObjectWithRequiredAndIgnoredBrowsers
    {
        boolean initIgnoredInvoked;
        boolean initRequiredInvoked;

        boolean initIgnoreAllInvoked;
        boolean initRequireAllInvoked;

        @Init
        @IgnoreBrowser(Browser.ALL)
        public void initIgnoreAll()
        {
            initIgnoreAllInvoked = true;
        }

        @Init
        @IgnoreBrowser({ Browser.HTMLUNIT, Browser.HTMLUNIT_NOJS })
        public void initIgnored()
        {
            initIgnoredInvoked = true;
        }

        @Init
        @RequireBrowser(Browser.ALL)
        public void initRequireAll()
        {
            initRequireAllInvoked = true;
        }

        @Init
        @RequireBrowser({ Browser.CHROME, Browser.FIREFOX, Browser.HTMLUNIT })
        public void initRequired()
        {
            initRequiredInvoked = true;
        }
    }

}
