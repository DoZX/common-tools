---
--- Created by dhh.
---
_M = {}

--- common
_M.CHECK_SHARED_TIMEOUT = 0
_M.CT_SERVER_SHARED_CONF_KEY = "conf__"
_M.CT_SERVER_CONF = "/www/tengine-server/conf/tengine-server.ini"
_M.INI_CT_SERVER_CONF_SECTION = "CT_SERVER_CONF"
_M.INI_TOKEN = "token"
_M.INI_ES_NODE_IPS = "es_node_ips"
_M.INI_KIBANA_NODE_IPS = "kibana_node_ips"
_M.INI_PD_TENGINE_NODE_IP = "pd_tengine_node_ip"

--- uri
_M.get_ct_server_conf_uri = "/conf"

--- responce code
_M.RESPONCE_CODE_SUCCESS = 200
_M.RESPONCE_CODE_BAD_REQUEST = 400
_M.RESPONCE_CODE_NOT_FOUND = 404
_M.RESPONCE_CODE_INVALID_PARAM = 403
_M.RESPONCE_CODE_ERROR = 500

--- log level
_M.LOG_INFO = ngx.INFO
_M.LOG_WARN = ngx.WARN
_M.LOG_ERR = ngx.ERR


return _M