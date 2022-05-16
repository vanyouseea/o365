# syntax=docker/dockerfile:1
FROM ubuntu:18.04
FROM java:8 
WORKDIR /app
RUN wget https://github.com/vanyouseea/o365/releases/download/v.1.7.2/o365-1.7.2.jar
RUN java -jar /app/o365-1.7.2.jar -Dserver.port=80
