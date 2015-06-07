package com.atlassian.pageobjects.binder;

import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;

import static org.mockito.Mockito.mock;

/**
*
*/
class MyTestedProduct implements TestedProduct<SetTester>
{
    private final SetTester setTester;
    private final PageBinder pageBinder = mock(PageBinder.class);
    private final ProductInstance productInstance = mock(ProductInstance.class);

    public MyTestedProduct(SetTester setTester)
    {
        this.setTester = setTester;
    }


    public <P extends Page> P visit(Class<P> pageClass, Object... args)
    {
        return null;
    }

    public PageBinder getPageBinder()
    {
        return pageBinder;
    }

    public ProductInstance getProductInstance()
    {
        return productInstance;
    }

    public SetTester getTester()
    {
        return setTester;
    }

}
