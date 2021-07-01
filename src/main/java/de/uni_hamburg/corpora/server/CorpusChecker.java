package de.uni_hamburg.corpora.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import de.uni_hamburg.corpora.*;

import org.exmaralda.partitureditor.jexmaralda.JexmaraldaException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210630
 * Worker thread for the corpus checker
 */
class CorpusThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    Report report = new Report();
    String inFile ;
    String functionNames;
    String outFile;
    String token; // Identifier to be sent back to the server to identify and authorize task
    String callbackUrl; // URL to be called when the task is done, giving an empty string means skipping the callback

    CorpusThread(String infile, String outfile, String functions, String token, String callbackUrl) {
        this.inFile = infile;
        this.functionNames = functions ;
        this.outFile = outfile;
        this.token = token ;
        this.callbackUrl = callbackUrl ;
    }

    public void run() {
        ArrayList<String> functionList = new ArrayList<String>(Arrays.asList(functionNames.split(",")));
        Set<String> allFunctions = CorpusServices.getCorpusFunctions() ;
        CorpusIO cio = new CorpusIO();

        try {
            // Create corpus from given input file/folder
            Corpus corpus = new Corpus(cio.read(new File(inFile).toURI().toURL())) ;
            System.out.println("Loaded " + Integer.toString(corpus.getCorpusData().size()) + " files");
            // For all functions to be applied, get their canonical name and create an object for them
            Set<CorpusFunction> functions = new HashSet<CorpusFunction>() ;
            for (String function : functionList) {
                // Indicator if we encountered the function
                boolean found = false ;
                for (String canonical : allFunctions) {
                    if (canonical.toLowerCase(Locale.ROOT).contains(function.toLowerCase(Locale.ROOT))) {
                        // Create an object from canonical name. calls the constructor with thr constructor setting hasfixingoption to false
                        try {
                            // functions.add((CorpusFunction) Class.forName(canonical).getDeclaredConstructor(boolean.class).newInstance(false));
                            functions.add((CorpusFunction) Class.forName(canonical).getDeclaredConstructor().newInstance());
                            found = true ;
                        }
                        catch (IllegalArgumentException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                            logger.warn("Error creating {}", canonical);
                            report.addWarning("CorpusService", "Test " + function + " cannot be created");
//                            e.printStackTrace();
                        }
                    }
                }
                if (!found) {
                    // Warn if we could not find the function
                    report.addWarning("CorpusService", "Test " + function + " is not available");
                    logger.warn("Function {} is not available in corpus services", function);

                }
            }
            for (CorpusFunction f : functions) {
                logger.warn("Running function {}", f.getFunction());
                report.addNote("CorpusService", "Run test " + f.getFunction());
                Report result = f.execute(corpus);
                report.merge(result);
                report.addNote("CorpusService", "Finish test " + f.getFunction());
                logger.warn("Done with function {}", f.getFunction());
            }
        } catch (URISyntaxException | ClassNotFoundException | IOException | SAXException | JexmaraldaException e) {
            e.printStackTrace();
        }

        logger.warn("Done with all functions");
        report.addNote("CorpusService","Finished all tests");
        logger.warn("Creating report");
        // Generate HTML report
        Collection<ReportItem> rawStatistics = report.getRawStatistics();
        logger.warn("Got {} items", rawStatistics.size());
        String reportOutput = report.getSummaryLines(); ReportItem.generateDataTableHTML(report.getRawStatistics(), report.getSummaryLines());
        // Alternative: Generate XML
        //XStream xstream = new XStream();
        // either all reports
        //String reportOutput = xstream.toXML(report.getRawStatistics());
        // or just errors
        //String reportOutput = xstream.toXML(report.getErrorStatistics());
        logger.warn("Writing report");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(outFile)));
            out.write(reportOutput);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.warn("Done with report");
    }
}

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210630
 * Resource to run the corpus checker on a corpus
 */
@Path("check_corpus")
public class CorpusChecker {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

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
    public Response checkCorpus(@QueryParam("input") String input,
                              @QueryParam("output") String output,
                              @QueryParam("functions") String functions,
                              @QueryParam("token") String token,
                              @QueryParam("callback") String callbackUrl) {
        //String defaultInfile = "/home/herb/projects/code/hamburg/testcorpora/exmeralda/MEDIA-TEST-BATTERY" ;
        //String defaultInfile = "/home/herb/projects/code/hamburg/testcorpora/exmeralda/hmat" ;
        //String defaultOutfile = "/tmp/MEDIA-TEST-BATTERY.html" ;
        //String defaultOutfile = "/tmp/hmat.html" ;
        //String defaultToken = "0xdeadbeef";
        //String defaultCallbackUrl = "http://localhost:8081/callback";
        //String defaultFunctions = "";
        //defaultFunctions += "ExbTimelineChecker" ; // Does not seem to exist
        //defaultFunctions += ",ExbFileReferenceChecker" ;
        //defaultFunctions += ",ExbFileCoverageChecker" ;
        //defaultFunctions += ",ExbStructureChecker" ;
        //defaultFunctions += ",RemoveAutoSaveExb"; // heap error with raw statistics
        //defaultFunctions += ",ExbTierDisplayNameChecker";
        //defaultFunctions += ",IAAFunctionality" ;
        //defaultFunctions += ",ExbEventLinebreaksChecker" ;
        //defaultFunctions += ",ExbSegmentationChecker" ;
        //defaultFunctions += ",RemoveEmptyEvents" ; // weird references in error statistics
        //defaultFunctions += ",ExbMP3Next2WavAdder" ;
        //defaultFunctions += ",ExbRefTierChecker" ;
        //defaultFunctions += ",ExbScriptMixChecker" ;
        //defaultFunctions += ",RemoveAbsolutePaths"; // heap error with raw statistics
        //defaultFunctions += ",XSLTChecker" ;
        //defaultFunctions += ",CorpusDataRegexReplacer" ;
        //defaultFunctions += ",NullChecker" ;
        //defaultFunctions += ",ExbLangCodes" ;
        //String defaultFunctions = "NullChecker" ;
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
            String errorMsg = "Missing parameters: " + missing.stream().reduce((s1, s2) -> s1 + ", " + s2).get();
            logger.error(errorMsg);
            return Response.status(400).entity("400 - " + errorMsg).build();
        }
        CorpusThread ct = new CorpusThread(input,output,functions,token,callbackUrl);
        ct.start();
        Main.addThread(ct);
        return Response.ok().entity("Executing " + functions + " on " + input +
                ". Result will be in " + output +
                ". When finished " + callbackUrl + " will be accessed using " + token).build() ;
    }

}
