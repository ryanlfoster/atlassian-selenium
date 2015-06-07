package com.atlassian.webdriver.pageobjects;

import com.atlassian.pageobjects.binder.PostInjectionProcessor;
import com.atlassian.webdriver.AtlassianWebDriver;
import org.openqa.selenium.support.PageFactory;

import javax.inject.Inject;

/**
 * Processor that will use {@Link PageFactory} to initialize the object
 */
public class PageFactoryPostInjectionProcessor implements PostInjectionProcessor
{

    private final AtlassianWebDriver atlassianWebDriver;

    @Inject
    public PageFactoryPostInjectionProcessor(AtlassianWebDriver atlassianWebDriver)
    {
        this.atlassianWebDriver = atlassianWebDriver;
    }

    public <P> P process(P pageObject)
    {
        PageFactory.initElements(atlassianWebDriver, pageObject);
        return pageObject;
    }
}
