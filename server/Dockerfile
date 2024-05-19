FROM bellsoft/liberica-openjdk-alpine

CMD ["./gradlew", "clean", "build"]

VOLUME /server

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]