package de.uni_hamburg.corpora.server;

//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.PathParam;
//import jakarta.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.http.MediaType;
/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20220405
 * Class handling static files
 */

public class Static {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(Static.class.getName());

    @GetMapping("/static/{staticFile}")
    public ServerResponse getStatic(@PathVariable("staticFile") String fileName) {
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
            return ServerResponse
            		.ok()
            		.contentType(new MediaType(type))
            		.body(this.getClass().getModule().getResourceAsStream("static/" + fileName).readAllBytes());
        }
        catch (IOException e) {
            return // Response.status(500, "Error loading resource").build();
            		ServerResponse
            		.status(500)
            		.body("Error loading resource");
        }
    }
}
