package de.uni_hamburg.corpora.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import de.uni_hamburg.corpora.CorpusFunction;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210705
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response listFunctions() {
        ArrayList<Map<String,String>> functionList = new ArrayList<>() ;
        for (String s : CorpusServices.getCorpusFunctions()) {
            String function, description;
            boolean available ;
            try {
                CorpusFunction cf = (CorpusFunction) Class.forName(s).getDeclaredConstructor().newInstance();
                function = cf.getFunction();
                try {
                    description = cf.getDescription();
                    available = true ;
                } catch (Exception e) {
                    description = "Error reading description" ;
                    available = false ;
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                function = s ;
                description = "Error loading class" ;
                available = false ;
            }
            HashMap<String,String> functionInfo = new HashMap<>();
            functionInfo.put("name", function);
            functionInfo.put("description",description);
            functionInfo.put("available",Boolean.toString(available));

            functionList.add(functionInfo);
        }
        // Sort the list by name
        functionList.sort(Comparator.comparing((x) -> x.get("name")));
        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(functionList);
            return Response.ok().entity(json).build() ;
        }
        // On error return empty JSON and an error code
        catch (JsonProcessingException e) {
            return Response.status(400).entity(new JsonObject().toString()).build();
        }
    }
}
