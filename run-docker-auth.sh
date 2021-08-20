docker stop land-registry-auth
docker rmi land-registry-auth

docker stop land-registry-auth-db
docker build -t land-registry-auth-db src/main/docker/db/
docker run -d --name land-registry-auth-db --rm -p3306:3306 land-registry-auth-db

echo "building jar..."
./mvnw clean install
echo "building jar done..."

docker build -f src/main/docker/dev/Dockerfile . -t land-registry-auth
docker run -d --name land-registry-auth --rm -p 9501:3501 --link land-registry-auth-db:db land-registry-auth
#docker rmi $(docker images --filter "dangling=true" -q --no-trunc)

echo "waiting 30 secs for app to settle..."
sleep 30
docker logs --follow land-registry-auth
