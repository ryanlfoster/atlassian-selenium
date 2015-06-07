package com.atlassian.webdriver.it.tests;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.browser.Browser;
import com.atlassian.pageobjects.browser.RequireBrowser;
import com.atlassian.webdriver.it.AbstractSimpleServerTest;
import com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging.IncludedScriptErrorPage;
import com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging.NoErrorsPage;
import com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging.UntypedErrorPage;
import com.atlassian.webdriver.it.pageobjects.page.jsconsolelogging.WindowErrorPage;
import com.atlassian.webdriver.testing.rule.JavaScriptErrorsRule;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

/**
 * Test the rule for logging client-side console output.
 *
 * At the time of writing, the logging only captures exceptions.
 * The tests only work in Firefox, since the rule uses a Firefox extension to get the output.
 */
@RequireBrowser(Browser.FIREFOX)
public class TestCapturingJavaScriptConsoleOutput extends AbstractSimpleServerTest
{
    private JavaScriptErrorsRule rule;

    @Before
    public void setUp()
    {
        rule = new JavaScriptErrorsRule();
    }

    @Test
    public void testPageWithNoErrors()
    {
        product.visit(NoErrorsPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, equalTo(""));
    }

    @Test
    public void testSingleErrorInWindowScope()
    {
        final Page page = product.visit(WindowErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, containsString("ReferenceError: foo is not defined"));
        assertThat(consoleOutput, containsString(page.getUrl()));
    }

    @Test
    public void testMultipleErrorsInIncludedScripts()
    {
        final IncludedScriptErrorPage page = product.visit(IncludedScriptErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, containsString("TypeError: $ is not a function"));
        assertThat(consoleOutput, containsString(page.objectIsNotFunctionScriptUrl()));

        assertThat(consoleOutput, containsString("Error: throw Error('bail')"));
        assertThat(consoleOutput, containsString(page.throwErrorObjectScriptUrl()));
    }

    @Test
    public void testEachJSErrorIsOnANewLine()
    {
        final IncludedScriptErrorPage page = product.visit(IncludedScriptErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        final String[] errors = consoleOutput.split("\n");

        assertThat(errors.length, equalTo(2));
        assertThat(errors[0], containsString(page.objectIsNotFunctionScriptUrl()));
        assertThat(errors[1], containsString(page.throwErrorObjectScriptUrl()));
    }

    @Test
    public void testCanCaptureUntypedErrors()
    {
        product.visit(UntypedErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, containsString("uncaught exception: throw string"));
    }

    @Test
    public void testCanBeOverriddenToIgnoreSpecificErrors()
    {
        rule = rule.errorsToIgnore(ImmutableSet.of("uncaught exception: throw string"));
        product.visit(UntypedErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, isEmptyString());
    }

    @Ignore("The JSErrorCollector plugin currently cannot capture console errors")
    @Test
    public void testCanCaptureConsoleErrors()
    {
        final UntypedErrorPage page = product.visit(UntypedErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, containsString("console.error"));
        assertThat(consoleOutput, containsString(page.consoleErrorScriptUrl()));
    }

    @Test
    public void testCurrentlyCannotCaptureConsoleErrors()
    {
        final UntypedErrorPage page = product.visit(UntypedErrorPage.class);
        final String consoleOutput = rule.getConsoleOutput();
        assertThat(consoleOutput, not(containsString("console.error")));
        assertThat(consoleOutput, not(containsString(page.consoleErrorScriptUrl())));
    }
}
