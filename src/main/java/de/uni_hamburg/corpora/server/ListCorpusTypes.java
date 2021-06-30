package de.uni_hamburg.corpora.server;

import de.uni_hamburg.corpora.CorpusData;
import org.reflections.Reflections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210630
 * Resource to list corpus data types defined in the corpus services
 */
@Path("/list_corpus_types")
public class ListCorpusTypes {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        StringBuilder classNames = new StringBuilder() ;
        for (String cn : CorpusServices.getCorpusTypes()) {
            classNames.append(cn + "\n");
        }
        return classNames.toString();
    }
}
