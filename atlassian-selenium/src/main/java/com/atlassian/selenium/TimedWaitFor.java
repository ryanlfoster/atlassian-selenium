package com.atlassian.selenium;

import com.atlassian.performance.EventTime;
import com.thoughtworks.selenium.Selenium;

class TimedWaitFor implements EventTime.TimedEvent
{
    private ByTimeoutConfiguration config;
    private Selenium client;

    public TimedWaitFor(ByTimeoutConfiguration config, Selenium client)
    {
        this.config = config;
        this.client = client;
    }

    public boolean run()
    {
        long startTime = System.currentTimeMillis();
        while (true)
        {
            long timePassed = System.currentTimeMillis() - startTime;
            if (timePassed >= config.getMaxWaitTime())
            {
                if(config.getCondition().executeTest(client))
                {
                    return false;
                }
                else
                {
                    //Timed out waiting for condition
                    return true;
                }

            }
            else if (config.getCondition().executeTest(client))
            {
                return false;
            }

            try
            {
                Thread.sleep(config.getConditionCheckInterval());
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException("Thread was interupted", e);
            }
        }
    }
}
