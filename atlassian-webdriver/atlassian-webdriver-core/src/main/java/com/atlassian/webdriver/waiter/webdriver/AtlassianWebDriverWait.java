package com.atlassian.webdriver.waiter.webdriver;

import com.atlassian.annotations.ExperimentalApi;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.SystemClock;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * A specialization of {@link FluentWait} that uses WebDriver instances
 * and uses milliseconds for the time interval instead of seconds.
 * @see org.openqa.selenium.support.ui.WebDriverWait
 *
 * <strong>WARNING</strong>: This API is still experimental and may be changed between versions.
 *
 * @since 2.1.0
 */
@ExperimentalApi
public class AtlassianWebDriverWait extends FluentWait<WebDriver>
{
    public AtlassianWebDriverWait(WebDriver input, Clock clock, Sleeper sleeper)
    {
        super(input, clock, sleeper);
    }
    
    /**
   * @param driver The WebDriver instance to pass to the expected conditions
   * @param timeOutInMilliSeconds The timeout in seconds when an expectation is
   * called
   */
  public AtlassianWebDriverWait(WebDriver driver, long timeOutInMilliSeconds) {
    this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInMilliSeconds,
            WebDriverWait.DEFAULT_SLEEP_TIMEOUT
    );
  }

  /**
   * @param driver The WebDriver instance to pass to the expected conditions
   * @param timeOutInMilliSeconds The timeout in seconds when an expectation is
   * called
   * @param sleepInMillis The duration in milliseconds to sleep between polls.
   */
  public AtlassianWebDriverWait(WebDriver driver, long timeOutInMilliSeconds, long sleepInMillis) {
    this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInMilliSeconds, sleepInMillis);
  }

  /**
   * @param driver The WebDriver instance to pass to the expected conditions
   * @param clock The clock to use when measuring the timeout
   * @param sleeper Object used to make the current thread go to sleep.
   * @param timeOutInMilliSeconds The timeout in seconds when an expectation is
   * @param sleepTimeOut The timeout used whilst sleeping. Defaults to 500ms
*     called.
   */
  protected AtlassianWebDriverWait(WebDriver driver, Clock clock, Sleeper sleeper,
          long timeOutInMilliSeconds, long sleepTimeOut) {
    super(driver, clock, sleeper);
    withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS);
    pollingEvery(sleepTimeOut, TimeUnit.MILLISECONDS);
    ignoring(NotFoundException.class);
  }
    
}
