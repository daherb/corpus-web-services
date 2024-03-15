#!/bin/sh
if [[ ! -e elan-6.2.jar ]] ; then curl -o - https://www.mpi.nl/tools/elan/ELAN_6-2_linux.tar.gz | tar xzv ELAN_6-2/opt/elan-6.2/lib/app/elan-6.2.jar --transform='s#ELAN_6-2/opt/elan-6.2/lib/app/##'; fi
../mvnw install:install-file -Dfile=elan-6.2.jar -DgroupId=nl.mpi -DartifactId=ELAN -Dversion=6.2 -Dpackaging=jar  -DgeneratePom=true
if [[ ! -e EXMARaLDA.jar ]]; then curl -o - https://www.exmaralda.org/files/officialDL/EXMARaLDA_linux.tar.gz | tar xzv exmaralda1.13/lib/EXMARaLDA.jar  --transform='s#exmaralda1.13/lib/##'; fi
# Remove problematic file
zip -d EXMARaLDA.jar TestSound.class
../mvnw install:install-file -Dfile=EXMARaLDA.jar -DgroupId=org.exmaralda -DartifactId=EXMARaLDA -Dversion=1.0 -Dpackaging=jar  -DgeneratePom=true
if [[ ! -e xml-magic ]]; then git clone https://github.com/daherb/xml-magic; fi
cd xml-magic && git pull && bash install-pom.sh
cd ..
if [[ ! -e invenio-java-api ]]; then git clone https://github.com/daherb/invenio-java-api; fi
cd invenio-java-api && git pull && bash install-pom.sh
cd ..
if [[ ! -e datacite-java-api ]]; then git clone --recursive --remote-submodules https://github.com/daherb/datacite-java-api; fi
cd datacite-java-api && git pull && bash install-pom.sh
cd ..
if [[ ! -e xml-magic ]]; then git clone --recursive --remote-submodules https://github.com/daherb/xml-magic; fi
cd xml-magic && git pull && bash install-pom.sh
cd ..
# Get the artifact for the most recent artifact from the main branch
if [[ -e corpus-services.zip ]] ; then rm corpus-services.zip; fi
if [[ -e  corpus-services-ng-1.0.jar ]] ; then rm corpus-services-ng-1.0.jar; fi
wget https://github.com/daherb/corpus-services-ng/releases/latest/download/corpus-services.zip
unzip corpus-services.zip
../mvnw install:install-file -Dfile=corpus-services-ng-1.0.jar -DgroupId=de.uni_hamburg.corpora -DartifactId=corpus-services-ng -Dversion=1.0 -Dpackaging=jar
