package de.uni_hamburg.corpora.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * Resource to list corpus data types defined in the corpus services
 */
@RestController
public class ListCorpusTypes {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@GetMapping(value = "list_corpus_types", produces = "text/plain")
    public String listCorpusTypes() {
        StringBuilder classNames = new StringBuilder() ;
        for (String cn : CorpusServices.getCorpusTypes()) {
            classNames.append(cn);
            classNames.append("\n");
        }
        return classNames.toString();
    }
}
