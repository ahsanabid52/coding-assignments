FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine

LABEL maintainer="Ahsan Abid"

RUN mkdir /application
WORKDIR /application
COPY /customers.csv /application
COPY /strategies.csv /application


COPY build/libs/*.jar ./application.jar

EXPOSE 8080

ARG JAVA_ADDITIONAL_OPTS
ENV JAVA_ADDITIONAL_OPTS=$JAVA_ADDITIONAL_OPTS
ENTRYPOINT java $JAVA_ADDITIONAL_OPTS \
                -Djava.security.egd=file:/dev/./urandom \
                -jar application.jar