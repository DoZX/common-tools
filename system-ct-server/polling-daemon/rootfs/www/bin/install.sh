#!/bin/bash

PACKAGE_PATH='/www/tengine-server/zip'
LUA_INSTALL_PATH='/www/tengine-server/lua_resources'
TENGINE_INSTALL_PATH='/www/tengine-server/server'

if [ ! -e $LUA_INSTALL_PATH ]; then
    mkdir -p $LUA_INSTALL_PATH && mkdir -p $TENGINE_INSTALL_PATH
fi

init_install_packages() {
    cd $PACKAGE_PATH
    if [ ! -e $PACKAGE_PATH/tengine-2.2.0.tar.gz ]; then
        wget http://tengine.taobao.org/download/tengine-2.2.0.tar.gz
    fi
    if [ ! -e $PACKAGE_PATH/ngx_cache_purge-2.3.tar.gz ]; then
        wget http://labs.frickle.com/files/ngx_cache_purge-2.3.tar.gz
    fi
    if [ ! -e $PACKAGE_PATH/v0.3.0rc1.tar.gz ]; then
        wget https://github.com/simpl/ngx_devel_kit/archive/v0.3.0rc1.tar.gz
    fi
    if [ ! -e $PACKAGE_PATH/LuaJIT-2.0.4.tar.gz ]; then
        wget http://luajit.org/download/LuaJIT-2.0.4.tar.gz
    fi
    if [ ! -e $PACKAGE_PATH/v0.10.2.tar.gz  ]; then
        wget https://github.com/openresty/lua-nginx-module/archive/v0.10.2.tar.gz
    fi
    if [ ! -e $PACKAGE_PATH/pcre-8.38.tar.gz  ]; then
        wget http://downloads.sourceforge.net/project/pcre/pcre/8.38/pcre-8.38.tar.gz
    fi

    tar -zxvf tengine-2.2.0.tar.gz
    tar -zxvf ngx_cache_purge-2.3.tar.gz
    tar -zxvf v0.3.0rc1.tar.gz
    tar -zxvf LuaJIT-2.0.4.tar.gz
    tar -zxvf v0.10.2.tar.gz
    tar -zxvf pcre-8.38.tar.gz
}

make_install_lua() {
    cd $PACKAGE_PATH/LuaJIT-2.0.4 && make && make install PREFIX=$LUA_INSTALL_PATH
    grep -i -n "^export LUAJIT_LIB=" /etc/profile
    if [ $? -ne 0 ]; then
        echo 'export LUAJIT_LIB='$LUA_INSTALL_PATH'/lib' >> /etc/profile && \
        echo 'export LUAJIT_INC='$LUA_INSTALL_PATH'/include/luajit-2.0' >> /etc/profile && \
        source /etc/profile
    fi
}

make_install_tengine() {
    cd $PACKAGE_PATH/tengine-2.2.0 && \
    ./configure --prefix=$TENGINE_INSTALL_PATH \
                --with-pcre=$PACKAGE_PATH/pcre-8.38 \
                --with-debug \
                --add-module=$PACKAGE_PATH/ngx_cache_purge-2.3 \
                --with-http_stub_status_module \
                --with-http_ssl_module \
                --add-module=$PACKAGE_PATH/ngx_devel_kit-0.3.0rc1 \
                --add-module=$PACKAGE_PATH/lua-nginx-module-0.10.2/ \
                --with-ld-opt=-Wl,-rpath,$LUA_INSTALL_PATH/lib && \
    make && make install
    mv -f /www/tengine-server/conf/nginx.conf $TENGINE_INSTALL_PATH/conf/nginx.conf && \
    mv -f /www/tengine-server/conf/conf.d $TENGINE_INSTALL_PATH/conf/
}

install_python_packages() {
    /usr/bin/pip install requests configparser
}

main() {
  init_install_packages
  make_install_lua
  make_install_tengine
  install_python_packages
  echo 'install success!'
}

main