package com.atlassian.webdriver.testing.rule;

import com.atlassian.selenium.visualcomparison.VisualComparableClient;
import com.atlassian.selenium.visualcomparison.VisualComparer;
import com.atlassian.webdriver.AtlassianWebDriver;
import com.atlassian.webdriver.LifecycleAwareWebDriverGrid;
import com.atlassian.webdriver.visualcomparison.WebDriverVisualComparableClient;

/**
 * @since 2.1
 */
public class VisualComparerRule extends FailsafeExternalResource
{
    private VisualComparer comparer;

    @Override
    protected void before() throws Throwable
    {
        AtlassianWebDriver driver = LifecycleAwareWebDriverGrid.getCurrentDriver();
        VisualComparableClient client = new WebDriverVisualComparableClient(driver);
        comparer = new VisualComparer(client);
    }

    public VisualComparer getComparer()
    {
        return comparer;
    }
}
