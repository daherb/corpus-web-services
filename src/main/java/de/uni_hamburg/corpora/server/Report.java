package de.uni_hamburg.corpora.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
 * Resource to show an existing local report
 * Scope: local
 */
@RestController
public class Report {

    private static final Logger logger = Logger.getLogger(Report.class.getName());
    

    @GetMapping("report")
    public ResponseEntity<String> getReport(@RequestParam("token") Optional<String> token) {
        // TODO place report somewhere else because files should be deleted after check
        if (token.isEmpty())
            token = Optional.of("tmp");
        String reportFileName = System.getProperty("java.io.tmpdir") + "/" + token.get() + "/report.html";
        logger.info("Loading report file " + reportFileName);
        File reportFile = new File(reportFileName);
        if (!reportFile.exists()) {
            logger.info("Report is missing");
            return new ResponseEntity<>("Error loading file", HttpStatus.NOT_FOUND);
        }
        else {
            try (InputStream stream = new FileInputStream(reportFile)){
            	return new ResponseEntity<>(new String(stream.readAllBytes()), HttpStatus.OK);
            } catch (IOException e) {
            	return new ResponseEntity<>("Error loading file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
