---
--- Created by dhh.
---

module("config", package.seeall)
--- common
check_shared_timeout = 2592000


get_ct_server_conf_uri = "/conf"


RESPONCE_CODE_SUCCESS = 200
RESPONCE_CODE_BAD_REQUEST = 400
RESPONCE_CODE_NOT_FOUND = 404
RESPONCE_CODE_INVALID_PARAM = 403
RESPONCE_CODE_ERROR = 500


LOG_INFO = ngx.INFO
LOG_WARN = ngx.WARN
LOG_ERR = ngx.ERR

