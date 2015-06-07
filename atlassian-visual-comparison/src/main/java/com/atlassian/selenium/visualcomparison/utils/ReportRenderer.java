package com.atlassian.selenium.visualcomparison.utils;

import com.atlassian.annotations.Internal;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Properties;

@Internal
public class ReportRenderer
{
    private static boolean initialised = false;

    public static VelocityContext createContext() throws Exception
    {
        if (!initialised)
        {
            Properties p = new Properties();
            p.setProperty("resource.loader", "class");
            p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init(p);
            initialised = true;
        }
        return new VelocityContext();
    }

    public static String render(VelocityContext context, String templateName) throws Exception
    {
        Template template = Velocity.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        return sw.toString();
    }
 }