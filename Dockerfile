FROM ubuntu:18.04
FROM java:8 
RUN \
    echo ">>>>>> get o365 from github <<<<<<" \
    && wget -O o365-1.7.3s.jar http://od.csb2.org/om/o365/o365-1.7.3s.jar

VOLUME ["/app"]

EXPOSE 8443

ENTRYPOINT java -Xms256m -Xmx512m -jar o365-1.7.3s.jar
