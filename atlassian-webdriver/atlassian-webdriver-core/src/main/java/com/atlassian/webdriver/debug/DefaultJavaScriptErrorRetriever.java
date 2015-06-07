package com.atlassian.webdriver.debug;

import com.atlassian.webdriver.utils.WebDriverUtil;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static com.google.common.collect.Iterables.transform;

/**
 * Default implementation of {@link JavaScriptErrorRetriever}, using the JSErrorCollector
 * library.
 * @since 2.3
 */
public class DefaultJavaScriptErrorRetriever implements JavaScriptErrorRetriever
{
    private final Supplier<? extends WebDriver> webDriver;

    public DefaultJavaScriptErrorRetriever(Supplier<? extends WebDriver> webDriver)
    {
        this.webDriver = webDriver;
    }

    @Override
    public boolean isErrorRetrievalSupported()
    {
        return WebDriverUtil.isInstance(webDriver.get(), FirefoxDriver.class);
    }
    
    @Override
    public Iterable<JavaScriptErrorInfo> getErrors()
    {
        if (isErrorRetrievalSupported())
        {
            return transform(JavaScriptError.readErrors(webDriver.get()), new Function<JavaScriptError, JavaScriptErrorInfo>()
            {
                public JavaScriptErrorInfo apply(final JavaScriptError e)
                {
                    return new JavaScriptErrorInfo()
                    {
                        public String getDescription()
                        {
                            return e.toString();
                        }
    
                        public String getMessage()
                        {
                            return e.getErrorMessage();
                        }
                    };
                }
            });
        }
        else
        {
            return ImmutableList.of();
        }
    }
}
