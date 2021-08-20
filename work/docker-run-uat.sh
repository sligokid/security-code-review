#!/usr/bin/env bash
docker stop land-registry-auth
docker stop land-registry-db
docker rmi land-registry-auth

docker build -t land-registry-db src/main/docker/db/
docker run -d --name land-registry-db --rm -p3306:3306 land-registry-db

echo "building jar..."
./mvnw clean install
echo "building jar done..."

docker build -f src/main/docker/uat/Dockerfile . -t land-registry-auth
docker run -d --name land-registry-auth --rm -p 9001:8888 --link land-registry-db:db land-registry-auth
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)

echo "waiting 10 secs for app to settle..."
sleep 10

docker logs --follow land-registry-auth
