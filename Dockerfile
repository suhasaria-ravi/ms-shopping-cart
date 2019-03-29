FROM java:8-jre

VOLUME [ "/data" ]

WORKDIR /data

EXPOSE 8080
ENTRYPOINT [ "java" ]
CMD ["-?"]