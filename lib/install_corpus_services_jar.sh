#!/bin/sh
curl -L -o corpus-services-1.0.jar https://gitlab.rrz.uni-hamburg.de/corpus-services/corpus-services/-/jobs/artifacts/develop-quest/raw/target/corpus-services-1.0.jar?job=compile_withmaven
mvn install:install-file -Dfile=corpus-services-1.0.jar -DgroupId=de.uni_hamburg.corpora -DartifactId=corpus-services -Dversion=1.0 -Dpackaging=jar
