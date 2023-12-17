package de.uni_hamburg.corpora.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
 * Resource to show an existing local report
 * Scope: local
 */
@Path("report")
public class Report {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(Report.class.getName());
    

    @GET
    public Response getReport(@QueryParam("token") String token) {
        // TODO place report somewhere else because files should be deleted after check
        if (token == null)
            token = "tmp";
        String reportFileName = System.getProperty("java.io.tmpdir") + "/" + token + "/report.html";
        logger.info("Loading report file " + reportFileName);
        File reportFile = new File(reportFileName);
        if (!reportFile.exists()) {
            logger.info("Report is missing");
            return Response.status(404,"Invalid report file").build();
        }
        else {
            try {
                return Response.ok(new FileInputStream(reportFile), Files.probeContentType(reportFile.toPath())).build();
            } catch (IOException e) {
                return Response.status(500, "Error loading file").build();
            }
        }
    }
}
