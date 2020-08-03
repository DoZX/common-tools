---
--- Created by dhh.
---

local conf = require("config")


local function do_response(log_level, result)
    ngx.log(log_level, host, " resultMsg: ", result)
    ngx.header["Content-Type"] = 'application/json'
    ngx.print(result)
    ngx.exit(conf.RESPONCE_CODE_SUCCESS)
end

local function get_conf(server_id, server_name)
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "success"
    local data = '{"status":' .. status .. ', "msg":"' .. msg .. '", "data":"' .. server_id .. ' : ' .. server_name .. '"}'
    do_response(conf.LOG_INFO, data)
end


--- main
local uri = ngx.var.uri
local method = ngx.var.request_method
if uri == conf.get_ct_server_conf_uri then
    local server_id = ngx.var.arg_id or ''
    local server_name = ngx.var.arg_name or ''
    if method == "GET" then
        ok, err = pcall(get_conf, server_id, server_name)
    end
    if not ok then
        local data = '{"status":' .. conf.RESPONCE_CODE_ERROR .. ', "msg":"' .. err .. '"}'
        do_response(conf.LOG_ERR, data)
    end
else
    local data = '{"status":' .. conf.RESPONCE_CODE_NOT_FOUND .. ', "msg":"invalid uri: "' .. uri .. '"}'
    do_response(conf.LOG_WARN, data)
end
