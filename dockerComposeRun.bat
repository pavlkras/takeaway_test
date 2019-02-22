call gradle assemble
cd compose
docker-compose rm -f && docker-compose pull
docker-compose --file docker-compose.yml build
docker-compose --file docker-compose.yml up
rem -d to add to detach