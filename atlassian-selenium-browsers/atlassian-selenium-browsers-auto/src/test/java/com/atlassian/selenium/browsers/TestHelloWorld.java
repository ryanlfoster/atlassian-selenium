package com.atlassian.selenium.browsers;

import com.atlassian.selenium.SeleniumClient;
import junit.framework.TestCase;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ServerSocket;

import static com.atlassian.selenium.browsers.AutoInstallClient.assertThat;
import static com.atlassian.selenium.browsers.AutoInstallClient.seleniumClient;

/**
 *
 */
public class TestHelloWorld extends TestCase
{
    public void testHelloWorld() throws Exception
    {
        int port = pickFreePort();
        Server server = startServer(port);
        System.setProperty("selenium.browser", "firefox-3.5");
        System.setProperty("baseurl", "http://localhost:" + port);
        SeleniumClient client = seleniumClient();
        client.open("/");
        assertThat().textPresent("Hello");
        server.stop();
    }

    private Server startServer(int port) throws Exception
    {
        Handler handler=new AbstractHandler()
        {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
                throws IOException, ServletException
            {
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("<h1>Hello</h1>");
                ((Request)request).setHandled(true);
            }
        };

        Server server = new Server(port);
        server.setHandler(handler);
        server.start();
        return server;
    }

    static int pickFreePort()
    {
        ServerSocket socket = null;
        try
        {
            socket = new ServerSocket(0);
            return socket.getLocalPort();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error opening socket", e);
        }
        finally
        {
            if (socket != null)
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Error closing socket", e);
                }
            }
        }
    }
}
