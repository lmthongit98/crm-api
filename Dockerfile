FROM eclipse-temurin:17

WORKDIR /app

COPY target/crm-project-1.0-SNAPSHOT.jar /app/crm-api.jar

ENTRYPOINT ["java", "-jar", "crm-api.jar"]