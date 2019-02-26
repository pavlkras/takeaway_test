#!/usr/bin/env bash
cd compose
docker-compose --file docker-compose.yml build mongo postgres rabbitmq
docker-compose --file docker-compose.yml up -d mongo postgres rabbitmq
cd ..
gradle clean test
cd compose
docker-compose down
cd ..
