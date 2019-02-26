# takeaway_test

Code challenge for TakeAway, Berlin

## Installation

Use [git](https://git-scm.com/) to clone the project
###Build tools
Project is based on [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html) and [Gradle 4.+](https://gradle.org/releases/)

###Deployment tools
Composing with [docker-compose](https://docs.docker.com/compose/install/)

Running on [Docker](https://www.docker.com/products/docker-desktop) containers

## Usage

Make sure Gradle 4.+, Docker, docker-compose are installed on your machine.

Please use scripts to build and run project:
* Windows: ``dockerComposeRun.bat``
* UNIX family: ``dockerComposeRun.sh``

You can also run all step by step:
* Build project with gradle. From the root directory call:
```cmd
gradle clean assemble
```
* From ``compose`` directory call:
```cmd
docker-compose --file docker-compose.yml build
docker-compose --file docker-compose.yml up
```
Add ``-d`` to detach process from terminal
```cmd
docker-compose --file docker-compose.yml up -d
``` 

###Swagger
When running on Docker:
* [employee-service](http://localhost:9090/swagger-ui.html) on port 9090
    * User for secured endpoints: 
        * name: **admin**, 
        * password: **admin**
* [events-service](http://localhost:9090/swagger-ui.html) on port 9091

## Tests
Make sure Java 8, Gradle 4.+, Docker, docker-compose are installed on your machine for tests.

Run script:
* Windows: ``dockerComposeTest.bat``
* UNIX family: ``dockerComposeTest.sh``

## Development and Debug
Please install and setup databases and message broker:
* [PostgreSQL 9+](https://www.postgresql.org/download/)
    * Create tables and dependencies:
    ```sql
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
    
    CREATE TABLE IF NOT EXISTS departments (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100) NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS employees (
      _uuid uuid PRIMARY KEY DEFAULT uuid_generate_v1(),
      email VARCHAR(254) NOT NULL UNIQUE,
      first_name VARCHAR(100) NOT NULL,
      last_name VARCHAR(100) NOT NULL,
      birthday timestamp without time zone NOT NULL,
      department_id INTEGER NOT NULL REFERENCES departments(id) ON DELETE RESTRICT
    );
    ```
* [MongoDB 4.+](https://www.mongodb.com/download-center)
* [RabbitMQ](https://www.rabbitmq.com/download.html) 

Or use prepared Docker images from ``compose`` directory:
```cmd
docker-compose --file docker-compose.yml build mongo postgres rabbitmq
docker-compose --file docker-compose.yml up -d mongo postgres rabbitmq
```
and use Spring Boot profile ``local`` to connect to containers