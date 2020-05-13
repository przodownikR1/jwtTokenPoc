FROM adoptopenjdk/openjdk11:alpine-slim
LABEL maintainer="przodownikR1@gmail.com"

#RUN apt-get update && apt-get install -y curl

VOLUME /tmp
ENV JAVA_OPTS="-Xmx256m -XX:+UseContainerSupport -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+OptimizeStringConcat  -XX:+HeapDumpOnOutOfMemoryError -XX:+ExitOnOutOfMemoryError -XshowSettings:vm"

RUN addgroup -S adauth && adduser -S adauth -G adauth
USER adauth:adauth

ARG DEPENDENCY=build/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8096

#ENTRYPOINT ["java","$JAVA_OPTS" ,"-Djava.security.egd=file:/dev/./urandom","-cp","app:app/lib/*","pl.scalatech.cashsystem.Application"]

ENTRYPOINT exec java \
$JAVA_OPTS \
-noverify \
-Djava.security.egd=file:/dev/./urandom \
-cp app:app/lib/* \
Application

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s CMD curl http://localhost:8097/ops/health