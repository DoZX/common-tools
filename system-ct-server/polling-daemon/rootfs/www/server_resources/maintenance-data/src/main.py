#!/usr/bin/python

import os
import sys
from md_base import *

MAINTENANCE_DATA_CONF_PATH = '../conf/maintenance-data.ini'
INI_SECTION_CT_TENGINE_SERVER = 'CT_TENGINE_SERVER_CONF'
INI_SECTION_GIT_KEY_SERVER_HOST = 'server_host'
INI_SECTION_GIT = 'GIT_CONF'
INI_SECTION_GIT_KEY_GIT_ADDR = 'git_addr'
INI_SECTION_GIT_KEY_BRANCH = 'branch'
INI_SECTION_GIT_KEY_RESOURCE_PATH = 'resource_path'
INI_SECTION_GIT_KEY_REV = 'rev'

def main():
    conf = IniUtil(MAINTENANCE_DATA_CONF_PATH)
    server_host = conf.get(INI_SECTION_CT_TENGINE_SERVER, INI_SECTION_GIT_KEY_SERVER_HOST)
    git_addr = conf.get(INI_SECTION_GIT, INI_SECTION_GIT_KEY_GIT_ADDR)
    branch = conf.get(INI_SECTION_GIT, INI_SECTION_GIT_KEY_BRANCH)
    resource_path = conf.get(INI_SECTION_GIT, INI_SECTION_GIT_KEY_RESOURCE_PATH)
    resource_code_repositories_path = resource_path + '/code-repositories'
    rev = conf.get(INI_SECTION_GIT, INI_SECTION_GIT_KEY_REV)

    # checkout git code
    git = GitUtil(git_addr, branch, resource_path)
    if not os.path.exists(resource_path):
        ret, _ = git.clone()
        if not ret: raise Exception("maintenance-data git clone fail")

    # get / update rev
    ret, commit_id = git.rev()
    if ret and rev == commit_id:
        loginfo.info("git resource is latest version")
        sys.exit(0)
    conf.save(INI_SECTION_GIT, INI_SECTION_GIT_KEY_REV, commit_id)

    # pull latest code
    ret, _ = git.pull()
    if not ret: raise Exception("maintenance-data git pull fail")

    # parser code
    rp = ResourceParser(resource_code_repositories_path)
    rp.parser_all_module()


if __name__ == '__main__':
    try:
        loginfo.info("start maintenance-data....")
        main()
    except Exception as e:
        logerror.info("maintenance-data do fail, exception:%s" % str(e))
        raise Exception("maintenance-data do fail, exception:%s" % str(e))
