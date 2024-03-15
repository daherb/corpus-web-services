package de.uni_hamburg.corpora.server;

import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Herbert Lange
 * @version 20240315
 * Main class creating the web server and handling a list of threads
 */
@SpringBootApplication
public class Main {

    // List of threads used in CorpusChecker
    private final static ArrayList<Thread> threads = new ArrayList<>();

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
        SpringApplication.run(Main.class, args);
    }
}

