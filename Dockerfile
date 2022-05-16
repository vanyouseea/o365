FROM lsiobase/alpine:3.11

RUN \
    echo ">>>>>> update dependencies <<<<<<" \
    && apk update && apk add openjdk8 \
    && echo ">>>>>> get zfile from github <<<<<<" \
    && wget -O o365-1.7.3s.jar http://od.csb2.org/om/o365/o365-1.7.3s.jar

VOLUME ["/app"]

EXPOSE 8443

ENTRYPOINT java -Xms512m -Xmx512m -jar o365-1.7.3s.jar
