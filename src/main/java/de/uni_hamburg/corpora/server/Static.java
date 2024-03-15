package de.uni_hamburg.corpora.server;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
/**
 * @author Herbert Lange
 * @version 20240315
 * Class handling static files
 */
@RestController
public class Static {
    private static final Logger logger = Logger.getLogger(Static.class.getName());

    @GetMapping("/static/{*staticFile}")
    public ResponseEntity<byte[]> getStatic(@PathVariable("staticFile") String fileName) {
        logger.info("Loading static file /static" + fileName);
        try (InputStream stream = this.getClass().getModule().getResourceAsStream("static" + fileName)) {
            // Try to guess the mime type for some common file extensions
            String type = getMimeType(fileName);
            // Update header to represent file type
            final HttpHeaders httpHeaders= new HttpHeaders();
            httpHeaders.setContentType(new MediaType(MimeType.valueOf(type)));
            return new ResponseEntity<>(stream.readAllBytes(),httpHeaders, HttpStatus.OK);
        }
        catch (Exception e) {
        	return new ResponseEntity<>("Error loading resource".getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Guesses the mime type based on the file extension
     * @param fileName the filename
     * @return the mime type as string
     */
    @NotNull
    private static String getMimeType(String fileName) {
        String type;
        if (fileName.toLowerCase().endsWith(".js")) {
            type = "application/javascript";
        }
        else if (fileName.toLowerCase().endsWith(".css")) {
            type = "text/css";
        }
        else if (fileName.toLowerCase().endsWith(".png")) {
            type = "image/png";
        }
        // Use octet stream as default
        else {
            type = "application/octet-stream";
        }
        return type;
    }
}
