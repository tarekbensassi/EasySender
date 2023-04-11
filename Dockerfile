
FROM openjdk:11-jdk-alpine
COPY target/*.jar app.jar
ENV PORT 9024
EXPOSE $PORT
ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","app.jar"]



