package de.uni_hamburg.corpora.server;

import de.uni_hamburg.corpora.CorpusFunction;
import org.reflections.Reflections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("list_corpus_functions")
public class ListCorpusFunctions {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String listFunctions() {
        StringBuilder classNames = new StringBuilder() ;
        for (String s : CorpusService.getCorpusFunctions()) {
            classNames.append(s+"\n");
        }
        return classNames.toString() ;
    }
}
