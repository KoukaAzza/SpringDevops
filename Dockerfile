FROM openjdk:8
COPY target/DevOps_Project-1.0.jar .
EXPOSE 8081
ENTRYPOINT ["java","-jar","DevOps_Project-1.0.jar"]