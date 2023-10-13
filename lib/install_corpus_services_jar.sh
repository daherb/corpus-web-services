#!/bin/sh
# Get the artifact for the most recent artifact from the main branch
curl -L -o corpus-services-ng-1.0.jar $(curl -L -H "Accept: application/vnd.github+json" -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/repos/daherb/corpus-services-ng/actions/artifacts | python -c "import json; import sys; print([a for a in json.load(sys.stdin)['artifacts'] if a['workflow_run']['head_branch'] == 'main'][0]['url'])")
mvn install:install-file -Dfile=corpus-services-ng-1.0.jar -DgroupId=de.uni_hamburg.corpora -DartifactId=corpus-services-ng -Dversion=1.0 -Dpackaging=jar
