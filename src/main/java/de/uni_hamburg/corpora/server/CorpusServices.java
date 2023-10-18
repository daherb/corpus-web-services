package de.uni_hamburg.corpora.server;

import de.uni_hamburg.corpora.CorpusData;
import de.uni_hamburg.corpora.CorpusFunction;
import de.uni_hamburg.corpora.CorpusMagician;
import de.uni_hamburg.corpora.Report;
import de.uni_hamburg.corpora.ReportItem;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20220404
 * Class encapsulating some corpus services functionality
 */
public class CorpusServices {
    /**
     * Gets the names of all corpus functions, ie all classes implementing CorpusFunction, defined in de.uni_hamburg.corpora.
     *
     * @return the list of corpus functions
     */
    public static Set<String> getCorpusFunctions() {
        // Get all classes implementing the interface via reflections
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(CorpusMagician.corpusFunctionPackages));
        Set<Class<? extends CorpusFunction>> classes = reflections.getSubTypesOf(CorpusFunction.class);
        // Convert classes to class names
        return classes.stream()
                .filter((c) -> Modifier.isPublic(c.getModifiers()) && !Modifier.isAbstract(c.getModifiers()))
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the class names of all corpus types, ie all classes derived from CorpusData, defined in de.uni_hamburg.corpora.
     *
     * @return the list of corpus types
     */
    public static Set<String> getCorpusTypes () {
        Reflections reflections = new Reflections("de.uni_hamburg.corpora");
        HashSet<String> classNames = new HashSet<>();
        Set<Class<? extends CorpusData>> classes = reflections.getSubTypesOf(CorpusData.class);
        for (Class<? extends CorpusData> c : classes) {
            classNames.add(c.getCanonicalName());
        }
        return classNames ;
    }

    /**
     * Generate the summary of a report returning the count of items for each severity level.
     *
     * @param report the report
     * @return the hash map
     */
    public static HashMap<ReportItem.Severity,Integer> generateSummary(Report report) {
        HashMap<ReportItem.Severity,Integer> results = new HashMap<>();
        for (ReportItem item : report.getRawStatistics()) {
            results.compute(item.getSeverity(),(k,v) -> (v==null) ? 1 : v + 1) ;
        }
        return results;
    }
}
