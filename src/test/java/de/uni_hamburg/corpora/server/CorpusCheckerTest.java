package de.uni_hamburg.corpora.server;

//import org.glassfish.grizzly.http.server.HttpServer;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import jakarta.ws.rs.client.Client;
//import jakarta.ws.rs.client.ClientBuilder;
//import jakarta.ws.rs.client.WebTarget;
//import jakarta.ws.rs.core.Response;
//
//import static org.junit.Assert.*;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * Test for the check_corpus resource
 */
public class CorpusCheckerTest {

//    private HttpServer server;
//    private WebTarget target;
//    private final Logger logger = LoggerFactory.getLogger(this.getClass().toString());
//
//    @Before
//    public void setUp() {
//        // start the server
//        server = Main.startServer();
//        // create the client
//        Client c = ClientBuilder.newClient();
//
//        // uncomment the following line if you want to enable
//        // support for JSON in the client (you also have to uncomment
//        // dependency on jersey-media-json module in pom.xml and Main.startServer())
//        // --
//        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());
//
//        target = c.target(Main.BASE_URI);
//    }
//
//    @After
//    public void tearDown() {
//        server.shutdown();
//    }
//
//    /**
//     * Test to see if we get a HTML file containing table rows
//     */
//    @Test
//    public void testListFunctions() {
//        logger.info("Run checkCorpus test");
//        // Test the error cases
//        // First completely without parameter should complain about missing callback
//        Response rsp = target.path("check_corpus")
//                .request()
//                .buildGet()
//                .invoke();
//        assertEquals("No parameter causes error code", 400, rsp.getStatus());
//        assertEquals("No parameter causes error message", "400 - Missing parameters: input, output, functions, token, callback", rsp.readEntity(String.class));
//        // Now give an input, others still missing
//        rsp = target.path("check_corpus")
//                .queryParam("input","testcorpora/exmeralda/hmat")
//                .request()
//                .buildGet()
//                .invoke();
//        assertEquals("Missing parameter causes error code", 400, rsp.getStatus());
//        assertEquals("Missing parameter causes error message", "400 - Missing parameters: output, functions, token, callback", rsp.readEntity(String.class));
//        // Now give an input and output, others still missing
//        rsp = target.path("check_corpus")
//                .queryParam("input","testcorpora/exmeralda/hmat")
//                .queryParam("output","hmat.html")
//                .request()
//                .buildGet()
//                .invoke();
//        assertEquals("Missing parameter causes error code", 400, rsp.getStatus());
//        assertEquals("Missing parameter causes error message", "400 - Missing parameters: functions, token, callback", rsp.readEntity(String.class));
//        // Now give an input, output and functions, others still missing
//        rsp = target.path("check_corpus")
//                .queryParam("input","testcorpora/exmeralda/hmat")
//                .queryParam("output","hmat.html")
//                .queryParam("functions","NullChecker")
//                .request()
//                .buildGet()
//                .invoke();
//        assertEquals("Missing parameter causes error code", 400, rsp.getStatus());
//        assertEquals("Missing parameter causes error message", "400 - Missing parameters: token, callback", rsp.readEntity(String.class));
//        // Now give an input, output, functions and token, callback still missing
//        rsp = target.path("check_corpus")
//                .queryParam("input","testcorpora/exmeralda/hmat")
//                .queryParam("output","hmat.html")
//                .queryParam("functions","NullChecker")
//                .queryParam("token","0xdeadbeef")
//                .request()
//                .buildGet()
//                .invoke();
//        assertEquals("Missing parameter causes error code", 400, rsp.getStatus());
//        assertEquals("Missing parameter causes error message", "400 - Missing parameters: callback", rsp.readEntity(String.class));
//        // The success case
//        // Now giving all parameters
//        // We do not really check if the corpus checker works
//        rsp = target.path("check_corpus")
//                .queryParam("input","testcorpora/exmeralda/hmat")
//                .queryParam("output","hmat.html")
//                .queryParam("functions","NullChecker")
//                .queryParam("token","0xdeadbeef")
//                .queryParam("callback", "")
//                .request()
//                .buildGet()
//                .invoke();
//        assertEquals("All parameter cause success code", 200, rsp.getStatus());
//        assertEquals("All parameters cause message",
//                "Executing NullChecker on testcorpora/exmeralda/hmat. Result will be in hmat.html. When finished  will be accessed using 0xdeadbeef",
//                rsp.readEntity(String.class));
//        //new NetworkListener()
//    }
}
