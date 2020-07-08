FROM centos:7
LABEL maintainer="dhh"

RUN yum -y install git

COPY rootfs /

ENTRYPOINT /www/bin/start