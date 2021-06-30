package de.uni_hamburg.corpora.server;

import de.uni_hamburg.corpora.CorpusFunction;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/list_corpus_functions")
public class ListCorpusFunctions {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listFunctions() {
        StringBuilder classNames = new StringBuilder() ;
        classNames.append("<html>\n");
        classNames.append("<body>\n");
        classNames.append("<table>\n");
        for (String s : CorpusServices.getCorpusFunctions()) {
            String function, description ;
            try {
                CorpusFunction cf = (CorpusFunction) Class.forName(s).getDeclaredConstructor().newInstance();
                function = cf.getFunction() ;
                try {
                    description = cf.getDescription();
                }
                catch (Exception e) {
                    description = "Error reading description" ;
                    logger.error("WTF",e);
                }
                //logger.info(cf.getDescription()) ;
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                function = s ;
                description = "Class not found";
            }

            classNames.append("<tr>\n");
            classNames.append("<td>");
            classNames.append(function) ;
            classNames.append("</td><td>");
            classNames.append(description);
            classNames.append("</td>\n");
            //classNames.append("<td>"+s+"</td>");
            classNames.append("</tr>\n");
        }
        classNames.append("</table>\n");
        classNames.append("</body>\n");
        classNames.append("</html>\n");
        return classNames.toString() ;
    }
}
