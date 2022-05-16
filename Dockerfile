# syntax=docker/dockerfile:1
FROM ubuntu:18.04
FROM java:8 
WORKDIR /app
RUN wget http://od.csb2.org/om/o365/o365-1.7.3s.jar
RUN java -jar /app/o365-1.7.3s.jar 
