# syntax=docker/dockerfile:1
FROM ubuntu:18.04
FROM java:8 
RUN wget https://github.com/vanyouseea/o365/releases/download/v.1.7.2/o365-1.7.2.jar
ADD o365-1.7.2.jar o365-1.7.2.jar
EXPOSE 9527
EXPOSE 8443
ENTRYPOINT ["java","-jar","o365-1.7.2.jar"]
