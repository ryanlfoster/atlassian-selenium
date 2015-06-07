package com.atlassian.webdriver.jira;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.binder.PostInjectionProcessor;
import com.atlassian.pageobjects.util.InjectUtils;
import com.atlassian.webdriver.jira.component.ClickableLink;
import com.atlassian.webdriver.jira.component.WebDriverLink;
import org.openqa.selenium.By;

import javax.inject.Inject;
import java.lang.reflect.Field;

import static com.atlassian.pageobjects.util.InjectUtils.forEachFieldWithAnnotation;

/**
 *
 */
public class ClickableLinkPostInjectionProcessor implements PostInjectionProcessor
{
    @Inject
    PageBinder pageBinder;

    public <T> T process(T pageObject)
    {
        injectWebLinks(pageObject);
        return pageObject;
    }

    private void injectWebLinks(final Object instance)
    {
        forEachFieldWithAnnotation(instance, ClickableLink.class, new InjectUtils.FieldVisitor<ClickableLink>()
        {
            public void visit(Field field, ClickableLink annotation)
            {
                WebDriverLink link = createLink(annotation);
                try
                {
                    field.setAccessible(true);
                    field.set(instance, link);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private WebDriverLink createLink(ClickableLink clickableLink)
    {
        By by;
        if (clickableLink.className().length() > 0)
        {
            by = By.className(clickableLink.className());
        }
        else if (clickableLink.id().length() > 0)
        {
            by = By.id(clickableLink.id());
        }
        else if (clickableLink.linkText().length() > 0)
        {
            by = By.linkText(clickableLink.linkText());
        }
        else if (clickableLink.partialLinkText().length() > 0)
        {
            by = By.partialLinkText(clickableLink.partialLinkText());
        }
        else
        {
            throw new IllegalArgumentException("No selector found");
        }

        return pageBinder.bind(WebDriverLink.class, by, clickableLink.nextPage());
    }
}
