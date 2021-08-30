FROM java:8 
ADD o365-1.6.1.jar o365-1.6.1.jar
EXPOSE 9527
ENTRYPOINT ["java","-jar","o365-1.6.1.jar"]
