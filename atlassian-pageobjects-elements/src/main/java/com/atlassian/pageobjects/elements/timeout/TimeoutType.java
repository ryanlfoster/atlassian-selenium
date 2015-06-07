package com.atlassian.pageobjects.elements.timeout;

/**
 * <p>
 * Enumeration of default timeout types in the elements.
 *
 * <p>
 * <b>NOTE</b>: This enumeration should <b>NOT</b> be extended with further timeout types, unless it is a matter of
 * <i>common</i> (as in: involving more than one developer) consensus that a new timeout type is necessary.
 * Instead, use customized timeouts, if a particular test context requires timeout adjustments (to that end you may use
 * {@link com.atlassian.pageobjects.elements.query.TimedQuery#by(long)}
 * and {@link com.atlassian.pageobjects.elements.query.TimedQuery#by(long, java.util.concurrent.TimeUnit)}).
 *
 */
public enum TimeoutType
{
    /**
     * Default timeout value. Meant to be used in case there is no explicit timeout provided for a components requiring
     * it.
     *
     */
    DEFAULT,

    /**
     * Interval between consecutive attempts to evaluate state of a particular test components, used e.g. by timed
     * conditions and queries. This is not really a timeout, but still has significant influence over how the timed
     * operations are executed.
     *
     * @see com.atlassian.pageobjects.elements.query.TimedQuery
     * @see com.atlassian.pageobjects.elements.query.PollingQuery
     */
    EVALUATION_INTERVAL,

    /**
     * Use for any actions executed by the test within the page UI, e.g. typing, clicking, moving mouse etc.
     *
     */
    UI_ACTION,

    /**
    * Page load of an average product page.
    *
    */
    PAGE_LOAD,

    /**
    * Page load of a slow page, e.g. Dashboard, pages for re-indexing products etc..
    *
    */
    SLOW_PAGE_LOAD,

    /**
     * Dialog load time.
     *
     */
    DIALOG_LOAD,

    /**
     * Load time of an UI-heavy components (e.g. drop-down, picker etc.)
     *
     */
    COMPONENT_LOAD,

    /**
     * An AJAX-like action on the tested page, e.g. an asynchronous request, a dialog submit (without redirection) etc.
     *
     */
    AJAX_ACTION
}
