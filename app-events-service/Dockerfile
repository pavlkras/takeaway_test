FROM java:8-jre
MAINTAINER Pavel <pavel.krasnoshchek@gmail.com>

ADD ./build/libs/app-events-service.jar /app/
CMD [ "sh", "-c", "java $JAVA_OPTS -jar /app/app-events-service.jar" ]

EXPOSE 8080