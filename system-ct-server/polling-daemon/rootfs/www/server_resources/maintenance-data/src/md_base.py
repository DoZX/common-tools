#!/usr/bin/python
# -*- coding: utf-8 -*-

import commands
from log_util import *

logUtil = LogUtil()
loginfo, logerror = logUtil.pub_logger()

def doBash(cmd):
    loginfo.info("cmd: %s" % (cmd))
    status, output = commands.getstatusoutput(cmd)
    loginfo.info("status: %s" % (status))
    loginfo.info("output: %s" % (output))
    return status, output