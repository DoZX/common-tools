FROM centos:7
LABEL maintainer="dhh"

COPY rootfs /

RUN yum -y install epel-release && \
    yum -y install wget python-pip git crontabs \
        make zlib zlib-devel gcc-c++ libtool  openssl openssl-devel

ENTRYPOINT /www/bin/start