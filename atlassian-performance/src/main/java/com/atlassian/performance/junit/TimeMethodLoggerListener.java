package com.atlassian.performance.junit;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

public class TimeMethodLoggerListener implements TestListener {

    private static final String PERF_MARKER_TOKEN = "com.atlassian.performance.TestMarker";

    public TimeMethodLoggerListener()
    {
	super();
	System.out.println("-------------------- Instantiating the TimeMethodLoggerListener");
    }

    public void addError(Test test, Throwable t) {
    }

    public void addFailure(Test test, AssertionFailedError t) {
    }

    public void endTest(Test test) {
	System.out.println("Removing marker for " + test);
	Marker perfMarker = MarkerFactory.getMarker(PERF_MARKER_TOKEN);
	perfMarker.remove(MarkerFactory.getMarker(test.getClass().toString()));
    }

    public void startTest(Test test) {
	System.out.println("Adding marker for " + test);
	Marker perfMarker = MarkerFactory.getMarker(PERF_MARKER_TOKEN);
	perfMarker.add(MarkerFactory.getMarker(test.getClass().toString()));
    }
    
}