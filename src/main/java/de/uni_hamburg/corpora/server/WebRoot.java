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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

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
        try {
            //putStream template = this.getClass().getModule().getResourceAsStream("templates/root.vm");
//            logger.info("Test: " + new String(this.getClass().getModule().getResourceAsStream("templates/root.vm")
//            .readAllBytes()) + " # ");
            StringWriter result = new StringWriter();
            VelocityContext context = new VelocityContext();
            context.put("functions", new ListCorpusFunctions().listFunctions());
            Velocity.evaluate(context,result,"webroot",new String(this.getClass().getModule()
                    .getResourceAsStream("templates/root.vm").readAllBytes()));
            return Response.ok(result.toString()).build() ;
        } catch (IOException e) {
            logger.info("Got exception " + e);
            return Response.status(500,"Template not found").build();
        }
//        URL res = this.getClass().getClassLoader().getResource("templates/root.vm");
        //URL res = this.getClass().getResource("template/root.vm");
        //if (res == null)
        //    return Response.status(500,"Template not found").build();
//        StringWriter result = new StringWriter();
//        VelocityContext context = new VelocityContext();
//        context.put("functions", new ListCorpusFunctions().listFunctions());
//        Velocity.mergeTemplate("templates/root.vm", "UTF-8",context,result) ;
//        return Response.ok(result.toString()).build() ;
    }
}
