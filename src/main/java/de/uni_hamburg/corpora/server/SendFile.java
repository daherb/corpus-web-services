package de.uni_hamburg.corpora.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20211004
 * Class handling files sent to the server
 */
@Path("/send")
public class SendFile {
    public static class CorpusFile {

        String name;
        String type;
        byte[] data;

        @JsonCreator
        public CorpusFile(
                @JsonProperty("name") String name,
                @JsonProperty("data") String data) throws IOException {
            this.name = name;
            String[] parts = data.split(";");
            if (parts.length == 1)
                throw new IOException("Invalid data") ;
            this.type = parts[0].substring(5);
            this.data = Base64.getDecoder().decode(parts[1].substring(7));
        }

        public String toString() {
            return "name:" + this.name + ", type:" + this.type + ", data:" + new String(this.data);
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public byte[] getData() {
            return data;
        }
    }
//    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final Logger logger = Logger.getLogger(SendFile.class.getName());
    
    /**
     * Accepts files sent via post and data-url-encoded. Only for local use because we don't avoid potential
     * conflicts with parallel uploads!
     *
     * @return Response containing the file or an error code
     */
    @POST
    public Response getFile(String fileData) {
        try {
            // New folder in temp
            File corpusDir;
            if (System.getProperty("corpusDir") == null) {
                corpusDir = Files.createTempDirectory("corpus-files").toFile();
                System.setProperty("corpusDir", corpusDir.toString());
            }
            else {
                corpusDir = new File(System.getProperty("corpusDir"));
            }

            // Create if folder is missing
//            if (!corpusDir.exists()) {
//                if (!corpusDir.mkdirs()) {
//                    throw new IOException("Failed to create directory " + corpusDir);
//                }
//            }
            // Parse json
            CorpusFile cf = new ObjectMapper().readerFor(CorpusFile.class).readValue(fileData);
            //logger.info("Got: " + cf);
            // Create a new file in temp folder and write the data we got
            File file = Paths.get(corpusDir.getPath(),cf.getName()).toFile();
            logger.info("FILE: " + file);
            // Delete file if it already exists
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted)
                    throw new IOException("Failed to delete file " + file);
            }
            if (!file.createNewFile()) throw new IOException("Failed to create file " + file);
            FileOutputStream fs = new FileOutputStream(file);
            fs.write(cf.getData());
            fs.close();
//            logger.info("Wrote file " + file + ": " + file.exists());
//            logger.info(Arrays.asList(corpusDir.listFiles()).toString());
            // Everything okay
            return Response.ok().build();
        } catch (IOException e) {
            // On exception print error and return error code
//            logger.error("Error reading " + fileData + ": " + e);
            logger.severe("Error reading " + fileData + ": " + e);
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),e.toString()).build();
        }
    }
}
