package de.uni_hamburg.corpora.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
//import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni_hamburg.corpora.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.exmaralda.partitureditor.jexmaralda.JexmaraldaException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20220405
 * Worker thread for the corpus checker
 */
class CorpusThread extends Thread {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(CorpusThread.class.getName());

    de.uni_hamburg.corpora.Report report = new de.uni_hamburg.corpora.Report();
    String corpusName;
    String inFile ;
    String functionNames;
    String outFile;
    Properties props; // The properties for the function calls
    String token; // Identifier to be sent back to the server to identify and authorize task
    String callbackUrl; // URL to be called when the task is done, giving an empty string means skipping the callback

    CorpusThread(String name, String infile, String outfile, String functions, Properties properties, String token,
                 String callbackUrl) throws IOException {
        this.corpusName=name;
        if (infile.equals("tmp")) {
            if (System.getProperty("corpusDir") != null)
                this.inFile = System.getProperty("corpusDir");
            else
                throw new IOException("Corpus files not ready");
        }
        else
            this.inFile = infile;
        this.functionNames = functions ;
        this.props = properties;
        this.token = Objects.requireNonNullElse(token, "tmp");
        if (outfile.equals("tmp")) {
            File logDir = Paths.get(System.getProperty("java.io.tmpdir"),token).toFile();
            // Create parent directory if it is missing
            if (!logDir.exists())
                if (!logDir.mkdirs())
                    throw new IOException("Failed to create folders "+ logDir);
            this.outFile = Paths.get(logDir.toString(), "report.html").toString();
        }
        else
            this.outFile = outfile;

        this.callbackUrl = callbackUrl ;
        logger.info("Input: " + this.inFile + " output: " + this.outFile + " functions: " + functions + " params: " +
                this.props + " token: " + this.token + " callback: " + this.callbackUrl);

    }

    public void run() {
        ArrayList<String> functionList = new ArrayList<>(Arrays.asList(functionNames.split(",")));
        Set<String> allFunctions = CorpusServices.getCorpusFunctions() ;
        CorpusIO cio = new CorpusIO();
        report.addNote("CorpusWebServices","Starting run at " +
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));
        try {
            // Create corpus from given input file/folder
            Corpus corpus;
            if (corpusName != null && !corpusName.isEmpty()) {
                URL baseUrl = new File(inFile).toURI().toURL();
                corpus = new Corpus(corpusName,baseUrl,cio.read(baseUrl,report)) ;
            }
            else
                corpus = new Corpus(cio.read(new File(inFile).toURI().toURL(),report)) ;
            logger.info("Loaded " + corpus.getCorpusData().size() + " corpus files");
            logger.info("Got report: " + report.getFullReports());
            // For all functions to be applied, get their canonical name and create an object for them
            Set<CorpusFunction> functions = new HashSet<>() ;
            for (String function : functionList) {
                // Indicator if we encountered the function
                boolean found = false ;
                for (String canonical : allFunctions) {
                    if (canonical.toLowerCase(Locale.ROOT).endsWith("." + function.toLowerCase(Locale.ROOT))) {
                        // Create an object from canonical name. calls the constructor with thr constructor setting hasfixingoption to false
                        try {
                            functions.add((CorpusFunction) Class.forName(canonical).getDeclaredConstructor(Properties.class).newInstance(props));
                            found = true ;
                        }
                        catch (IllegalArgumentException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
//                            logger.warn("Error creating {}", canonical);
                            logger.warning("Error creating" + canonical);
                            report.addWarning("CorpusWebServices", "Test " + function + " cannot be created");
//                            e.printStackTrace();
                        }
                    }
                }
                if (!found) {
                    // Warn if we could not find the function
                    report.addWarning("CorpusWebServices", "Test " + function + " is not available");
                    logger.log(Level.WARNING,"Function {} is not available in corpus services", function);

                }
            }
            for (CorpusFunction f : functions) {
                logger.log(Level.WARNING,"Running function {}", f.getFunction());
                report.addNote("CorpusWebServices", "Run test " + f.getFunction());
                de.uni_hamburg.corpora.Report result = f.execute(corpus);
                report.merge(result);
                report.addNote("CorpusWebServices", "Finish test " + f.getFunction());
                logger.log(Level.WARNING,"Done with function {}", f.getFunction());
            }
        } catch (URISyntaxException | ClassNotFoundException | IOException | SAXException | JexmaraldaException e) {
            e.printStackTrace();
        }

