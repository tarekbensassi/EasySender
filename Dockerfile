FROM maven:3-jdk-8-alpine AS build
# Copy folder in docker
WORKDIR /var/lib/jenkins/workspace/easy/
COPY ./ /var/lib/jenkins/workspace/easy/
RUN mvn clean install -DskipTests
# Run spring boot in Docker
FROM openjdk:11-jdk-alpine
COPY --from=build /var/lib/jenkins/workspace/easy/target/*.jar app.jar
ENV PORT 9024
EXPOSE $PORT
ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","app.jar"]