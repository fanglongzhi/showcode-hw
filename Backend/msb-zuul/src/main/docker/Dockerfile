FROM @docker.image.prefix@/java:8
VOLUME /tmp
ADD msb-zuul-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Xmx512m","-Xms256m","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--CONFIG_URL=http://msb-config:8888","--spring.profiles.active=test"]
