package com.atlassian.pageobjects.elements.test.pageobjects;

import com.atlassian.pageobjects.Defaults;
import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.pageobjects.binder.BrowserModule;
import com.atlassian.pageobjects.binder.InjectPageBinder;
import com.atlassian.pageobjects.binder.LoggerModule;
import com.atlassian.pageobjects.binder.StandardModule;
import com.atlassian.pageobjects.elements.ElementModule;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.WebDriverElement;
import com.atlassian.pageobjects.elements.timeout.Timeouts;
import com.atlassian.pageobjects.elements.timeout.TimeoutsModule;
import com.atlassian.webdriver.AtlassianWebDriverModule;
import com.atlassian.webdriver.pageobjects.DefaultWebDriverTester;
import com.atlassian.webdriver.pageobjects.WebDriverTester;
import org.openqa.selenium.By;

import static com.google.common.base.Preconditions.checkNotNull;

@Defaults(instanceId = "testapp", contextPath = "/", httpPort = 5990)
public class PageElementsTestedProduct implements TestedProduct<WebDriverTester>
{
    private final PageBinder pageBinder;
    private final WebDriverTester  webDriverTester;
    private final ProductInstance productInstance;
    private final Timeouts timeouts;

    public PageElementsTestedProduct(TestedProductFactory.TesterFactory<WebDriverTester> testerFactory, ProductInstance productInstance)
    {
        checkNotNull(productInstance);
        this.productInstance = productInstance;
        this.webDriverTester =  new DefaultWebDriverTester();
        TimeoutsModule timeoutsModule =  new TimeoutsModule();
        this.timeouts = timeoutsModule.timeouts();
        this.pageBinder = new InjectPageBinder(productInstance, webDriverTester,
                new StandardModule(this),
                new AtlassianWebDriverModule(this),
                new ElementModule(),
                new TimeoutsModule(),
                new BrowserModule(),
                new LoggerModule(PageElementsTestedProduct.class));
    }

    public Timeouts timeouts()
    {
        return timeouts;
    }

    public PageElement find(final By by)
    {
        return getPageBinder().bind(WebDriverElement.class, by);
    }

    public <P extends Page> P visit(Class<P> pageClass, Object... args)
    {
        return pageBinder.navigateToAndBind(pageClass, args);
    }

    public PageBinder getPageBinder()
    {
        return pageBinder;
    }

    public ProductInstance getProductInstance()
    {
        return productInstance;
    }

    public WebDriverTester getTester()
    {
        return webDriverTester;
    }
}
