FROM openjdk:8

COPY target/land-registry-auth-0.0.1-SNAPSHOT.jar registration-service.jar

#CMD ["java", "-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=dev", "-jar", "registration-service.jar"]
CMD ["java", "-Dspring.profiles.active=uat", "-jar", "registration-service.jar"]
