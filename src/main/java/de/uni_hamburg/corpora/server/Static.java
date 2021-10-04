package de.uni_hamburg.corpora.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
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
        // Get file from resource
        URL resource = this.getClass().getClassLoader().getResource("static/"+ fileName);
        if (resource == null) {
            logger.info("Resource is null");
            return Response.status(404,"Invalid static file").build();
        }
        else {
            try {
                File staticFile = new File(resource.toURI());
                logger.info("Resource is " + resource.toURI());
                return Response.ok(new FileInputStream(staticFile),Files.probeContentType(staticFile.toPath())).build();
            } catch (URISyntaxException | IOException e) {
                return Response.status(500, "Error loading file").build();
            }
        }
    }
}
