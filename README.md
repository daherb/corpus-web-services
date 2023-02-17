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