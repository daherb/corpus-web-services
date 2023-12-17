package de.uni_hamburg.corpora.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.JsonObject;
import de.uni_hamburg.corpora.CorpusFunction;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20220405
 * Resource to list corpus functions defined in the corpus services
 * Scope: any
 */
@Path("list_corpus_functions")
public class ListCorpusFunctions {
    /**
     * Class representing the relevant information for a corpus function, used to (de)serialize JSON
     */
    public static class CorpusFunctionInfo {
        String name;
        String description;
        boolean available ;
        Set<String> usableFor;
        Map<String,String> params;

        public CorpusFunctionInfo(String name, String description, boolean available, Set<String> usableFor,
                                  Map<String,String> params) {
            this.name = name;
            this.description = description;
            this.available = available;
            this.usableFor = usableFor;
            this.params = params ;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public boolean isAvailable() {
            return available;
        }

        public Set<String> getUsableFor() {
            return usableFor;
        }

        public String getUsableForString() { return String.join(", ", usableFor); }

        public Map<String,String> getParams() { return params; }

        public String getParamsString() {
            return params.keySet().stream().map((k) -> k + ": " + params.get(k))
                    .collect(Collectors.joining("<br>\n"));
        }
    }
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON response containing the list of corpus functions or HTTP error code 400
     */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCorpusFunctions() {
//        // Convert to JSON
//        ArrayList<CorpusFunctionInfo> functionList = listFunctions();
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String json = mapper.writeValueAsString(functionList);
//            return Response.ok().entity(json).build() ;
//        }
//        // On error return empty JSON and an error code
//        catch (JsonProcessingException e) {
//            return Response.status(400).entity(new JsonObject().toString()).build();
//        }
//    }


    /**
     * Gets the list of all corpus functions available in corpus services
     *
     * @return list of CorpusFunctionInfo objects representing the functions
     */
    public ArrayList<CorpusFunctionInfo> listFunctions() {

        ArrayList<CorpusFunctionInfo> functionList = new ArrayList<>();
        for (String s : CorpusServices.getCorpusFunctions()) {
            String function, description;
            Set<String> usableClasses = new HashSet<>();
            Map<String,String> params = new HashMap<>();
            boolean available;
            try {
                // Create the corpus function object using reflections and get its information
                CorpusFunction cf =
                        (CorpusFunction) Class.forName(s).getDeclaredConstructor(Properties.class).newInstance(new Properties());
                function = cf.getFunction();
                usableClasses.addAll(cf.getIsUsableFor().stream().map(Class::getSimpleName).collect(Collectors.toSet()));
                params.putAll(cf.getParameters());
                try {
                    description = cf.getDescription();
                    available = true;
                } catch (Exception e) {
                    description = "Error reading description";
                    available = false;
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                function = s;
                description = "Error loading class";
                available = false;
            }
            // Create new info object and add it to the list
            CorpusFunctionInfo functionInfo = new CorpusFunctionInfo(function, description, available, usableClasses,
                    params);
            functionList.add(functionInfo);
        }
        // Sort the list by name
        functionList.sort(Comparator.comparing(CorpusFunctionInfo::getName));
        return functionList;
    }
}
