#!/usr/bin/python
# -*- coding: utf-8 -*-

import os
import time
import commands
import traceback
import configparser
import yaml
import json
import requests
from log_util import *

logUtil = LogUtil()
loginfo, logerror = logUtil.pub_logger()
RET_OK = True
RET_ERR = False
DESCRIPTION_FILE_NAME = 'description.yml'
IGNORE_FILE_NAME = '.gitignore'

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
        with open(file_path, 'r') as f:
            if hasattr(yaml, 'FullLoader'):
                return yaml.load(f, Loader=yaml.FullLoader)
            else:
                return yaml.load(f)

    def read_resource(self, file_path):
        with open(file_path, 'r') as f:
            return f.read()

    def parser_single_module(self, resource_path):
        pass

    def parser_all_module(self):
        for type_file in os.listdir(self.resource_path):
            ct_code_type = type_file
            for module_file in os.listdir(os.path.join(self.resource_path, type_file)):
                if module_file == IGNORE_FILE_NAME: continue
                module_file_path = os.path.join(self.resource_path, type_file, module_file)
                module_files = os.listdir(module_file_path)
                if DESCRIPTION_FILE_NAME in module_files:
                    description_file_path = os.path.join(module_file_path, DESCRIPTION_FILE_NAME)
                    description_dict = self.parser_description_yml(description_file_path)
                    description_dict['ct__file_path__'] = self.get_git_url(module_file_path)
                    description_dict['ct__code_type__'] = ct_code_type
                    description_dict['ct__date__'] = self.get_last_update_time(description_file_path)
                    for code_file_l1 in module_files:
                        if code_file_l1 == DESCRIPTION_FILE_NAME: continue
                        code_file_path_l1 = os.path.join(module_file_path, code_file_l1)
                        code_file_list = []
                        if os.path.isdir(code_file_path_l1):
                            self.get_file_path_list(code_file_path_l1, code_file_list)
                        if len(code_file_list) > 0:
                            for code_file_path_ln in code_file_list:
                                description_dict['ct__code_' + code_file_path_ln.replace(code_file_l1, '')] = self.read_resource(code_file_path_l1)
                        else:
                            description_dict['ct__code_' + code_file_l1] = self.read_resource(code_file_path_l1)
                    self.db_server_instance.push(json.dumps(description_dict))
                else:
                    loginfo.info("maintenance-data parser path %s %s does not exist" %(module_file_path, DESCRIPTION_FILE_NAME))
        loginfo.info("maintenance-data parser all module success")

    def get_git_url(self, root_path):
        return root_path.replace(self.resource_path, 'https://github.com/DoZX/common-tools/blob/master/code-repositories')

    def get_last_update_time(self, file_path):
        return time.strftime('%Y-%m-%dT%H:%M:%S', time.localtime(os.stat(file_path).st_mtime))

    def get_file_path_list(self, path, file_path_list):  #传入存储的list
        for file in os.listdir(path):
            file_path = os.path.join(path, file)
            if os.path.isdir(file_path): self.get_file_path_list(file_path, file_path_list)
            file_path_list.append(file_path)

class PushDatabase:
    def __init__(self, db_server_host, user_name, password):
        self.db_server_host = db_server_host
        self.user_name = user_name
        self.password = password
        self.init()

    def init(self):
        self.es_index_name = 'ct.' + str(time.strftime("%Y%m%d_%H%M", time.localtime()))
        url = 'http://%s/%s' %(self.db_server_host, self.es_index_name)
        response_data = requests.head(url, timeout=30, auth=(self.user_name, self.password))
        if response_data.status_code != 200:
            request_headers = {'Content-Type': 'application/json'}
            request_body = '{\"settings\": {\"number_of_shards\": 6, \"number_of_replicas\": 0}}'
            response_data = requests.put(url, data=request_body, headers=request_headers, timeout=30, auth=(self.user_name, self.password))
            if response_data.status_code != 200: raise Exception("init elasticsearch add index:%s fail, code:%s exception:%s" % (self.es_index_name, response_data.status_codem, str(response_data.content)))
            loginfo.info("init elasticsearch add index:%s success" % (self.es_index_name))
        else:
            loginfo.info("init elasticsearch have %s" %(self.es_index_name))

        url = 'http://%s/_template/ct_template' %(self.db_server_host)
        response_data = requests.head(url, timeout=30, auth=(self.user_name, self.password))
        if response_data.status_code != 200:
            loginfo.info("init elasticsearch need add ct_template")
            request_headers = {'Content-Type': 'application/json'}
            request_body = "{\"template\":\"ct.*\",\"order\":9,\"settings\":{\"number_of_shards\":6,\"number_of_replicas\":0,\"analysis\":{\"analyzer\":{\"comma\":{\"type\":\"simple\",\"pattern\":[\",\",\" \",\"'\",\"\\\"\",\";\",\"=\",\"(\",\")\",\"[\",\"]\",\"{\",\"}\",\"?\",\"@\",\"&\",\"<\",\">\",\"/\",\":\",\"\\n\",\"\\t\",\"\\r\"],\"lowercase\":false}}}},\"mappings\":{\"properties\":{\"ct__file_path__\":{\"type\":\"keyword\"},\"ct__code_type__\":{\"type\":\"keyword\"},\"ct__date__\":{\"type\":\"date\"},\"name\":{\"type\":\"keyword\"},\"description\":{\"type\":\"keyword\"},\"keyword\":{\"type\":\"keyword\"}}}}"
            response_data = requests.put(url, data=request_body, headers=request_headers, timeout=30, auth=(self.user_name, self.password))
            if response_data.status_code != 200: raise Exception("init elasticsearch add ct_template fail, code:%s exception:%s" % (response_data.status_code, str(response_data.content)))
            loginfo.info("init elasticsearch add ct_template success")
        else:
            loginfo.info("init elasticsearch have ct_template")

    def push(self, request_body_json_str):
        url = 'http://%s/%s/_doc' %(self.db_server_host, self.es_index_name)
        request_headers = {'Content-Type': 'application/json'}
        response_data = requests.post(url, data=request_body_json_str, headers=request_headers, timeout=120, auth=(self.user_name, self.password))
        if response_data.status_code not in [200, 201]: raise Exception("push data to elasticsearch fail, code:%s exception:%s" % (response_data.status_code, str(response_data.content)))


