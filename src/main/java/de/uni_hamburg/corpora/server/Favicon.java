package de.uni_hamburg.corpora.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Favicon
 */

@Path("/{fileName: .*ico}")
public class Favicon {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces({"image/x-icon"})
    public Response getFavicon() {
        logger.info("Here");
        return Response.ok().build() ;
    }
}
