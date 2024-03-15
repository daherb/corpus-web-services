# Corpus-Web-Services

Java-based application to make calls to [corpus-services-ng](https://github.com/daherb/corpus-services-ng)

## Requirements 

Currently tested with
- Java 11
- Maven 3.8
- Linux

## Build and run
- cd lib
- bash install_corpus_services_jar.sh
- cd ..
- mvn clean compile exec:java

After these steps you can access a graphical web frontend by pointing your
webbrowser to http://localhost:8080/

## Available endpoints

- `/`

  The web-frontend that can be used from the browser
- `/list_corpus_functions`

  List all corpus functions with description and accepted parameters as JSON
- `/list_corpus_types`

  List all known corpus files as plain text
- ´/send'

  Sends a file to the server to be checked
  
- `/check_corpus`

  Run a set of corpus functions on a set of files
- `/report`

  Get the report for a run of corpus services
