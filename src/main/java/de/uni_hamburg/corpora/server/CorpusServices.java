package de.uni_hamburg.corpora.server;

import de.uni_hamburg.corpora.CorpusData;
import de.uni_hamburg.corpora.CorpusFunction;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CorpusServices {
    public static Set<String> getCorpusFunctions() {
        // Get all classes implementing the interface via reflections
        Reflections reflections = new Reflections("de.uni_hamburg.corpora");
        Set<Class<? extends CorpusFunction>> classes = reflections.getSubTypesOf(CorpusFunction.class);
        // Convert classes to class names
        Set<String> classNames = classes.stream().filter((c) -> !Modifier.isAbstract(c.getModifiers())).map(Class::getCanonicalName).collect(Collectors.toSet());
        return classNames ;
    }

    public static Set<String> getCorpusTypes () {
        Reflections reflections = new Reflections("de.uni_hamburg.corpora");
        HashSet<String> classNames = new HashSet<>();
        Set<Class<? extends CorpusData>> classes = reflections.getSubTypesOf(CorpusData.class);
        for (Class c : classes) {
            classNames.add(c.getCanonicalName());
        }
        return classNames ;
    }
}
