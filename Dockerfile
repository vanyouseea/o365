# syntax=docker/dockerfile:1
FROM ubuntu:18.04
FROM java:8 
RUN mvn clean package
RUN cd ./target 
ADD o365-1.7.2.jar o365-1.7.2.jar
EXPOSE 9527
EXPOSE 8443
RUN java -jar o365-1.7.2.jar
