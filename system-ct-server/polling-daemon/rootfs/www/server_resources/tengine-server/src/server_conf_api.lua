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

local function get_conf(server_ip)
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "success"
    local data = '{"status":' .. status .. ', "msg":"' .. msg .. '", "data":"' .. server_ip .. '"}'
    do_response(conf.LOG_INFO, data)
end

local function reload_conf()
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "success"
    local iniconf = iniUtil.load(conf.CT_SERVER_CONF)
    local conf_data = dkjson.encode(iniconf)
    local data = '{"status":' .. status .. ', "msg":"' .. msg .. '", "data":'.. conf_data .. '}'
    do_response(conf.LOG_INFO, data)
end

--- main
local uri = ngx.var.uri
local method = ngx.var.request_method
if uri == conf.get_ct_server_conf_uri then
    if method == "GET" then
        local server_ip = ngx.var.arg_ip or ''
        ok, err = pcall(get_conf, server_ip)
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