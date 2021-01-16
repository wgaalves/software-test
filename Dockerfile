FROM oracle/graalvm-ce:20.2.0-java11

RUN yum update -y && yum install -y \
        wget \
        htop \
        curl \
        tar \
        vim \
        tzdata \
        && yum clean all

RUN ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime


VOLUME /tmp

ENV APP_NAME test.jar

COPY ["build/libs/test.jar",  "/application/"]

RUN bash -c 'touch /application/$APP_NAME'

WORKDIR  /application

EXPOSE 8080

# Start tomcat
ENTRYPOINT exec java -jar /application/$APP_NAME