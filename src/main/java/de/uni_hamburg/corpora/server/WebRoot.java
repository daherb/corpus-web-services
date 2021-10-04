package de.uni_hamburg.corpora.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.net.URL;

/**
 * /**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
 * Root resource (exposed at "/" path)
 */
@Path("/")
public class WebRoot {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getRoot() {
        ClassLoader cl = this.getClass().getClassLoader();
        URL res = this.getClass().getClassLoader().getResource("templates/root.vm");
        if (res == null)
            return Response.status(500,"Template not found").build();
        StringWriter result = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("functions", new ListCorpusFunctions().listFunctions());
        Velocity.mergeTemplate("templates/root.vm", "UTF-8",context,result) ;
        return Response.ok(result.toString()).build() ;
    }
}
