package de.uni_hamburg.corpora.server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author bba1792 Dr. Herbert Lange
 * @version 20210701
 * The Exception handler logging exceptions.
 */
@ControllerAdvice
public class ExceptionLogger {

    private static final Logger log = Logger.getLogger("ExceptionLog");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> toResponse(Exception e) {
        //log.severe("Exception:" + e);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        log.severe("Exception:" + e + "\n" + sw);
        return new ResponseEntity<>(sw.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
