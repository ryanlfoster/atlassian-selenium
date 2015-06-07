package com.atlassian.performance.junit;

import junit.framework.Test;
import com.atlassian.performance.TimeRecorder;

public interface PerformanceTest extends Test {

    public TimeRecorder getRecorder();
}
