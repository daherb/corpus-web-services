#!/bin/sh
# Get the artifact for the most recent artifact from the main branch
if [[ -e corpus-services.zip ]] ; then rm corpus-services.zip; fi
if [[ -e  corpus-services-ng-1.0.jar ]] ; then rm corpus-services-ng-1.0.jar; fi
wget https://github.com/daherb/corpus-services-ng/releases/latest/download/corpus-services.zip
unzip corpus-services.zip
../mvnw install:install-file -Dfile=corpus-services-ng-1.0.jar -DgroupId=de.uni_hamburg.corpora -DartifactId=corpus-services-ng -Dversion=1.0 -Dpackaging=jar
