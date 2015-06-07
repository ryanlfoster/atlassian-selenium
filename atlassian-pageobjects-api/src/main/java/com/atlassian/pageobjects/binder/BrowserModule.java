package com.atlassian.pageobjects.binder;

import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.BrowserAware;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.Map;

/**
 * Adds the browser associated with the product to the context, via
 * {@link com.atlassian.pageobjects.browser.BrowserAware}.
 *
 * @since 2.1
 */
public class BrowserModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(BrowserAware.class).toProvider(BrowserAwareProvider.class);
        bind(Browser.class).toProvider(BrowserProvider.class);
    }

    protected static class AbstractBrowserAwareFinder
    {
        protected final BrowserAware browserAware;


        protected AbstractBrowserAwareFinder(Injector injector)
        {
            this.browserAware = find(injector);
        }

        private BrowserAware find(Injector injector)
        {
            for (Map.Entry<Key<?>,Binding<?>> entry : injector.getBindings().entrySet())
            {
                if (entry.getValue().getProvider().get() instanceof BrowserAware)
                {
                    return (BrowserAware) entry.getValue().getProvider().get();
                }
            }
            throw new IllegalStateException("BrowserAware not found in this injector");
        }

    }

    public static class BrowserAwareProvider extends AbstractBrowserAwareFinder implements Provider<BrowserAware>
    {
        @Inject public BrowserAwareProvider(Injector injector)
        {
            super(injector);
        }

        @Override
        public BrowserAware get()
        {
            return browserAware;
        }
    }

    public static class BrowserProvider extends AbstractBrowserAwareFinder implements Provider<Browser>
    {
        @Inject public BrowserProvider(Injector injector)
        {
            super(injector);
        }

        @Override
        public Browser get()
        {
            return browserAware.getBrowser();
        }
    }
}