        logger.info("Done with all functions");
        report.addNote("CorpusWebServices","Finished all tests at " +
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));
        logger.info("Creating report");
        // Get summary
        HashMap<ReportItem.Severity,Integer> summary = CorpusServices.generateSummary(report);
        // try to convert to JSON
        String jsonSummary = "{}";
        try {
            jsonSummary = new ObjectMapper().writeValueAsString(summary);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Generate HTML report
        Collection<ReportItem> rawStatistics = report.getRawStatistics();
        String reportOutput = ReportItem.generateDataTableHTML(new ArrayList<>(rawStatistics),
                report.getSummaryLines());
        // Alternative: Generate XML
        //XStream xstream = new XStream();
        //String reportOutput = xstream.toXML(rawStatistics);

        // Cleanup folder
        try {
            FileUtils.deleteDirectory(new File(inFile));
            System.getProperties().remove("corpusDir");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Writing report");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            out.write(reportOutput);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Done with report");
        // cleanup corpus data
        try {
            FileUtils.deleteDirectory(new File(this.inFile));
        } catch (IOException e) {
            logger.info("Exception when deleting input directory");
        }
        if (!callbackUrl.equals("")) {
            Client c = ClientBuilder.newClient();
            logger.info("Contacting callback");
            try {
                c.target(callbackUrl)
                        .queryParam("token", token)
                        .queryParam("output", outFile)
                        .request().header("Connection", "close")
                        .buildPost(Entity.json(jsonSummary))
                        .invoke()
                        .close();
                logger.info("Done with callback");
            }
            catch (Exception e) {
                logger.log(Level.SEVERE,"Failed contacting callback", e);
            }
        }

    }
}

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210630
 * Resource to run the corpus checker on a corpus
 */
@Path("check_corpus")
public class CorpusChecker {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(CorpusChecker.class.getName());
    

    public CorpusChecker() {

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkCorpus(@QueryParam("name") String name,
                                @QueryParam("input") String input,
                                @QueryParam("output") String output,
                                @QueryParam("functions") String functions,
                                @QueryParam("params") String paramStr,
                                @QueryParam("token") String token,
                                @QueryParam("callback") String callbackUrl) throws IOException {
        boolean error = false ;
        ArrayList<String> missing = new ArrayList<>();
        if (input == null) {
            error = true ;
            missing.add("input");
            //input = defaultInfile ;
        }
        if (output == null) {
            error = true ;
            missing.add("output") ;
            //output = defaultOutfile;
        }
        if (functions == null) {
            error = true ;
            missing.add("functions") ;
            //functions = defaultFunctions ;
        }
        if (token == null) {
            error = true ;
            missing.add("token") ;
            //token = defaultToken ;
        }
        if (callbackUrl == null) {
            error = true ;
            missing.add("callback") ;
            //callbackUrl = defaultCallbackUrl ;
        }
        if (error) {
            String errorMsg = "Missing parameters: " + String.join(", ", missing);
            logger.log(Level.SEVERE,errorMsg);
            return Response.status(400).entity("400 - " + errorMsg).build();
        }
        Properties params = new Properties();
        if (paramStr != null && !paramStr.equals("{}")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(paramStr);
                // Copy the jason data into the properties
                for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> e = it.next();
                    params.put(e.getKey(),e.getValue().textValue());
                }
                logger.info(params.toString());
                //params.putAll(mapper.convertValue(paramStr,Map.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            params = new Properties();
        }
        CorpusThread ct = new CorpusThread(name,input,output,functions,params,token,callbackUrl);
        ct.start();
        Main.addThread(ct);
        return Response.ok().entity("Executing " + functions + " on " + input +
                ". Result will be in " + output +
                ". When finished " + callbackUrl + " will be accessed using " + token).build() ;
    }

}
