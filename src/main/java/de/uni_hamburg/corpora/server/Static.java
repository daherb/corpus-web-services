package de.uni_hamburg.corpora.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20220405
 * Class handling static files
 */
@Path("/static/{staticFile}")
public class Static {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    /**
     * Method handling HTTP GET requests for static files. The returned object will be sent
     * to the client
     *
     * @return Response containing the file or an error code
     */
    @GET
    public Response getStatic(@PathParam("staticFile") String fileName) {
        logger.info("Loading file " + fileName);
        try {
            String type;
            if (fileName.toLowerCase().endsWith("js")) {
                type = "application/javascript";
            }
            else if (fileName.toLowerCase().endsWith("css")) {
                type = "text/css";
            }
            else {
                type = "application/octet-stream";
            }
            return Response.ok(this.getClass().getModule().getResourceAsStream("static/" + fileName).readAllBytes())
                    .type(type)
                    .build();
        }
        catch (IOException e) {
            return Response.status(500, "Error loading resource").build();
        }
    }
}
