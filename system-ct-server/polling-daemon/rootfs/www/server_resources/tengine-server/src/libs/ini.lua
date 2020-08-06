module("libs.ini", package.seeall)

--[[
    载入一个INI文件
--]]
function load(filename)
    local data = {}
    local session = nil -- 当前结点，为空则不会读取
    local file = io.open(filename, "r")
    for line in file:lines() do
        --处理注释
        line = string.sub(line, 1, (string.find(line, "#") or string.find(line, ";") or (string.len(line) + 1)) - 1)
        if line ~= "" then
            --首选判断是不是session
            local s = line:match("%[(.+)%]")
            if s then --session的情况，赋值当前session
                session = s
            elseif session then --不是session的情况，必须当前session有值
                local epos = string.find(line, " = ") --查找第一个等于号位置
                if epos and epos ~= 1 and epos ~= string.len(line) then --有等于号，并且不是第一个和最后一个位置
                    --不存在这个节点则创建一个
                    if data[session] == nil then
                        data[session] = {}
                    end
                    --添加这个值
                    data[session][string.sub(line, 1, epos - 1)] = string.sub(line, epos + 3)
                end
            end
        end
    end
    file:close()
    return data
end
--[[
    保存一个INI文件
--]]
function save(data, filename)
    local file = io.open(filename, "w")
    for session, group in pairs(data) do
        file:write("["..session.."]\n")
        for key, value in pairs(group) do
            file:write(key.." = "..value.."\n")
        end
    end
    file:close()
end
--[[
    打印INI文件
--]]
function print(data)
    for session, group in pairs(data) do
        print(session)
        for key, value in pairs(group) do
            print("    "..key.." : "..value)
        end
    end
end
--[[
    获取一个值
--]]
function get(data, session, key, default)
    if data[session] then
        return data[session][key] or default
    else
        return default
    end
end
--[[
    添加一个值
--]]
function set(data, session, key, value)
    if data[session] == nil then
        data[session] = {}
    end
    data[session][key] = value
end