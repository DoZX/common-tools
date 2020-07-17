FROM centos:7
LABEL maintainer="dhh"

COPY rootfs /

RUN yum -y install wget \
    crontabs \
    make zlib zlib-devel gcc-c++ libtool  openssl openssl-devel \
    epel-release && \
    yum -y install python-pip

ENTRYPOINT /www/bin/start