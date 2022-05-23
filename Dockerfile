#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src ./src
COPY pom.xml ./
RUN mvn -f ./pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build ./target/*.jar /usr/local/lib/1.jar

EXPOSE 9527 8443

ENTRYPOINT ["java","-jar","/usr/local/lib/1.jar"]
