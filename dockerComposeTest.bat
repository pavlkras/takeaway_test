cd compose
call docker-compose --file docker-compose.yml build mongo postgres rabbitmq
call docker-compose --file docker-compose.yml up -d mongo postgres rabbitmq
cd ..
call gradle clean test
cd compose
call docker-compose down
cd ..