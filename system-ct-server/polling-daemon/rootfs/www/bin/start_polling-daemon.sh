#!/bin/bash
cron_file='/var/spool/cron/root'
cron_log_file='/www/bin/cron_start_polling-daemon.log'
# 每一分钟被拉起1次日志(只看最后一次拉起的时间)
echo $(date) > $cron_log_file
# 检查是否已加入任务
if ! grep -Fxq '*/1 * * * * /bin/sh /www/bin/start_polling-daemon.sh' $cron_file; then
    echo '*/1 * * * * /bin/sh /www/bin/start_polling-daemon.sh' >> $cron_file
    /etc/init.d/crond reload
    echo 'crontab reload' >> $cron_log_file
fi

echo 'maintenance-data crontab start...' >> $cron_log_file
/bin/sh /www/maintenance-data/src/start_md.sh
if [ $? == 0 ]; then
    echo 'maintenance-data crontab success' >> $cron_log_file
else
    echo 'maintenance-data crontab fail' >> $cron_log_file
fi
