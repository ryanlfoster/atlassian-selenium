package com.atlassian.pageobjects.binder;

import com.atlassian.annotations.Internal;
import com.atlassian.pageobjects.DelayedBinder;
import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.Tester;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.IgnoreBrowser;
import com.atlassian.pageobjects.browser.RequireBrowser;
import com.atlassian.pageobjects.inject.AbstractInjectionConfiguration;
import com.atlassian.pageobjects.inject.ConfigurableInjectionContext;
import com.atlassian.pageobjects.inject.InjectionConfiguration;
import com.atlassian.pageobjects.inject.InjectionContext;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.util.Modules;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * Page navigator that builds page objects from classes, then injects them with dependencies and calls lifecycle methods.
 * <p/>
 * The construction process is as follows:
 * <ol>
 *  <li>Determine the actual class by checking for an override</li>
 *  <li>Instantiate the class using a constructor that matches the passed arguments</li>
 *  <li>Changes the tester to the corrent URL (if {@link #navigateToAndBind(Class, Object...)})</li>
 *  <li>Inject all fields annotated with {@link Inject}, including private</li>
 *  <li>Execute the supplied {@link PostInjectionProcessor}</li>
 *  <li>Call all methods annotated with {@link WaitUntil}</li>
 *  <li>Call all methods annotated with {@link ValidateState}</li>
 *  <li>Call all methods annotated with {@link Init}</li>
 * </ol>
 * <p/>
 * When going to a page via the {@link #navigateToAndBind(Class, Object...)} method, the page's URL is retrieved and
 * navigated to via {@link Tester#gotoUrl(String)} after construction and initializing but before {@link WaitUntil}
 * methods are called.
 * <p/>
 * This class also implements a mutable variant of {@link ConfigurableInjectionContext}, where injection configuration
 * changes are applied in-place, by creating a new Guice injector.
 *
 * @since 2.0
 */
@NotThreadSafe
@Internal
public final class InjectPageBinder implements PageBinder, ConfigurableInjectionContext
{
    private final Tester tester;
    private final ProductInstance productInstance;
    private static final Logger log = LoggerFactory.getLogger(InjectPageBinder.class);

    private final Map<Class<?>, Class<?>> overrides = new HashMap<Class<?>, Class<?>>();
    private volatile Module module;
    private volatile Injector injector;
    private volatile List<Binding<PostInjectionProcessor>> postInjectionProcessors;

    public InjectPageBinder(ProductInstance productInstance, Tester tester, Module... modules)
    {
        checkNotNull(productInstance);
        checkNotNull(tester);
        checkNotNull(modules);
        this.tester = tester;
        this.productInstance = productInstance;
        this.module = Modules.override(modules).with(new ThisModule());
        this.injector = Guice.createInjector(module);
        initPostInjectionProcessors();
    }

    /**
     * Injector used by this binder.
     *
     * @return injector used by this binder.
     * @deprecated take advantage of {@link InjectionContext} API instead. Scheduled for removal in 3.0
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public Injector injector()
    {
        return injector;
    }

    // ------------------------------------------------------------------------------------------------------ PageBinder

    @Override
    public <P extends Page> P navigateToAndBind(Class<P> pageClass, Object... args)
    {
        checkNotNull(pageClass);
        DelayedBinder<P> binder = delayedBind(pageClass, args);
        P p = binder.get();
        visitUrl(p);
        return binder.bind();
    }

    @Override
    public <P> P bind(Class<P> pageClass, Object... args)
    {
        checkNotNull(pageClass);
        return delayedBind(pageClass, args).bind();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> DelayedBinder<P> delayedBind(Class<P> pageClass, Object... args)
    {
        return new InjectableDelayedBind<P>(asList(
                new InstantiatePhase<P>(pageClass, args),
                new InjectPhase<P>(),
                new WaitUntilPhase<P>(),
                new ValidateStatePhase<P>(),
                new InitializePhase<P>()));
    }

    @Override
    public <P> void override(Class<P> oldClass, Class<? extends P> newClass)
    {
        checkNotNull(oldClass);
        checkNotNull(newClass);
        overrides.put(oldClass, newClass);
    }

    // -----------------------------------------------------------------------------------------------  InjectionContext

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public <T> T getInstance(@Nonnull Class<T> type)
    {
        checkArgument(type != null, "type was null");
        try
        {
            return injector.getInstance(type);
        }
        catch (ProvisionException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (ConfigurationException e)
        {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public void injectStatic(@Nonnull final Class<?> targetClass)
    {
        injector.createChildInjector(new AbstractModule()
        {
            @Override
            protected void configure()
            {
                requestStaticInjection(targetClass);
            }
        });
    }

    @Override
    public void injectMembers(@Nonnull Object targetInstance)
    {
        injector.injectMembers(targetInstance);
    }

    @Nonnull
    @Override
    public <T> T inject(@Nonnull T target)
    {
        checkNotNull(target, "target");
        injector.injectMembers(target);

        return target;
    }

    @Nonnull
    @Override
    public InjectionConfiguration configure()
    {
        return new InjectConfiguration();
    }

    protected void visitUrl(@Nonnull Page page)
    {
        checkNotNull(page, "page");
        tester.gotoUrl(normalisedBaseUrl() + normalisedPath(page));
    }

    // ------------------------------------------------------------------------------------------------- Private methods

    /**
     * Calls all methods with the given annotation, starting with methods found in the topmost superclass, then calling
     * more specific methods in subclasses. Note that this can mean that this will attempt to call the same method
     * multiple times - once per override in the hierarchy. Will call the methods even if they're private. Skips methods
     * if they are also annotated with {@link com.atlassian.pageobjects.browser.IgnoreBrowser}
     * (or {@link com.atlassian.pageobjects.browser.RequireBrowser}) if the current {@link Browser}
     * matches (does not match) any of the browsers listed in that annotation.
     * @param instance the page object to check for the annotation
     * @param annotation the annotation to find
     * @throws InvocationTargetException if any matching method throws any exception.
     */
    @SuppressWarnings("unchecked")
    private void callLifecycleMethod(Object instance, Class<? extends Annotation> annotation) throws InvocationTargetException
    {
        Class clazz = instance.getClass();
        List<Class> classes = ClassUtils.getAllSuperclasses(clazz);
        Collections.reverse(classes);
        classes.add(clazz);

        for (Class cl : classes)
        {
            for (Method method : cl.getDeclaredMethods())
            {
                if (method.getAnnotation(annotation) != null)
                {
                    Browser currentBrowser = getBrowser();
                    if (isIgnoredBrowser(method, method.getAnnotation(IgnoreBrowser.class), currentBrowser) ||
                            !isRequiredBrowser(method, method.getAnnotation(RequireBrowser.class), currentBrowser))
                    {
                        continue;
                    }

                    try
                    {
                        if (!method.isAccessible())
                        {
                            method.setAccessible(true);
                        }

                        method.invoke(instance);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Map<Key<?>, Binding<?>> collectBindings()
    {
        ImmutableMap.Builder<Key<?>, Binding<?>> result = ImmutableMap.builder();
        Injector current = this.injector;
        while (current != null)
        {
            result.putAll(current.getAllBindings());
            current = current.getParent();
        }
        return result.build();
    }

    private Browser getBrowser()
    {
        try
        {
            return injector.getInstance(Browser.class);
        }
        catch (Exception e)
        {
            // no browser in context
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void initPostInjectionProcessors()
    {
        List<Binding<PostInjectionProcessor>> processors = Lists.newArrayList();
        for (Binding binding : collectBindings().values())
        {
            if (PostInjectionProcessor.class.isAssignableFrom(binding.getKey().getTypeLiteral().getRawType()))
            {
                processors.add(binding);
            }
        }
        postInjectionProcessors = unmodifiableList(processors);
    }

    private boolean isRequiredBrowser(Method method, RequireBrowser requireBrowser, Browser currentBrowser)
    {
        if (currentBrowser == null || requireBrowser == null)
        {
            return true;
        }
        if (requireBrowser.value().length == 0)
        {
            return false;
        }

        Set<Browser> required = EnumSet.copyOf(ImmutableList.copyOf(requireBrowser.value()));
        boolean result = required.contains(currentBrowser) || required.contains(Browser.ALL);
        if (!result)
        {
            log.info("{} ignored, since one of {} is required (reason: {}), but the current browser is {}",
                    method.getName(), required, requireBrowser.reason(), currentBrowser);
        }

        return result;
    }

    private boolean isIgnoredBrowser(Method method, IgnoreBrowser ignoreBrowser, Browser currentBrowser)
    {
        if (currentBrowser == null || ignoreBrowser == null)
        {
            return false;
        }

        for (Browser browser : ignoreBrowser.value())
        {
            if (browser == currentBrowser || browser == Browser.ALL)
            {
                log.info(method.getName() + " ignored, reason: " + ignoreBrowser.reason());
                return true;
            }
        }
        return false;
    }

    /**
     * @return the base URL for the application with no trailing slash
     */
    private String normalisedBaseUrl()
    {
        final String baseUrl = productInstance.getBaseUrl();
        if (baseUrl.endsWith("/"))
        {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl;
    }

    /**
     * @param page a Page
     * @return the path segment for a page with a leading slash
     */
    private String normalisedPath(Page page)
    {
        final String path = page.getUrl();
        if (!path.startsWith("/"))
        {
            return "/" + path;
        }

        return path;
    }

    void reconfigure(Module module)
    {
        this.module = Modules.override(this.module).with(module);
        this.injector = Guice.createInjector(this.module);
        initPostInjectionProcessors();
    }

    // ---------------------------------------------------------------------------------------------------------- Phases

    private static interface Phase<T>
    {
        T execute(T pageObject);
    }

    private class InstantiatePhase<T> implements Phase<T>
    {
        private Class<T> pageClass;
        private final Object[] args;

        public InstantiatePhase(Class<T> pageClass, Object[] args)
        {
            this.pageClass = pageClass;
            this.args = args;
        }

        @SuppressWarnings("unchecked")
        public T execute(T t)
        {
            T instance;
            Class<T> actualClass = pageClass;
            if (overrides.containsKey(pageClass))
            {
                actualClass = (Class<T>) overrides.get(pageClass);
            }

            try
            {
                instance = instantiate(actualClass, args);
            }
            catch (InstantiationException e)
            {
                throw new IllegalArgumentException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new IllegalArgumentException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new IllegalArgumentException(e.getCause());
            }
            return instance;
        }

        @SuppressWarnings("unchecked")
        private T instantiate(Class<T> clazz, Object[] args)
                throws InstantiationException, IllegalAccessException, InvocationTargetException
        {
            if (args != null && args.length > 0)
            {
                for (Constructor c : clazz.getConstructors())
                {
                    Class[] paramTypes = c.getParameterTypes();
                    if (args.length == paramTypes.length)
                    {
                        boolean match = true;
                        for (int x = 0; x < args.length; x++)
                        {
                            if (args[x] != null && !ClassUtils.isAssignable(args[x].getClass(), paramTypes[x], true))
                            {
                                match = false;
                                break;
                            }
                        }
                        if (match)
                        {
                            return (T) c.newInstance(args);
                        }
                    }
                }
            }
            else
            {
                try
                {
                    return clazz.newInstance();
                }
                catch (InstantiationException e)
                {
                    throw new IllegalArgumentException("Error invoking default constructor", e);
                }
            }
            throw new IllegalArgumentException("Cannot find constructor of " + clazz + " to match args: "
                    + asList(args));
        }
    }

    private class InjectPhase<T> implements Phase<T>
    {
        public T execute(T t)
        {
            autowireInjectables(t);
            T pageObject = t;
            for (Binding<PostInjectionProcessor> binding : postInjectionProcessors)
            {
                pageObject = binding.getProvider().get().process(pageObject);
            }
            return pageObject;
        }

        private void autowireInjectables(final Object instance)
        {
            try
            {
                injector.injectMembers(instance);
            }
            catch (ConfigurationException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    private class WaitUntilPhase<T> implements Phase<T>
    {
        public T execute(T pageObject)
        {
            try
            {
                callLifecycleMethod(pageObject, WaitUntil.class);
            }
            catch (InvocationTargetException e)
            {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof PageBindingWaitException)
                {
                    throw (PageBindingWaitException) targetException;
                }
                else
                {
                    throw new PageBindingWaitException(pageObject, targetException);
                }
            }
            return pageObject;
        }
    }

    private class ValidateStatePhase<T> implements Phase<T>
    {
        public T execute(T pageObject)
        {
            try
            {
                callLifecycleMethod(pageObject, ValidateState.class);
            }
            catch (InvocationTargetException e)
            {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof InvalidPageStateException)
                {
                    throw (InvalidPageStateException) targetException;
                }
                else
                {
                    throw new InvalidPageStateException(pageObject, targetException);
                }
            }
            return pageObject;
        }
    }

    private class InitializePhase<T> implements Phase<T>
    {
        public T execute(T pageObject)
        {
            try
            {
                callLifecycleMethod(pageObject, Init.class);
            }
            catch (InvocationTargetException e)
            {
                throw new PageBindingException(pageObject, e.getTargetException());
            }
            return pageObject;
        }
    }

    // --------------------------------------------------------------------------------------------------- DelayedBinder

    private class InjectableDelayedBind<T> implements DelayedBinder<T>
    {
        private final LinkedList<Phase<T>> phases;
        private T pageObject = null;

        public InjectableDelayedBind(List<Phase<T>> phases)
        {
            this.phases = new LinkedList<Phase<T>>(phases);
        }

        @Override
        public boolean canBind()
        {
            try
            {
                advanceTo(InitializePhase.class);
                return true;
            }
            catch (PageBindingException ex)
            {
                return false;
            }
        }

        @Override
        @Nonnull
        public T get()
        {
            advanceTo(InstantiatePhase.class);
            return pageObject;
        }

        @Override
        @Nonnull
        public DelayedBinder<T> inject()
        {
            advanceTo(InjectPhase.class);
            return this;
        }

        @Override
        @Nonnull
        public DelayedBinder<T> waitUntil()
        {
            advanceTo(WaitUntilPhase.class);
            return this;
        }

        @Override
        @Nonnull
        public DelayedBinder<T> validateState()
        {
            advanceTo(ValidateStatePhase.class);
            return this;
        }

        @Override
        @Nonnull
        public T bind()
        {
            advanceTo(InitializePhase.class);
            return pageObject;
        }

        private void advanceTo(Class<? extends Phase> phaseClass)
        {
            boolean found = false;
            for (Phase<T> phase : phases)
            {
                if (phase.getClass() == phaseClass)
                {
                    found = true;
                }
            }

            if (found)
            {
                while (!phases.isEmpty())
                {
                    pageObject = phases.getFirst().execute(pageObject);
                    if (phases.removeFirst().getClass() == phaseClass)
                    {
                        break;
                    }
                }
            }
            else
            {
                log.debug("Already advanced to state: " + phaseClass.getName());
            }
        }
    }

    // ------------------------------------------------------------------------------------------ InjectionConfiguration

    private final class InjectConfiguration extends AbstractInjectionConfiguration
    {

        @Override
        @Nonnull
        public ConfigurableInjectionContext finish()
        {
            reconfigure(getModule());
            return InjectPageBinder.this;
        }

        Module getModule()
        {
            return new Module()
            {
                @Override
                @SuppressWarnings("unchecked")
                public void configure(Binder binder)
                {
                    for (InterfaceToImpl intToImpl : interfacesToImpls)
                    {
                        binder.bind((Class)intToImpl.interfaceType).to(intToImpl.implementation);
                    }
                    for (InterfaceToInstance intToInstance : interfacesToInstances)
                    {
                        binder.bind((Class)intToInstance.interfaceType).toInstance(intToInstance.instance);
                    }
                }
            };
        }
    }

    // --------------------------------------------------------------------------------------------- Other inner classes

    private final class ThisModule extends AbstractModule
    {
        @Override
        protected void configure()
        {
            bind(PageBinder.class).toInstance(InjectPageBinder.this);
        }
    }
}
