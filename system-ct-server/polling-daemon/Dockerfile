FROM centos:7
LABEL maintainer="dhh"

COPY rootfs /

RUN yum -y install epel-release && \
    yum -y install wget python-pip git crontabs

ENTRYPOINT /www/bin/start