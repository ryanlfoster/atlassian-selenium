package com.atlassian.pageobjects.binder;

import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.Tester;
import com.google.inject.Binder;
import com.google.inject.Module;

public class StandardModule implements Module
{
    private final TestedProduct testedProduct;

    public StandardModule(TestedProduct testedProduct)
    {
        this.testedProduct = testedProduct;
    }

    @SuppressWarnings("unchecked")
    public void configure(Binder binder)
    {
        binder.bind(TestedProduct.class).toInstance(testedProduct);
        binder.bind((Class<TestedProduct>) testedProduct.getClass()).toInstance(testedProduct);

        binder.bind(Tester.class).toInstance(testedProduct.getTester());
        binder.bind((Class<Tester>) testedProduct.getTester().getClass()).toInstance(testedProduct.getTester());

        binder.bind(ProductInstance.class).toInstance(testedProduct.getProductInstance());
    }
}
