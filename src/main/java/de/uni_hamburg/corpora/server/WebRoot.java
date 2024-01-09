package de.uni_hamburg.corpora.server;

//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.Produces;
//import jakarta.ws.rs.core.MediaType;
//
//import jakarta.ws.rs.core.Response;
////import org.apache.velocity.VelocityContext;
////import org.apache.velocity.app.Velocity;
////import org.apache.velocity.Template;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.http.MediaType;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.Arrays;

/**
 * /**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
 * Root resource (exposed at "/" path)
 */
@RestController
public class WebRoot {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(WebRoot.class.getName());

    @GetMapping("/")
//    @Produces(MediaType.TEXT_HTML)
    public ServerResponse getRoot() {
//        try {
            //putStream template = this.getClass().getModule().getResourceAsStream("templates/root.vm");
//            logger.info("Test: " + new String(this.getClass().getModule().getResourceAsStream("templates/root.vm")
//            .readAllBytes()) + " # ");
            StringWriter result = new StringWriter();
//            VelocityContext context = new VelocityContext();
//            context.put("functions", new ListCorpusFunctions().listFunctions());
//            Velocity.evaluate(context,result,"webroot",new String(this.getClass().getModule()
//                    .getResourceAsStream("templates/root.vm").readAllBytes()));
            return ServerResponse
            		.ok()
            		.contentType(MediaType.TEXT_HTML)
            		.body(result.toString());
//        } 
//        catch (IOException e) {
//            logger.info("Got exception " + e);
//            return Response.status(500,"Template not found").build();
//        }
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
