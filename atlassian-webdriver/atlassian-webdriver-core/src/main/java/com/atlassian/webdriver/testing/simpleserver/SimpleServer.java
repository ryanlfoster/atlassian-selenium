package com.atlassian.webdriver.testing.simpleserver;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Map;

/**
 * Simple server for serving up html pages and other resources from class path. Uses an an embedded Jetty server under
 * the covers.
 *
 * @since 2.2
 */
public class SimpleServer
{
    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);

    private int port = 0;
    private Server server = null;
    private final Map<String,String> urlMappings;

    /**
     * Main method to run the server standalone.
     *
     * @param args program arguments
     * @throws Exception any exception
     */
    public static void main(@Nonnull String... args) throws Exception
    {
        int preferredPort = 5555;
        if (args.length > 0)
        {
            preferredPort = parsePort(args[0]);
        }
        SimpleServer server = new SimpleServer(preferredPort);
        server.startServer();
        int port = server.getPort();
        logger.info("Server started: " + "http://localhost:" + port);
        Runtime.getRuntime().addShutdownHook(new Thread(new ServerShutdown(server)));
    }

    public SimpleServer(int port)
    {
        this(Maps.<String, String>newHashMap(), port);
    }

    public SimpleServer()
    {
        this(Maps.<String, String>newHashMap());
    }

    public SimpleServer(@Nonnull Map<String, String> urlMappings) {
        checkPort();
        this.urlMappings = ImmutableMap.copyOf(urlMappings);
    }

    public SimpleServer(@Nonnull Map<String, String> urlMappings, int port)
    {
        Validate.isTrue(port > 0, "Port must be a positive number");
        this.port = port;
        this.urlMappings = ImmutableMap.copyOf(urlMappings);

        checkPort();
    }

    public void stopServer() throws Exception
    {
        if (server != null)
        {
            server.stop();
        }
    }

    public void startServer() throws Exception
    {
        Handler handler = new AbstractHandler()
        {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
                    throws IOException, ServletException
            {

                String uri = request.getRequestURI();

                if(uri.endsWith("css"))
                {
                    response.setContentType("text/css");
                }
                else  if(uri.endsWith("js"))
                {
                    response.setContentType("application/javascript");
                }
                else
                {
                    response.setContentType("text/html");
                }


                InputStream inputStream = null;
                inputStream = getClass().getClassLoader().getResourceAsStream(uri.substring(1));

                if(inputStream == null && urlMappings.containsKey(uri))
                {
                    String filename = urlMappings.get(uri);
                    inputStream = getClass().getClassLoader().getResourceAsStream(filename);
                }

                if(inputStream != null)
                {
                    String contents = IOUtils.toString(inputStream);

                    if (contents != null)
                    {
                        response.getWriter().print(contents);
                    }
                    else
                    {
                        response.getWriter().println("<h1>Cannot read file at: " + uri + "</h1>");
                    }
                }

                else
                {
                    response.getWriter().println("<h1>File not found at: " + uri + "</h1>");
                }

                response.setStatus(HttpServletResponse.SC_OK);

                ((Request)request).setHandled(true);
            }
        };

        server = new Server(port);
        server.setHandler(handler);
        server.start();
    }

    public int getPort()
    {
        return port;
    }

    private static int parsePort(String port)
    {
        try
        {
            return Integer.parseInt(port);
        }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("Could not parse port, not a number: " + port, e);
        }
    }

    private void checkPort()
    {
        ServerSocket socket = null;
        try
        {
            socket = new ServerSocket(port);
            this.port = socket.getLocalPort();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error opening socket, port: " + port + " may already be in use", e);
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
                    logger.error("Error closing sockets", e);
                }
            }
        }
    }

    private static final class ServerShutdown implements Runnable
    {
        private final SimpleServer server;

        private ServerShutdown(SimpleServer server)
        {
            this.server = server;
        }

        @Override
        public void run()
        {
            try
            {
                logger.info("Shutting down SimpleServer at port " + server.getPort());
                server.stopServer();
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
