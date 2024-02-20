#!/usr/bin/env bash

echo -e "\n*** setting up SonarQube locally for flashcards project ***\n"
docker-compose -f sonar/docker-compose.yaml up -d

# Wait for SonarQube to be up and running
echo -e "\n*** waiting for SonarQube to start ***\n"
until curl --silent http://localhost:9000/api/system/status | grep "UP" ; do
    printf '.'
    sleep 5 # wait 5 seconds before check again
done

echo -e "\n*** SonarQube is up. Creating flashcards-client project via WebAPI ***\n"
echo "Enter SonarQube Admin password: " && read password
curl -X POST "http://localhost:9000/api/projects/create?name=Flashcards%20Client&project=flashcards-client" -u "admin:$password"
echo
response=$(curl -s -X POST -u "admin:$password" "http://localhost:9000/api/user_tokens/generate?name=flashcardsUserToken")
token=$(echo $response | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo -e "\nGenerated user token:\nsonarToken=$token"
echo "**** copy above line (sonarToken=...) to the gradle.properties file in the root of the project"
echo "it will be used by the sonarqube gradle plugin to upload the results of the analysis to the server"
echo -e "It is on .gitignore so it will not be committed to the repository.\n"

echo -e "Now go to http://localhost:9000/projects/flashcards-client and have fun!\n"
