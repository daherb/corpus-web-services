package de.uni_hamburg.corpora.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210708
 * Test for the CorpusServices methods
 */
public class CorpusServicesTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().toString());

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO implement the tests
}
