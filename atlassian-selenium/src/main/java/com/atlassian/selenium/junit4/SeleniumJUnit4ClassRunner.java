package com.atlassian.selenium.junit4;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * A JUNIT 4 test runner that registers the {@link it.com.atlassian.applinks.CaptureScreenshotListener} before executing a test.
 * This test runner can be applied by using the {@link org.junit.runner.RunWith} annotation on the test class.
 *
 * Example:
 *
 *    @RunWith(SeleniumJUnit4ClassRunner.class)
 *    public class MySeleniumTest
 *
 * @since 2.0
 */
public class SeleniumJUnit4ClassRunner extends BlockJUnit4ClassRunner
{
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @throws org.junit.runners.model.InitializationError if the test class is malformed.
     */
    public SeleniumJUnit4ClassRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    @Override
    public void run(final RunNotifier notifier)
    {
        CaptureScreenshotListener listener = new CaptureScreenshotListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);
    }
}
