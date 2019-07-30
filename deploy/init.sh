#!/usr/bin/env bash
adduser -q sclay
adduser sclay sudo
sudo apt-get install python-pip python-dev build-essential
sudo pip install 'fabric <2.0'
sudo pip install boto
git clone https://github.com/madfrog2047/NewsBlur.git ~/NewsBlur
cd ~/NewsBlur
#sed -i 's/    change_shell()/    # change_shell()/g' ~/NewsBlur/fabfile.py
#sed -i 's/    copy_certificates()/    # copy_certificates()/g' ~/NewsBlur/fabfile.py
#sed -i 's/    setup_db_firewall()/    # setup_db_firewall()/g' ~/NewsBlur/fabfile.py
mkdir -p ~/NewsBlur/secrets-newsblur/configs ~/NewsBlur/secrets-newsblur/settings ~/NewsBlur/secrets-newsblur/keys
cp ~/.ssh/id_rsa.pub ~/NewsBlur/secrets-newsblur/keys/newsblur.key.pub
cp ~/.ssh/id_rsa ~/NewsBlur/secrets-newsblur/keys/newsblur.key
cp /etc/hosts ~/NewsBlur/secrets-newsblur/configs/hosts
touch ~/NewsBlur/secrets-newsblur/configs/pgbouncer_auth.conf
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/local_settings.py
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/secrets-newsblur/configs/app_settings.py
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/secrets-newsblur/settings/app_settings.py
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/secrets-newsblur/settings/task_settings.py

mkdir -p /srv/secrets-newsblur/configs /srv/secrets-newsblur/settings /srv/secrets-newsblur/keys
cp ~/.ssh/id_rsa.pub /srv/secrets-newsblur/keys/newsblur.key.pub
cp ~/.ssh/id_rsa /srv/secrets-newsblur/keys/newsblur.key
cp /etc/hosts /srv/secrets-newsblur/configs/hosts
touch /srv/secrets-newsblur/configs/pgbouncer_auth.conf
cp ~/NewsBlur/local_settings.py.template /srv/secrets-newsblur/configs/app_settings.py
cp ~/NewsBlur/local_settings.py.template /srv/secrets-newsblur/settings/app_settings.py
cp ~/NewsBlur/local_settings.py.template /srv/secrets-newsblur/settings/task_settings.py
echo 127.0.0.1 db_mongo >> /etc/hosts
echo 127.0.0.1 db_mongo_analytics >> /etc/hosts
echo 127.0.0.1 db_redis >> /etc/hosts
echo 127.0.0.1 db_redis_pubsub >> /etc/hosts
echo 127.0.0.1 db_redis_story >> /etc/hosts
echo 127.0.0.1 db_redis_sessions >> /etc/hosts
echo 127.0.0.1 db_search_feed >> /etc/hosts
echo 127.0.0.1 db_search_story >> /etc/hosts
fab -H localhost setup_all