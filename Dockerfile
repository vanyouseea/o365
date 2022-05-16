# syntax=docker/dockerfile:1
FROM ubuntu:18.04
FROM java:8 
EXPOSE 9527
EXPOSE 8443
EXPOSE 80
EXPOSE 443
RUN apt update & apt install mvn
RUN mvn clean package
RUN wget https://github.com/vanyouseea/o365/releases/download/v.1.7.2/o365-1.7.2.jar
RUN java -jar o365-1.7.2.jar
