package de.uni_hamburg.corpora.server;

//import de.uni_hamburg.corpora.Report;
//import de.uni_hamburg.corpora.ReportItem;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import jakarta.ws.rs.client.Client;
//import jakarta.ws.rs.client.ClientBuilder;
//import jakarta.ws.rs.client.WebTarget;
//import jakarta.ws.rs.core.Response;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.Callable;
//
//import static org.junit.Assert.*;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210708
 * Test for the CorpusServices methods
 */
public class CorpusServicesTest {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass().toString());
//
//    @Before
//    public void setUp() {
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test if the corpus function list is not null and all elements are actually derived from CorpusFunction.
//     */
//    @Test
//    public void testGetCorpusFunctions() {
//        Set<String> functions = CorpusServices.getCorpusFunctions();
//        try {
//            Class corpusFunction = Class.forName("de.uni_hamburg.corpora.CorpusFunction");
//            assertNotEquals("The list is not null", null, functions);
//            for (String f : functions) {
//                assertTrue("A function is derived from CorpusFunction",
//                        corpusFunction.isAssignableFrom(Class.forName(f)));
//            }
//        } catch (ClassNotFoundException e) {
//            fail("Error loading classes from strings");
//        }
//
//    }
//
//    /**
//     * Test if the corpus type list is not null and all elements are actually derived from CorpusData.
//     */
//    @Test
//    public void testGetCorpusTypes() {
//        Set<String> types = CorpusServices.getCorpusTypes();
//        try {
//            Class corpusData = Class.forName("de.uni_hamburg.corpora.CorpusData");
//            assertNotEquals("The list is not null", null, types);
//            for (String t : types) {
//                assertTrue("A type is derived from CorpusData",
//                        corpusData.isAssignableFrom(Class.forName(t)));
//            }
//        } catch (ClassNotFoundException e) {
//            fail("Error loading classes from strings");
//        }
//    }
//
//    /**
//     * Test if the summary contains the information from the report, i.e. the correct severity levels as keys and
//     * the correct sums
//     */
//    @Test
//    public void testGenerateSummary() {
//        Report report = new Report();
//        report.addWarning("main", "Warning 1");
//        report.addWarning("test","Warning 2");
//        report.addCritical("Critical 1");
//        report.addCritical("main","Critical 2");
//        report.addCritical("test","Critical 3");
//        report.addNote("main","Note 1");
//        report.addNote("test", "Note 2");
//        Map<ReportItem.Severity,Integer> summary = CorpusServices.generateSummary(report);
//        assertNotEquals("Map is not null", null, summary);
//        assertFalse("No correct item", summary.containsKey(ReportItem.Severity.CORRECT));
//        assertFalse("No fixed item", summary.containsKey(ReportItem.Severity.IFIXEDITFORYOU));
//        assertFalse("No missing item", summary.containsKey(ReportItem.Severity.MISSING));
//        assertFalse("No unknown item", summary.containsKey(ReportItem.Severity.UNKNOWN));
//        assertTrue("Some warning items", summary.containsKey(ReportItem.Severity.WARNING));
//        assertTrue("Some critical items", summary.containsKey(ReportItem.Severity.CRITICAL));
//        assertTrue("Some note items", summary.containsKey(ReportItem.Severity.NOTE));
//        assertEquals("Two warnings", new Integer(2) , summary.get(ReportItem.Severity.WARNING));
//        assertEquals("Three critical errors", new Integer(3) , summary.get(ReportItem.Severity.CRITICAL));
//        assertEquals("Two notes", new Integer(2) , summary.get(ReportItem.Severity.NOTE));
//    }
}
