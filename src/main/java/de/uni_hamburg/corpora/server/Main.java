package de.uni_hamburg.corpora.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210630
 * Main class creating the web server and handling a list of threads
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private static ArrayList<Thread> threads = new ArrayList<Thread>();
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in de.uni_hamburg.corpora package
        final ResourceConfig rc = new ResourceConfig().packages("de.uni_hamburg.corpora.server");

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
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.logger.warn("Starting server");
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        main.logger.warn("Shutting down server");
        server.shutdown();

        try {
            Main.cleanupThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

