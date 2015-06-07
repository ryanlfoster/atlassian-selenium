package com.atlassian.performance.junit;

import com.atlassian.performance.PerformanceReporter;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

public class PerformanceListener implements TestListener {
        private final PerformanceReporter reporter;

        public PerformanceListener(PerformanceReporter reporter) {
            this.reporter = reporter;
        }

        public void addError(Test test, Throwable t) {
        }

        public void addFailure(Test test, AssertionFailedError t) {
        }

        public void endTest(Test test) {
            if (test instanceof PerformanceTest) {
                reporter.addRecorder(((PerformanceTest) test).getRecorder());
            }
        }

        public void startTest(Test test) {
        }

        public PerformanceReporter getReporter() {
            return reporter;
        }

}