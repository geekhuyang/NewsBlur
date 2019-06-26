#!/usr/bin/env bash
adduser -q sclay
adduser sclay sudo
sudo apt-get install python-pip python-dev build-essential
sudo pip install 'fabric <2.0'
sudo pip install boto
git clone https://github.com/madfrog2047/NewsBlur.git ~/NewsBlur
cd ~/NewsBlur
sed -i 's/    change_shell()/    # change_shell()/g' ~/NewsBlur/fabfile.py
sed -i 's/    copy_certificates()/    # copy_certificates()/g' ~/NewsBlur/fabfile.py
sed -i 's/    setup_db_firewall()/    # setup_db_firewall()/g' ~/NewsBlur/fabfile.py

mkdir -p ~/NewsBlur/secrets-newsblur/configs ~/NewsBlur/secrets-newsblur/settings
cp /etc/hosts ~/NewsBlur/secrets-newsblur/configs/hosts
touch ~/NewsBlur/secrets-newsblur/configs/pgbouncer_auth.conf
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/local_settings.py
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/secrets-newsblur/configs/app_settings.py
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/secrets-newsblur/settings/app_settings.py
cp ~/NewsBlur/local_settings.py.template ~/NewsBlur/secrets-newsblur/settings/task_settings.py
fab -H localhost setup_all