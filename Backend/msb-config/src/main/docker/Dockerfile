FROM @docker.image.prefix@/java:8
VOLUME /tmp
ADD msb-config-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8888
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=test"]
