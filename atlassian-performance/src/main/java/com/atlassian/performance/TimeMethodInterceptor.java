package com.atlassian.performance;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

public class TimeMethodInterceptor implements MethodInterceptor
{
    private static final String PERF_MARKER_TOKEN = "com.atlassian.performance.TestMarker";

    Logger log = LoggerFactory.getLogger("com.atlassian.performance.");

    public Object invoke(MethodInvocation invocation) throws Throwable {
	TimedMethodInvocation tmi = new TimedMethodInvocation(invocation);
	EventTime et = EventTime.timeEvent(eventKey(invocation), false, tmi);
	
	if(et.getTime() > 1) {
	    log.debug(MarkerFactory.getMarker(PERF_MARKER_TOKEN),
		      "{}, {}", new Object[]{argsToString(invocation.getArguments()), et.getTime()});
	}

	if(tmi.throwable == null) {
	    return tmi.val;
	} else {
	    throw tmi.throwable;
	}
    }

    private static String eventKey(MethodInvocation invocation)
    {
	return trimMethod(invocation.getMethod()) + " " + argsToString(invocation.getArguments());
    }

    private static String trimMethod(Method method) {
	String rawMethStr =  method.toString();
	int methodStart = rawMethStr.lastIndexOf(" ");
	if(methodStart > -1) 
	{
	    return rawMethStr.substring(methodStart);
	} else {
	    return rawMethStr;
	}
    }
    
    private static String argsToString(Object[] args) 
    {
	StringBuffer buf = new StringBuffer();
	for(Object arg : args) 
	{
	    buf.append(arg);
	    buf.append("*");
	}
	return buf.toString();
    }
	    

    public class TimedMethodInvocation implements EventTime.TimedEvent
    {
        Object val;
        Throwable throwable;
        MethodInvocation invocation;

        TimedMethodInvocation(MethodInvocation invocation)
        {
            this.invocation = invocation;
        }

        public boolean run()
        {
            try
            {
                val = invocation.proceed();
            }
            catch (Throwable t)
            {
                throwable = t;
            }
            finally
            {
                return true;
            }
        }

    }

}
