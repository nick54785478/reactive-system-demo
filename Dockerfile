FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ENV TZ Asia/Taipei
COPY /target/reactive-system-demo-0.0.1-SNAPSHOT.jar auth-service.jar
ENTRYPOINT ["java","-jar","/auth-service.jar"]