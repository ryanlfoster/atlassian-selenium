package com.atlassian.webdriver.testing.runner;

import com.atlassian.pageobjects.inject.InjectionContext;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * A JUnit runner that injects all the page bindery things into your tests.
 *
 * @since 2.1.
 */
public abstract class AbstractInjectingRunner extends BlockJUnit4ClassRunner
{

    /**
     * Constructor compatible with the underlying default JUnit4 runner.
     *
     * @throws org.junit.runners.model.InitializationError
     *          if the test class is malformed.
     */
    public AbstractInjectingRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    @Override
    protected Statement classBlock(RunNotifier notifier)
    {
        getInjectionContext().injectStatic(getTestClass().getJavaClass());
        return super.classBlock(notifier);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test)
    {
        getInjectionContext().injectMembers(test);
        return super.methodInvoker(method, test);
    }


    protected abstract InjectionContext getInjectionContext();
}
