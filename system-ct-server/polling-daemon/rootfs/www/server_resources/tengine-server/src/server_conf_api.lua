---
--- Created by dhh.
---
local conf = require("config")
local iniUtil = require("libs.ini")
local dkjson = require("libs.dkjson")

local function do_response(log_level, result)
    ngx.log(log_level, host, " resultMsg: ", result)
    ngx.header["Content-Type"] = 'application/json'
    ngx.print(result)
    ngx.exit(conf.RESPONCE_CODE_SUCCESS)
end

local function get_conf(key)
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "get [" .. key ..  "] conf success"
    local shared_data = shared_conf:get(conf.CT_SERVER_SHARED_CONF_KEY .. key) or ''
    if string.len(shared_data) == 0 then
        status = conf.RESPONCE_CODE_INVALID_PARAM
        msg = "get [" .. key ..  "] conf is nil, Please try again later."
        shared_data = {}
    end
    local data = '{"status":' .. status .. ', "msg":"' .. msg .. '", "data":' .. shared_data .. '}'
    do_response(conf.LOG_INFO, data)
end

local function reload_conf()
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "success"
    local iniconf = iniUtil.load(conf.CT_SERVER_CONF)
    local token = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_TOKEN, "admin:admin")
    shared_conf:set(conf.CT_SERVER_SHARED_CONF_KEY .. conf.INI_TOKEN, '"'.. token .. '"', conf.CHECK_SHARED_TIMEOUT, os.time() + 1)
    local es_node_ips = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_ES_NODE_IPS, "127.0.0.1")
    -- ip > cluster.name node.name
    local kibana_node_ips = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_KIBANA_NODE_IPS, "127.0.0.1")
    -- ip > server.name
    local pd_tengine_node_ip = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_PD_TENGINE_NODE_IP, "127.0.0.1")

    local conf_data = dkjson.encode(iniconf)
    local data = '{"status":' .. status .. ', "msg":"' .. msg .. '", "data":'.. conf_data .. '}'
    do_response(conf.LOG_INFO, data)
end

--- main
local uri = ngx.var.uri
local method = ngx.var.request_method
if uri == conf.get_ct_server_conf_uri then
    if method == "GET" then
        local key = ngx.var.arg_k or ''
        ok, err = pcall(get_conf, key)
    elseif method == "POST" then
        ok, err = pcall(reload_conf)
    end
    if not ok then
        local data = '{"status":' .. conf.RESPONCE_CODE_ERROR .. ', "msg":"' .. err .. '"}'
        do_response(conf.LOG_ERR, data)
    end
else
    local data = '{"status":' .. conf.RESPONCE_CODE_NOT_FOUND .. ', "msg":"invalid uri: "' .. uri .. '"}'
    do_response(conf.LOG_WARN, data)
end