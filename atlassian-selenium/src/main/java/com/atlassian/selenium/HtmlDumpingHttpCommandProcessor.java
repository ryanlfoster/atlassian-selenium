package com.atlassian.selenium;

import com.thoughtworks.selenium.HttpCommandProcessor;
import org.apache.log4j.Logger;

/**
 * An Atlassian HttpCommand processor to dump the HTML if there are any errors.
 */
public class HtmlDumpingHttpCommandProcessor extends HttpCommandProcessor
{
    private static final Logger log = Logger.getLogger(HtmlDumpingHttpCommandProcessor.class);

    public HtmlDumpingHttpCommandProcessor(String s, int i, String s1, String s2)
    {
        super(s, i, s1, s2);
    }

    public String doCommand(String s, String[] strings)
    {
        try
        {
            return super.doCommand(s, strings);
        }
        catch (RuntimeException e)
        {
            log.error("Error executing command [" + s + "]", e);
            log.error("---------------- HTML DUMP -------------\n\n\n");
            log.error(super.doCommand("getHtmlSource", new String[0]));
            log.error("\n\n\n----------- END HTML DUMP -----------");
            throw e;
        }
    }
}
