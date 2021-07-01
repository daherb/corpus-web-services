package de.uni_hamburg.corpora.server;

import de.uni_hamburg.corpora.CorpusFunction;
import org.jdom.*;
import org.jdom.output.XMLOutputter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * Resource to list corpus functions defined in the corpus services
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
    @Produces(MediaType.TEXT_HTML)
    public String listFunctions() {
        Element htmlTable = new Element("table");
        for (String s : CorpusServices.getCorpusFunctions()) {
            Content function, description;
            try {
                CorpusFunction cf = (CorpusFunction) Class.forName(s).getDeclaredConstructor().newInstance();
                function = new Text(cf.getFunction());
                try {
                    description = new Text(cf.getDescription());
                } catch (Exception e) {
                    description = new Element("span")
                            .addContent(new Text("Error reading description"))
                            .setAttribute("style","color:red");
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                function = new Text(s);
                description = new Element("span")
                        .addContent(new Text("Error loading class"))
                        .setAttribute("style","color:red");
            }
            Element htmlRow = new Element("tr");
            htmlRow.addContent(new Element("td").addContent(function));
            htmlRow.addContent(new Element("td").addContent(description));
            htmlTable.addContent(htmlRow);

        }
        Document html = new Document(
                new Element("html")
                        .addContent(new Element("body")
                        .addContent(htmlTable)),
                new DocType("html"));
        return new XMLOutputter().outputString(html);
    }
}
