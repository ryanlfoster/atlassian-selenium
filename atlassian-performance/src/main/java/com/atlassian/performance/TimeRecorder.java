package com.atlassian.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeRecorder {

    private String testName;
    private List<EventTime> events = new ArrayList<EventTime>();


    public TimeRecorder(String testName)
    {
        this.testName = testName;
    }

    public void record(EventTime et)
    {
        events.add(et);
    }

    public List<EventTime> getEventTimes()
    {
        return Collections.unmodifiableList(events);
    }

    public String getTestName()
    {
        return testName;
    }
}
