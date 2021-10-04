package de.uni_hamburg.corpora.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.junit.Assert.*;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210705
 * Test for the list_corpus_types resource
 */
public class ListCorpusTypesTest {

    private HttpServer server;
    private WebTarget target;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().toString());

    @Before
    public void setUp() {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() {
        server.shutdown();
    }

    /**
     * Test to see if we get a HTML file containing table rows
     */
    @Test
    public void testListFunctions() {
        logger.info("Run listCorpusTypes test");
        Response rsp = target.path("list_corpus_types").request().buildGet().invoke();
        // First check the media type in the header
        assertEquals("Media type is plain text", MediaType.TEXT_PLAIN, rsp.getMediaType().toString());
        String responseMsg = rsp.readEntity(String.class);
        // Then read the body as xml and look for table rows
        String[] lines = responseMsg.split("\n");
        assertNotEquals("There should be at least some functions in the table",0,lines.length);
    }
}
