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

// Worker thread
class CorpusThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    Report report = new Report();
    String inFile ;
    String functionNames;
    String outFile;

    CorpusThread(String infile, String outfile, String functions) {
        this.inFile = infile;
        this.functionNames = functions ;
        this.outFile = outfile;
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
 * Root resource (exposed at "corpus" path)
 */
@Path("check_corpus")
public class CorpusChecker {

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
    public String checkCorpus(@QueryParam("input") String input, @QueryParam("output") String output, @QueryParam("functions") String functions) {
        //String defaultInfile = "/home/herb/projects/code/hamburg/testcorpora/exmeralda/MEDIA-TEST-BATTERY" ;
        String defaultInfile = "/home/herb/projects/code/hamburg/testcorpora/exmeralda/hmat" ;
        //String defaultOutfile = "/tmp/MEDIA-TEST-BATTERY.html" ;
        String defaultOutfile = "/tmp/hmat.html" ;
        String defaultFunctions = "";
        defaultFunctions += "ExbTimelineChecker" ; // Does not seem to exist
        defaultFunctions += ",ExbFileReferenceChecker" ;
        defaultFunctions += ",ExbFileCoverageChecker" ;
        defaultFunctions += ",ExbStructureChecker" ;
        defaultFunctions += ",RemoveAutoSaveExb"; // heap error with raw statistics
        defaultFunctions += ",ExbTierDisplayNameChecker";
        defaultFunctions += ",IAAFunctionality" ;
        defaultFunctions += ",ExbEventLinebreaksChecker" ;
        defaultFunctions += ",ExbSegmentationChecker" ;
        defaultFunctions += ",RemoveEmptyEvents" ; // weird references in error statistics
        defaultFunctions += ",ExbMP3Next2WavAdder" ;
        defaultFunctions += ",ExbRefTierChecker" ;
        defaultFunctions += ",ExbScriptMixChecker" ;
        defaultFunctions += ",RemoveAbsolutePaths"; // heap error with raw statistics
        defaultFunctions += ",XSLTChecker" ;
        defaultFunctions += ",CorpusDataRegexReplacer" ;
        defaultFunctions += ",NullChecker" ;
        defaultFunctions += ",ExbLangCodes" ;
        // String defaultFunctions = "NullChecker" ;
        if (input == null) {
            input = defaultInfile ;
        }
        if (output == null) {
            output = defaultOutfile ;
        }
        if (functions == null) {
            functions = defaultFunctions;
        }
        CorpusThread ct = new CorpusThread(input,output,functions);
        ct.start();
        Main.addThread(ct);
        return ("Executing " + functions + " on " + input + ". Result will be in " + output) ;
    }

}
