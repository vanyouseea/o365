# syntax=docker/dockerfile:1
FROM ubuntu:18.04
FROM java:8 
EXPOSE 9527
EXPOSE 8443
RUN mvn clean package
RUN cd ./target 
RUN java -jar ./target/o365-1.7.2.jar
