call gradle assemble
cd compose
rem call docker-compose rm -f && docker-compose pull
call docker-compose --file docker-compose.yml build
call docker-compose --file docker-compose.yml up
rem -d to add to detach
cd ..
