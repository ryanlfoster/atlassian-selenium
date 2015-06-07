package com.atlassian.pageobjects.binder;

import com.atlassian.pageobjects.DelayedBinder;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.Tester;
import com.google.inject.Binder;
import com.google.inject.Module;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test case for {@link com.atlassian.pageobjects.DelayedBinder}.
 */
public class TestDelayedBinder
{
    private MyTestedProduct product;

    @Before
    public void setUp() throws Exception
    {
        product = new MyTestedProduct(new SetTester());
    }

    private InjectPageBinder createBinder()
    {
        return createBinder(null, null);
    }
    private InjectPageBinder createBinder(final Class<?> key, final Class impl)
    {
        return new InjectPageBinder(mock(ProductInstance.class), mock(Tester.class), new StandardModule(product),
                new Module()
                {
                    public void configure(Binder binder)
                    {
                        if (key != null)
                        {
                            binder.bind(key).to(impl);
                        }
                    }
                });
    }

    @Test
    public void shouldAlwaysReturnFalseGivenWaitUntilFails()
    {
        final PageBinder binder = createBinder();
        final DelayedBinder<PageObjectFail> delayedBinder = binder.delayedBind(PageObjectFail.class, 8);
        for (int i=0; i<8; i++)
        {
            assertFalse(delayedBinder.canBind());
        }
        assertTrue(delayedBinder.canBind());
    }


    @Test
    public void canBindMustReturnFalseIfInitFails()
    {
        final PageBinder binder = createBinder();
        final DelayedBinder<PageObjectWithInitializeThrowingException> delayedBinder = binder.delayedBind(PageObjectWithInitializeThrowingException.class);
        assertFalse(delayedBinder.canBind());
    }


    /**
     * A typical failing page object.
     *
     */
    static class PageObjectFail
    {
        private int numberOfFails = 0;

        public PageObjectFail(int numberOfFails)
        {
            this.numberOfFails = numberOfFails;
        }

        @Init
        protected void init()
        {
        }

        @WaitUntil
        protected void failingNotWaiting()
        {
            if (numberOfFails > 0)
            {
                numberOfFails--;
                throw new RuntimeException("FAIL!");
            }
        }

        @ValidateState
        protected void goodValidate()
        {
        }
    }

    static class PageObjectWithInitializeThrowingException
    {
        @Init
        private void badStuff()
        {
            throw new RuntimeException("HAHAHAHAHA");
        }
    }


}
