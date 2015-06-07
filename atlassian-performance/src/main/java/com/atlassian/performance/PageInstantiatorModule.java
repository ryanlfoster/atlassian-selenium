package com.atlassian.performance;

import java.lang.reflect.Constructor;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import org.apache.commons.lang.ClassUtils;

import static java.util.Arrays.asList;

public class PageInstantiatorModule extends AbstractModule
{
    private PageObjectProvider pop;
    
    public PageInstantiatorModule(Class clazz, Object[] args)
    {
	pop = new PageObjectProvider(clazz, args);
    }
    
    @Override
    protected void configure()
    {
	bind(pop.clazz).toProvider(pop);
    }

    class PageObjectProvider<P> implements Provider<P>
    {
	Class<P> clazz;
	Object[] args;
	
	public PageObjectProvider(Class<P>  clazz, Object[] args) {
	    this.clazz = clazz;
	    this.args = args;
	}

	public P get() 
	{
	    try{
		if (args != null && args.length > 0)
		{
		    for (Constructor c : clazz.getConstructors())
		    {
			Class[] paramTypes = c.getParameterTypes();
			if (args.length == paramTypes.length)
			{
			    boolean match = true;
			    for (int x = 0; x < args.length; x++)
			    {
				if (args[x] != null && !ClassUtils.isAssignable(args[x].getClass(), paramTypes[x], true /*autoboxing*/))
				{
				    match = false;
				    break;
				}
                        }
                        if (match)
                        {
                            return (P) c.newInstance(args);
                        }
                    }
                }
            }
            else
            {
                try
                {
                    return clazz.newInstance();
                }
                catch (InstantiationException ex)
                {
                    throw new IllegalArgumentException("Error invoking default constructor", ex);
                }
            }
            throw new IllegalArgumentException("Cannot find constructor on " + clazz + " to match args: " + asList(args));
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
        }	
    }

}
