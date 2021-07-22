FROM java:8 
ADD o365-1.5.4.jar o365-1.5.4.jar
EXPOSE 9527
ENTRYPOINT ["java","-jar","o365-1.5.4.jar"]
