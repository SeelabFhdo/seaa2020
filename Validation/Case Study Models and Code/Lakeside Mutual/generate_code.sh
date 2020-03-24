#!/usr/bin/env bash

VALIDATION_NAME="Lakeside Mutual"
services=(
    city
    customer
    identityAccess
    interactionLog
    policy
)

generate_code() {
    SERVICE=$1    
    eval "java -jar libs/de.fhdo.lemma.model_processing.code_generation.java_base-0.0.1-SNAPSHOT-standalone.jar -s \"$VALIDATION_NAME/Phase 4/$SERVICE.mapping\" -i \"$VALIDATION_NAME/intermediate/mapping models/$SERVICE.xmi\" -t \"$VALIDATION_NAME/generated code\" code_generation main --code_generation_serializer=$PATTERN \"--genlet=libs/de.fhdo.lemma.model_processing.code_generation.springcloud-0.0.1-SNAPSHOT.jar\""
}

compile() {
    SERVICE="$(echo "$1" | sed 's/.*/\l&/')"
    mvn -f "generated code/org/example/$SERVICE/pom.xml" clean package
}

run() {
    SERVICE="$(echo "$1" | sed 's/.*/\l&/')"
    if [ "$SHOW_RUN_INFORMATION" == false ]
    then
        nohup mvn -f "generated code/org/example/$SERVICE/pom.xml" spring-boot:run & echo $! > $SERVICE.pid
    else
        mvn -f "generated code/org/example/$SERVICE/pom.xml" spring-boot:run &
    fi
}

# You need at least Java 12 in order to run the code generator's JAR and compile the generated source code with Maven
source export_java_home.sh
bash export_java_home.sh

mvn install:install-file -Dfile=../libs/de.fhdo.lemma.msa-0.0.1-SNAPSHOT.jar -DgroupId=de.fhdo.lemma.msa -DartifactId=de.fhdo.lemma.msa -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

PATTERN="plain"
if [ "$1" == "generation-gap" ] || [ "$1" == "extended-generation-gap" ]
then
    PATTERN=$1
fi

COMPILE_ONLY=false
if [ "$1" == "compile_only" ] || [ "$2" == "compile_only" ] || [ "$3" == "compile_only" ]
then
    COMPILE_ONLY=true
fi

SHOW_RUN_INFORMATION=false
if [ "$1" == "show_run_information" ] || [ "$2" == "show_run_information" ] || [ "$3" == "show_run_information" ]
then
    SHOW_RUN_INFORMATION=true
fi

for service in "${services[@]}"; do
    cd ..
    generate_code $service
    cd "$VALIDATION_NAME"
    compile $service

    if [ "$COMPILE_ONLY" == false ]
    then
        run $service
    fi
done

if [ "$COMPILE_ONLY" == true ]
then
    exit 0
else
    sleep 60s
    SERVICE_PIDS=`cat *.pid | tr '\r\n' ' '`
    echo "Killing service processes again. PIDs: $SERVICE_PIDS"
    kill -9 $SERVICE_PIDS
    rm *.pid
fi
