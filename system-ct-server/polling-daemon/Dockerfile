FROM centos:7
LABEL maintainer="dhh"

COPY rootfs /

RUN yum -y install wget vixie-cron crontabs make zlib zlib-devel gcc-c++ libtool  openssl openssl-devel git

ENTRYPOINT /www/bin/start