FROM openjdk:17-jdk-slim
RUN java -jar target/topeneryv2-auth-service-dev.jar
RUN mkdir -p target/dependency
RUN cd target/dependency
RUN jar -xf ../*.jar
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 5000
ENTRYPOINT ["java","-cp","app:app/lib/*","com.topcode.topenergyv2.authservice.AuthServiceApplication"]