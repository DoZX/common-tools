#!/bin/bash

log_file=/www/bin/start.log

log() {
    if [ ! -f $log_file ]; then
        touch $log_file
    fi
    if [ "X$1" = "XERROR" ];then
        echo -e "\033[31m`date "+%Y-%m-%d %H:%M:%S"` [$1] - $2\033[0m"|tee -a $log_file

    elif [ "X$1" = "XGREEN" ];then
        echo -e "\033[32m`date "+%Y-%m-%d %H:%M:%S"` [$1] - $2\033[0m"|tee -a $log_file

    elif [ "X$1" = "XWARN" ];then
        echo -e "\033[33m`date "+%Y-%m-%d %H:%M:%S"` [$1] - $2\033[0m"|tee -a $log_file

    else
        echo "`date "+%Y-%m-%d %H:%M:%S"` [$1] - $2"|tee -a $log_file
    fi
}
