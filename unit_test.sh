#!/bin/bash
file=./unit_test_config.txt

## without array
while  IFS="|" read -r gradle_exec nefario_path
do
    echo "./gradlew $gradle_exec:jacocoTestReport"
    nefario st $nefario_path
   ./gradlew $gradle_exec:jacocoTestReport &
done <"$file"
wait

##publish to sonarqube
./gradlew -Dsonar.host.url=http://10.164.8.12:8080  sonarqube --stacktrace &
wait