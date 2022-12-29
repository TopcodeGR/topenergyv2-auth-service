FROM openjdk:17-jdk-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY mvnw.cmd ./
RUN ./mvnw package && java -jar target/gs-spring-boot-docker-0.1.0.jar
RUN mkdir -p target/dependency
RUN cd target/dependency
RUN jar -xf ../*.jar
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 5000
ENTRYPOINT ["java","-cp","app:app/lib/*","com.topcode.topenergyv2.authservice.AuthServiceApplication"]