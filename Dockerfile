FROM ubuntu:18.04
FROM java:8 
RUN \
    echo ">>>>>> get o365 from github <<<<<<" \
    && wget -O o365-1.7.2.jar https://github.com/vanyouseea/o365/releases/download/v.1.7.2/o365-1.7.2.jar

VOLUME ["/app"]

EXPOSE 80
EXPOSE 9527
EXPOSE 443
EXPOSE 8443

ENTRYPOINT java -Xms256m -Xmx512m -jar -Dserver.port=80 o365-1.7.2.jar
