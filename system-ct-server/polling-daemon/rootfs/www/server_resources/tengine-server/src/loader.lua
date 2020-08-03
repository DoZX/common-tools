---
--- Created by dhh.
---

-- Loading framework
local init_list = {
    "/www/tengine-server/src/init.lua",
}

for i, f in ipairs(init_list) do
    c, err = loadfile(f)
    if err then
        ngx.log(ngx.ERR,"load file err: ", err)
    else
        c()
    end
end
