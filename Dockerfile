FROM alpine:3.15

RUN apk update && \
    apk add openjdk11 && \
    mkdir /application/

WORKDIR /application/

COPY build/libs/ .

CMD ["java", "-jar", "kastro.dev.users-api-0.0.1-all.jar"]