FROM openjdk:14
VOLUME /tmp
ADD build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar
EXPOSE 8069
ENTRYPOINT ["java","-jar","gateway.jar"]
