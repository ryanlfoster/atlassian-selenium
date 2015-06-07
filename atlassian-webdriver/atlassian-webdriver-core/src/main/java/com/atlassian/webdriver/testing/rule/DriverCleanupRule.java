package com.atlassian.webdriver.testing.rule;

import com.atlassian.webdriver.LifecycleAwareWebDriverGrid;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * A class rule that cleans up web drivers from the {@link com.atlassian.webdriver.LifecycleAwareWebDriverGrid}
 * after a specific amount of time elapses since the last test has been run.
 *
 * <p/>
 * Some {@link org.openqa.selenium.WebDriver}s suffer from bugs that prevent shutdown hooks from cleaning them
 * up properly. If you need to run your tests using those browsers (e.g. Chrome, IE), use this rule to
 * make sure the browser will be closed after the test suite finishes.
 *
 * <p/>
 * This rule is only effective if you use it in all your tests as a class rule. If you are using an injection context
 * (e.g. a {@link com.atlassian.pageobjects.TestedProduct} with a {@link com.atlassian.pageobjects.PageBinder}),
 * make sure this product's life cycle does not exceed this rule's life cycle (which is generally <code>true</code>,
 * if you use a {@link com.atlassian.webdriver.testing.runner.ProductContextRunner}, or just manually instantiate
 * the produce per test class).
 *
 * <p/>
 * When instantiating the class clients can set a custom timeout, or use default constructor that sets a 3 seconds
 * timeout.
 *
 * @since 2.1
 */
@ThreadSafe
public class DriverCleanupRule extends TestWatcher
{

    private static final Timer KILLER_TIMER = new Timer("Atlassian-BrowserKiller", true);
    private static final Queue<TimerTask> KILLER_TASKS = new ConcurrentLinkedQueue<TimerTask>();

    private final long timeout;
    private final TimeUnit timeUnit;

    public DriverCleanupRule(long timeout, TimeUnit timeUnit)
    {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public DriverCleanupRule()
    {
        this(3, TimeUnit.SECONDS);
    }

    @Override
    protected void starting(Description description)
    {
        for (Iterator<TimerTask> tasks = KILLER_TASKS.iterator(); tasks.hasNext();)
        {
            tasks.next().cancel();
            tasks.remove();
        }
    }

    @Override
    protected void finished(Description description)
    {
        final TimerTask newKiller = new BrowserKiller();
        KILLER_TASKS.add(newKiller);
        KILLER_TIMER.schedule(newKiller, timeUnit.toMillis(timeout));
    }

    private static final class BrowserKiller extends TimerTask
    {

        @Override
        public void run()
        {
            LifecycleAwareWebDriverGrid.shutdown();
        }
    }
}
