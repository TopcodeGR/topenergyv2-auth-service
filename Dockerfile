FROM openjdk:17-jdk-slim
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY mvnw.cmd ./
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 5000
ENTRYPOINT ["java","-cp","app:app/lib/*","com.topcode.topenergyv2.authservice.AuthServiceApplication"]