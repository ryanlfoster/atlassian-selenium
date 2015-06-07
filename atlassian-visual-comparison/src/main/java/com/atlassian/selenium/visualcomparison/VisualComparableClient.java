package com.atlassian.selenium.visualcomparison;

import com.atlassian.annotations.Internal;
import com.atlassian.selenium.visualcomparison.utils.ScreenResolution;

/**
 * @see VisualComparer
 * @see com.atlassian.selenium.visualcomparison.v2.BrowserEngine
 */
@Internal
public interface VisualComparableClient
{

    public void captureEntirePageScreenshot (String filePath);

    public ScreenElement getElementAtPoint(int x, int y);

    /**
     * Execute a script in the client.
     * @param command a string of javascript to send to the client.
     * @deprecated You should use {@link VisualComparableClient#execute(String, Object...)} instead.
     */
    @Deprecated
    public void evaluate(String command);

    /**
     * Execute a script in the client and return the evaluated result.
     * @param command a string of javascript to send to the client.
     * @param arguments additional arguments to provide to the script.
     * @return the evaluated result of the javascript.
     */
    public Object execute(String command, Object... arguments);

    public boolean resizeScreen(ScreenResolution resolution, boolean refreshAfterResize);

    public void refreshAndWait();

    /**
     * Wait until all active jQuery AJAX queries have returned -- i.e., no AJAX queries are active.
     * @param waitTimeMillis the time to wait for everything to finish.
     * @return true if no AJAX query is active, false otherwise.
     */
    public boolean waitForJQuery(long waitTimeMillis);
}
