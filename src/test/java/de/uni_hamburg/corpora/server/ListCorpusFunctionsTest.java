package de.uni_hamburg.corpora.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * Test for the list_corpus_functions resource
 */
public class ListCorpusFunctionsTest {

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
        logger.info("Run listFunctions test");
        Response rsp = target.path("list_corpus_functions").request().buildGet().invoke();
        // First check the media type in the header
        assertEquals("Media type is HTML", MediaType.TEXT_HTML, rsp.getMediaType().toString());
        String responseMsg = rsp.readEntity(String.class);
        // Then read the body as xml and look for table rows
        try {
            Document response = new SAXBuilder().build(new StringReader(responseMsg)) ;
            assertEquals("DocType is HTML", new DocType("html").toString(),response.getDocType().toString());
            assertEquals("Root element is html", "html",response.getRootElement().getName());
            // Get all rows from the table
            XPath rows = XPath.newInstance("//tr");
            assertNotEquals("There should be at least some functions in the table",0,rows.selectNodes(response).size());
        } catch (JDOMException | IOException e) {
            fail("Unable to parse the response as DOM");
        }
    }
}
