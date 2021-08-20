#!/usr/bin/env bash

echo -e "\n### Generating CAs, keys, and certs for clients and servers ###"
(cd ssl-certs && sh run.sh)

echo -e "\n### Copying server-related certs to docker image for mysql server ###"
cp -v ssl-certs/mysql/ca.pem src/main/docker/db/
cp -v ssl-certs/mysql/server*.pem src/main/docker/db/

