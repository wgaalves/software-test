FROM graalvm/java11:latest

VOLUME /tmp

ENV APP_NAME test.jar

COPY ["build/libs/test.jar",  "/application/"]

RUN bash -c 'touch /application/$APP_NAME'

WORKDIR  /application

EXPOSE 8080

# Start tomcat
ENTRYPOINT exec java -jar /application/$APP_NAME