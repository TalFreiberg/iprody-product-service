FROM openjdk:21-jdk-slim

WORKDIR /projects/iprody/product-service

COPY target/*.jar product-service.jar

EXPOSE ${N_TLS_PORT} ${TLS_PORT} ${DEBUG_PORT}

ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} -jar product-service.jar
