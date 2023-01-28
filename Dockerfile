FROM maven:3.8.1-openjdk-17-slim as maven
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package

FROM openjdk:17-jdk-slim
ARG JAR_FILE=topenergyv2-auth-service.jar
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/
EXPOSE 5000
ENTRYPOINT ["java","-jar","topenergyv2-auth-service.jar"]