# ========================================================
# EverGlade Multi-Stage Dockerfile (Spring Boot 4 / Java 21)
# ========================================================

# Stage 1: Build JAR with Maven Wrapper
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Batasi penggunaan RAM Maven agar tidak terkena OOM Killer di VPS
ENV MAVEN_OPTS="-Xmx512m -XX:+UseSerialGC"

# Copy Maven wrapper and descriptor to cache dependency layer
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:resolve -B

# Copy application source code and compile production artifact
COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Minimal Production JRE Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add non-root application user
RUN addgroup -S everglade && adduser -S everglade -G everglade

# Copy compiled JAR artifact
COPY --from=builder --chown=everglade:everglade /build/target/*.jar app.jar

USER everglade

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
