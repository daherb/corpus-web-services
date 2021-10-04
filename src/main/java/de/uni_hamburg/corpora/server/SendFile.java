package de.uni_hamburg.corpora.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Base64;

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
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    /**
     * Accepts files sent via post and data-url-encoded. Only for local use because we don't avoid potential
     * conflicts with parallel uploads!
     *
     * @return Response containing the file or an error code
     */
    @POST
    public Response getFile(String fileData) {
        // New folder in temp
        String corpusDirStr = System.getProperty("java.io.tmpdir") + "/corpus-files";
        File corpusDir = new File(corpusDirStr);
        // Create if folder is missing
        if (!corpusDir.exists())
            corpusDir.mkdirs();
        try {
            // Parse json
            CorpusFile cf = new ObjectMapper().readerFor(CorpusFile.class).readValue(fileData);
            //logger.info("Got: " + cf);
            // Create a new file in temp folder and write the data we got
            File file = new File(new URI(corpusDir.toURI() + "/" + cf.getName())) ;
            // Delete file if it already exists
            if (file.exists())
                file.delete();
            if (!file.createNewFile()) throw new IOException("Cannot create file");
            logger.info(String.valueOf(file.toURI()));
            FileOutputStream fs = new FileOutputStream(file);
            fs.write(cf.getData());
            fs.close();
            // Everything okay
            return Response.ok().build();
        } catch (IOException | URISyntaxException e) {
            // On exception print error and return error code
            logger.error("Error reading " + fileData + ": " + e);
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),e.toString()).build();
        }
    }
}
