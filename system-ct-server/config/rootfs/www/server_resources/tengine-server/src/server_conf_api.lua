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

local function split(str, reps)
    local resultStrList = {}
    string.gsub(str,'[^'..reps..']+',function ( w )
        table.insert(resultStrList, w)
    end)
    return resultStrList
end

local function get_es_conf(es_ip_list, token)
    local es_conf_table = {}
    es_cluster_name = "ct-elasticsearch"
    es_http_port = 9200
    es_tcp_port = 9300
    es_minimum_master_nodes = #es_ip_list / 2 + 1
    es_start_node_name = "ct-node-"
    es_initial_master_nodes = {}
    es_seed_hosts = {}
    for num, es_ip in pairs(es_ip_list) do
        es_initial_master_nodes[num] = es_start_node_name .. num
        es_seed_hosts[num] = es_ip .. ":" .. es_tcp_port

        local es_conf = {}
        es_conf["cluster_name"] = es_cluster_name
        es_conf["http_port"] = es_http_port
        es_conf["tcp_port"] = es_tcp_port
        es_conf["minimum_master_nodes"] = es_minimum_master_nodes
        es_conf["node_name"] = es_start_node_name .. num
        es_conf["initial_master_nodes"] = es_initial_master_nodes
        es_conf["seed_hosts"] = es_seed_hosts
        es_conf["token"] = token
        es_conf_table[es_ip] = es_conf
    end
    return es_conf_table
end

local function get_kibana_conf(kibana_ip_list, es_ip_list, token)
    local kibana_conf_table = {}
    kibana_server_name = "ct-kibana"
    es_http_port = 9200
    elasticsearch_hosts = {}
    for num, es_ip in pairs(es_ip_list) do
        elasticsearch_hosts[num] = es_ip .. ":" .. es_http_port
    end
    for num, kibana_ip in pairs(kibana_ip_list) do
        local kibana_conf = {}
        kibana_conf["server_name"] = kibana_server_name
        kibana_conf["elasticsearch_hosts"] = elasticsearch_hosts
        kibana_conf["token"] = token
        kibana_conf_table[kibana_ip] = kibana_conf
    end
    return kibana_conf_table
end

local function get_conf(key)
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "get [" .. key ..  "] conf success"
    local shared_data = shared_conf:get(conf.CT_SERVER_SHARED_CONF_KEY .. key) or ''
    if string.len(shared_data) == 0 then
        status = conf.RESPONCE_CODE_INVALID_PARAM
        msg = "get [" .. key ..  "] conf is nil, Please try again later."
        shared_data = "{}"
    end
    local data = '{"status":' .. status .. ', "msg":"' .. msg .. '", "data":' .. shared_data .. '}'
    do_response(conf.LOG_INFO, data)
end

local function reload_conf()
    local status = conf.RESPONCE_CODE_SUCCESS
    local msg = "success"
    local iniconf = iniUtil.load(conf.CT_SERVER_CONF)
    -- token
    local token = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_TOKEN, "admin:admin")
    shared_conf:set(conf.CT_SERVER_SHARED_CONF_KEY .. conf.INI_TOKEN, '"'.. token .. '"', conf.CHECK_SHARED_TIMEOUT, os.time() + 1)
    -- es
    local es_node_ips = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_ES_NODE_IPS, "127.0.0.1")
    local es_ip_list = split(es_node_ips, ",")
    local es_conf_table = get_es_conf(es_ip_list, token)
    for es_ip, es_conf in pairs(es_conf_table) do
        shared_conf:set(conf.CT_SERVER_SHARED_CONF_KEY .. es_ip, dkjson.encode(es_conf), conf.CHECK_SHARED_TIMEOUT, os.time() + 1)
    end
    -- kibana
    local kibana_node_ips = iniUtil.get(iniconf, conf.INI_CT_SERVER_CONF_SECTION, conf.INI_KIBANA_NODE_IPS, "127.0.0.1")
    local kibana_ip_list = split(kibana_node_ips, ",")
    local kibana_conf_table = get_kibana_conf(kibana_ip_list, es_ip_list, token)
    for kibana_ip, kibana_conf in pairs(kibana_conf_table) do
        shared_conf:set(conf.CT_SERVER_SHARED_CONF_KEY .. kibana_ip, dkjson.encode(kibana_conf), conf.CHECK_SHARED_TIMEOUT, os.time() + 1)
    end
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
