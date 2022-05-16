EXPOSE 9527
EXPOSE 8443
EXPOSE 80
EXPOSE 443
RUN docker pull vanyouseea/o365:dev_https
RUN docker run -d -p 443:8443 vanyouseea/o365:dev_https
