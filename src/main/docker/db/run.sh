#!/usr/bin/env bash

cp /tmp/*.pem /var/lib/mysql
chown mysql:mysql /var/lib/mysql/*.pem
chmod 600 /var/lib/mysql/server-key.pem
