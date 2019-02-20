FROM java:8-jre
MAINTAINER Pavel <pavel.krasnoshchek@gmail.com>

ADD ./build/libs/app-employee-service.jar /app/
CMD [ "sh", "-c", "java $JAVA_OPTS -jar /app/app-employee-service.jar" ]

EXPOSE 8080