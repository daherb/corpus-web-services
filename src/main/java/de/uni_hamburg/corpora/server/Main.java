package de.uni_hamburg.corpora.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.velocity.app.Velocity;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * Main class creating the web server and handling a list of threads
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final static ArrayList<Thread> threads = new ArrayList<>();
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in de.uni_hamburg.corpora package
        final ResourceConfig rc = new ResourceConfig().packages("de.uni_hamburg.corpora.server");
        //rc.register(new LoggingFeature((java.util.logging.Logger) null));
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Adds thread to list of all running threads to be joined later.
     *
     * @param t the thread to be added
     */
    public static void addThread(Thread t) {
        Main.threads.add(t);
    }

    /**
     * Cleanup threads by joining them.
     *
     * @throws InterruptedException the interrupted exception
     */
    public static void cleanupThreads() throws InterruptedException {
        for (Thread t : Main.threads) {
            t.join();
        }
    }
    /**
     * Main method.
     * @param args No command line arguments expected
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.logger.info("Starting server");
        final HttpServer server = startServer();
        System.out.printf("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...%n", BASE_URI);
        // initialize the template engine
        Velocity.setProperty(Velocity.RESOURCE_LOADERS, "classpath");
        Velocity.addProperty(
                "resource.loader.classpath.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init();
        // try to open url in browser
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(BASE_URI));
            } catch (IOException | URISyntaxException e) {
                main.logger.info("Problem opening URI");
            }
        }
        else {
            main.logger.info("Problem opening browser");
        }
        // Add proper shutdown when server is exited
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            main.logger.info("Shutting down server");
            server.shutdown();
            try {
                Main.cleanupThreads();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        main.mainWait();

    }
    synchronized void mainWait() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

