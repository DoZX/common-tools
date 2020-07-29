#!/bin/bash
start_log_file='/www/maintenance-data/logs/start_md.log'

echo $(date) > $start_log_file
echo 'maintenance-data start...' >> $start_log_file

source /etc/profile
cd /www/maintenance-data/src/ && /usr/bin/python /www/maintenance-data/src/main.py
if [ $? == 0 ]; then
    echo 'maintenance-data do success' >> $start_log_file
else
    echo 'maintenance-data do fail' >> $start_log_file
fi