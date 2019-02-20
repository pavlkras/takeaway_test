#!/usr/bin/env bash
gradle assemble
cd compose
docker-compose --file docker-compose.yml build
docker-compose --file docker-compose.yml up
# -d to add to detach