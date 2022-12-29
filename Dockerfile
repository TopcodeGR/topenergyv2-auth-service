FROM openjdk:17-jdk-slim
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY mvnw.cmd ./
RUN sudo chmod +x mvnw
RUN sudo ./mvnw package && java -jar target/gs-spring-boot-docker-0.1.0.jar
RUN mkdir -p target/dependency
RUN cd target/dependency
RUN jar -xf ../*.jar
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 5000
ENTRYPOINT ["java","-cp","app:app/lib/*","com.topcode.topenergyv2.authservice.AuthServiceApplication"]