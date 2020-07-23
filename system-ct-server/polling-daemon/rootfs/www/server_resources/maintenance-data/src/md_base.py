#!/usr/bin/python
# -*- coding: utf-8 -*-

import os
import time
import commands
import configparser
import yaml
import json
import requests
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
        return doBash('git clone -b %s %s %s' %(self.branch, self.git_addr, self.resource_path))

    def rev(self):
        return doBash('cd %s && git rev-parse HEAD' %(self.resource_path))

    def pull(self):
        return doBash('cd %s && git pull origin %s' %(self.resource_path, self.branch))


class IniUtil:
    def __init__(self, file_path):
        self.file_path = file_path

    def get(self, section, key):
        ini_conf = configparser.ConfigParser()
        ini_conf.read(self.file_path)
        return ini_conf.get(section, key)

    def save(self, section, key, value):
        ini_conf = configparser.ConfigParser()
        ini_conf.read(self.file_path)
        ini_conf.set(section, key, value)
        ini_conf.write(open(self.file_path, 'w+'))


class ResourceParser:
    def __init__(self, resource_path, db_server_host, db_user_name, db_password):
        self.resource_path = resource_path
        self.db_server_host = db_server_host
        self.db_server_instance = PushDatabase(self.db_server_host, db_user_name, db_password)

    def parser_description_yml(self, file_path):
        with open(file_path, 'r', encoding='utf-8') as f:
            if hasattr(yaml, 'FullLoader'):
                return yaml.load(f, Loader=yaml.FullLoader)
            else:
                return yaml.load(f)

    def read_resource(self, file_path):
        with open(file_path, 'r', encoding='utf-8') as f:
            return f.read()

    def parser_single_module(self, resource_path):
        pass

    def parser_all_module(self):
        for root, dirs, files in os.walk(self.resource_path):
            if 'description.yml' in files:
                description_dict = self.parser_description_yml(root + "/description.yml")
                description_dict['__file_path__'] = self.get_file_path(root)
                description_dict['__code_type__'] = self.get_code_type(root)
                for fileName in files:
                    if fileName == 'description.yml':
                        continue
                    description_dict['__code_' + fileName] = self.read_resource('%s/%s' %(root, fileName))
                self.db_server_instance.push(json.dumps(description_dict))

    def get_file_path(self, root_path):
        return root_path.replace(self.resource_path, 'https://github.com/DoZX/common-tools/blob/master/code-repositories')

    def get_code_type(self, root_path):
        path_list = root_path.split('/')
        if len(path_list) >= 3:
            return path_list[2]


class PushDatabase:
    def __init__(self, db_server_host, user_name, password):
        self.db_server_host = db_server_host
        self.user_name = user_name
        self.password = password
        self.init()

    def init(self):
        self.es_index_name = 'ct.' + str(time.strftime("%Y%m%d_%H%M", time.localtime()))
        url = 'http://%s/%s' %(self.db_server_host, self.es_index_name)
        request_headers = {'Content-Type': 'application/json'}
        request_body = '{\"settings\": {\"number_of_shards\": 9, \"number_of_replicas\": 0}}'
        response_data = requests.put(url, data=request_body, headers=request_headers, timeout=30, auth=(self.user_name, self.password))
        if response_data.status_code != 200: raise Exception("init elasticsearch add index:%s fail, exception:%s" % (self.es_index_name, str(response_data.content)))
        loginfo.info("init elasticsearch add index:%s ok" % (self.es_index_name))

        url = 'http://%s/_template/ct_template' %(self.db_server_host)
        response_data = requests.head(url, timeout=30, auth=(self.user_name, self.password))
        if response_data.status_code is not 200:
            loginfo.info("init elasticsearch need add ct_template")
            request_headers = {'Content-Type': 'application/json'}
            request_body = "{\"template\":\"ct.*\",\"order\":9,\"settings\":{\"number_of_shards\":9,\"number_of_replicas\":0,\"analysis\":{\"analyzer\":{\"comma\":{\"type\":\"simple\",\"pattern\":[\",\",\" \",\"'\",\"\\\"\",\";\",\"=\",\"(\",\")\",\"[\",\"]\",\"{\",\"}\",\"?\",\"@\",\"&\",\"<\",\">\",\"/\",\":\",\"\\n\",\"\\t\",\"\\r\"],\"lowercase\":false}}}},\"mappings\":{\"properties\":{\"__file_path__\":{\"type\":\"keyword\"},\"__code_type__\":{\"type\":\"keyword\"},\"name\":{\"type\":\"keyword\"},\"description\":{\"type\":\"keyword\"},\"keyword\":{\"type\":\"keyword\"}}}}"
            response_data = requests.put(url, data=request_body, headers=request_headers, timeout=30, auth=(self.user_name, self.password))
            if response_data.status_code != 200: raise Exception("init elasticsearch add ct_template fail, exception:%s" % (str(response_data.content)))
            loginfo.info("init elasticsearch add ct_template ok")
        else:
            loginfo.info("init elasticsearch have ct_template")

    def push(self, request_body_json_str):
        url = 'http://%s/%s/_doc' %(self.db_server_host, self.es_index_name)
        request_headers = {'Content-Type': 'application/json'}
        response_data = requests.put(url, data=request_body_json_str, headers=request_headers, timeout=120, auth=(self.user_name, self.password))
        if response_data.status_code != 200: raise Exception("push data to elasticsearch fail, exception:%s" % (str(response_data.content)))


