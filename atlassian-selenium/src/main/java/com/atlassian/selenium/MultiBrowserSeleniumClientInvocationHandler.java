package com.atlassian.selenium;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MultiBrowserSeleniumClientInvocationHandler implements InvocationHandler {
    private List<SeleniumClient> clients;
    private final long MAX_WAIT;
    private final boolean VERIFY_RETURN_VALUES;
    private final boolean PARALLEL;

    private ExecutorService executorService;

    public MultiBrowserSeleniumClientInvocationHandler(List<SeleniumConfiguration> configs, long maxWait,
                                                       boolean verifyReturnValues, boolean parallel)
    {
        clients = new LinkedList<SeleniumClient>();

        for (SeleniumConfiguration config : configs)
        {
            SeleniumClient client = new SingleBrowserSeleniumClient(config);
            client.start();
            clients.add(client);
        }
        this.VERIFY_RETURN_VALUES = verifyReturnValues;
        this.PARALLEL = parallel;

        if(PARALLEL)
        {
            executorService = Executors.newFixedThreadPool(clients.size());
            this.MAX_WAIT = maxWait;
        }
        else
        {
            // i.e. sequential execution
            executorService = Executors.newSingleThreadExecutor();
            this.MAX_WAIT = maxWait * clients.size();
        }
    }


    public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o;

        List<Future<Object>> futures = new ArrayList<Future<Object>>(clients.size());

        for (final SeleniumClient client : clients)
        {
            futures.add(executorService.submit(new MethodHandlerCallable(method, client, args)));
        }
        executorService.awaitTermination(MAX_WAIT, TimeUnit.MILLISECONDS);
        if(VERIFY_RETURN_VALUES)
        {
            verifyReturnValues(futures);
        }

        return futures.get(0).get();
    }

    public void verifyReturnValues(List <Future<Object>> futures) throws Throwable
    {
        if (futures.size() > 0)
        {
            Object first = futures.get(0).get();
            for(int i = 0; i < clients.size(); i++)
            {
                Object value = futures.get(i).get();
                if (first == null)
                {
                    if (value != null)
                    {
                        throw createValueMismatchException(clients.get(0), first, clients.get(i), value);
                    }
                }
                else
                {
                    if (!first.equals(value))
                    {
                        throw createValueMismatchException(clients.get(0), first, clients.get(i), value);
                    }
                }
            }
        }
    }

    protected SeleniumReturnValueMismatch createValueMismatchException(SeleniumClient c1, Object h1,
                                                                       SeleniumClient c2, Object h2) {
        return new SeleniumReturnValueMismatch(c1.getBrowser(), h1, c2.getBrowser(), h2);
    }

}
