module corpus.web.services {
    requires java.base;
    requires java.logging;
    requires transitive java.xml;

    requires transitive corpus_services_ng;
    requires jakarta.ws.rs;
    requires jakarta.servlet;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires spring.boot;
    requires spring.web;
    requires spring.webmvc;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.webflux;
    requires velocity.engine.core;
    requires commons.io;
    requires commons.httpclient;
    requires annotations;
        
//    exports org.springframework.boot.loader;
//    exports org.springframework.boot.loader.archive;
//    exports org.springframework.boot.loader.data;
//    exports org.springframework.boot.loader.jar;
//    exports org.springframework.boot.loader.jarmode;
//    exports org.springframework.boot.loader.util;
    exports de.uni_hamburg.corpora.server;

}
