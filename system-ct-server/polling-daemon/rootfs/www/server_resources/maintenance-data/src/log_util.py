#!/usr/bin/python
# -*- coding: utf-8 -*-

import logging
from logging.handlers import RotatingFileHandler
from logging.handlers import TimedRotatingFileHandler
from threading import Lock

ERRLOG_SIZE = 10*1024*1024
BACKUP_COUNT = 5

def __singletion(cls):
    instances = {}
    def getInstance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return getInstance

class LogUtil(object):
    def __init__(self, loginfo=None, logerror=None, prefix=None):
        if loginfo is None:
            self.loginfo = '../logs/log.info'
        else:
            self.loginfo = loginfo
        if logerror is None:
            self.logerror = '../logs/log.error'
        else:
            self.logerror = logerror

        if prefix is None:
            self.prefix = ''
        else:
            self.prefix = prefix
        self.mutex = Lock()
        self.formatter = '%(asctime)s, %(filename)s,[line]:%(lineno)d,%(levelname)s,%(message)s'

    def _create_logger(self):
        _logger = logging.getLogger("INFO" + self.prefix)
        _logger.setLevel(level=logging.INFO)
        _logger2 = logging.getLogger("ERROR" + self.prefix)
        _logger2.setLevel(level=logging.INFO)
        return _logger, _logger2

    def _file_logger_error(self):
        size_rotate_file = RotatingFileHandler(filename=self.logerror, maxBytes=ERRLOG_SIZE, backupCount=BACKUP_COUNT)
        size_rotate_file.setFormatter(logging.Formatter(self.formatter))
        size_rotate_file.setLevel(logging.INFO)
        return size_rotate_file

    def _file_logger_info(self):
        time_rotate_file = TimedRotatingFileHandler(filename=self.loginfo, when='D', interval=1, backupCount=BACKUP_COUNT)
        time_rotate_file.setFormatter(logging.Formatter(self.formatter))
        time_rotate_file.setLevel(logging.INFO)
        return time_rotate_file

    def _console_logger(self):
        console_handler = logging.StreamHandler()
        console_handler.setLevel(level=logging.INFO)
        console_handler.setFormatter(logging.Formatter(self.formatter))
        return console_handler

    def pub_logger(self):
        logger_info,logger_error = self._create_logger()
        self.mutex.acquire()
        if not logger_info.handlers:
            logger_info.addHandler(self._file_logger_info())
            logger_error.addHandler(self._file_logger_error())
        logger_info.propagate = False
        logger_error.propagate = False
        self.mutex.release()
        return logger_info, logger_error