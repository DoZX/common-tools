#!/usr/bin/python
# -*- coding: utf-8 -*-

import time
import commands
import configparser
from log_util import *

logUtil = LogUtil()
loginfo, logerror = logUtil.pub_logger()
RET_OK=True
RET_ERR=False

def doBash(cmd):
    ret = RET_OK
    output = ''
    try:
        status, output = commands.getstatusoutput(cmd)
        loginfo.info("cmd:%s status:%s output:%s" % (cmd, status, output))
        if status != 0: ret = RET_ERR
    except Exception as e:
        logerror.info("cmd:%s do fail, exception:%s" % (cmd, str(e)))
        ret = RET_ERR
    finally:
        return ret, output

class GitUtil:
    def __init__(self, git_addr, branch, resource_path):
        self.git_addr = git_addr
        self.branch = branch
        self.resource_path = resource_path

    def clone(self):
        return doBash('git clone -b %s %s %s' %(self.git_addr, self.branch, self.resource_path))

    def rev(self):
        return doBash('cd %s && git rev-parse HEAD' %(self.resource_path))

    def pull(self):
        return doBash('cd %s && git pull origin %s' %(self.resource_path, self.branch))


class IniUtil:
    def __init__(self, file_path):
        self.file_path = file_path

    def get(self, section, key):
        ini_conf = configparser.ConfigParser()
        ini_conf_read = ini_conf.read(self.file_path)
        return ini_conf_read[section][key]

    def save(self, section, key, value):
        ini_conf = configparser.ConfigParser()
        ini_conf.set(section, key, value)
        ini_conf.write(open(self.file_path, 'a'))


class ResourceParser:
    def __init__(self, resource_path, db_server_host):
        self.resource_path = resource_path
        self.db_server_host = db_server_host
        self.db_server_instance = PushDatabase(self.db_server_host)

    def parser_description_yml(self, file_path):
        return {}

    def read_resource(self, file_path):
        return ''

    def parser_single_module(self, resource_path):
        pass

    def parser_all_module(self):
        for root, dirs, files in os.walk(self.resource_path):
            if 'description.yml' in files:
                description_dict = self.parser_description_yml(root + "/description.yml")
                description_dict['file_path'] = root
                for fileName in files:
                    if fileName == 'description.yml':
                        continue
                    description_dict['code_' + fileName] = self.read_resource(root + fileName)
                self.db_server_instance.push(description_dict)


class PushDatabase:
    def __init__(self, db_server_host):
        self.db_server_host = db_server_host
        self.init()

    def init(self):
        self.es_index_name = 'ct.' + str(time.strftime("%Y-%m-%d_%H:%M", time.localtime()))
        pass

    def push(self, data):
        pass


