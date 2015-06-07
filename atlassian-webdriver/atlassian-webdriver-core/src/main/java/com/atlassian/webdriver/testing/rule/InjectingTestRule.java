package com.atlassian.webdriver.testing.rule;

import com.atlassian.pageobjects.inject.InjectionContext;
import org.junit.rules.TestRule;

/**
 * A test rule capable of performing injection.
 *
 * @since 2.1
 */
public interface InjectingTestRule extends TestRule, InjectionContext
{
}
