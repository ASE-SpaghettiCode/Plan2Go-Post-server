FROM openjdk:17-jdk-alpine
VOLUME /tmp
#RUN ['./gradlew','build']
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]
