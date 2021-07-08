#!/bin/sh
mvn install:install-file -Dfile=corpus-services-1.0.jar -DgroupId=de.uni_hamburg.corpora -DartifactId=corpus-services -Dversion=1.0 -Dpackaging=jar
