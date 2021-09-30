package de.uni_hamburg.corpora.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * The Exception handler logging exceptions.
 */
@Provider
public class ExceptionLogger implements ExceptionMapper<Exception> {

    private static final Logger log = LoggerFactory.getLogger("ExceptionLog");

    @Override
    public Response toResponse(Exception e) {
        log.error("Exception:",e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return Response.status(500).entity(sw.toString()).build();
    }
}
