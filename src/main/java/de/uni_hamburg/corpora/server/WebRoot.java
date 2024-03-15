package de.uni_hamburg.corpora.server;

import java.io.StringWriter;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

/**
 * /**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
 * Root resource (exposed at "/" path)
 */
@RestController
public class WebRoot {

    private static final Logger logger = Logger.getLogger(WebRoot.class.getName());

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getRoot()  {
        try {
            StringWriter result = new StringWriter();
            VelocityContext context = new VelocityContext();
            context.put("functions", new ListCorpusFunctions().listFunctions());
            Velocity.evaluate(context, result, "webroot", new InputStreamReader(new ClassPathResource("templates/root.vm").getInputStream()));
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Got exception " + e);
            return new ResponseEntity<>("Template not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
