FROM eclipse-temurin:21-jre-alpine
COPY ./target/*.jar .
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]