FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/test-task.jar /test-task/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/test-task/app.jar"]
