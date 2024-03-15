package de.uni_hamburg.corpora.server;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20220405
 * Class handling font files
 */

@RestController
public class Font {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(Font.class.getName());

    @GetMapping("/fonts/{*staticFile}")
    public ResponseEntity<byte[]> getFont(@PathVariable("staticFile") String fileName) {
        logger.info("Loading font file /fonts" + fileName);
        try (InputStream stream = this.getClass().getModule().getResourceAsStream("fonts" + fileName)){
            final HttpHeaders httpHeaders= new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(stream.readAllBytes(),httpHeaders, HttpStatus.OK);
        }
        catch (IOException e) {
            return new ResponseEntity<>("Error loading resource".getBytes(StandardCharsets.UTF_8), HttpStatus.valueOf(500));
        }
    }
}
