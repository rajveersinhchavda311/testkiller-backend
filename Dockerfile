# syntax=docker/dockerfile:1

# ---- Build stage ----------------------------------------------------------
# Full JDK + Maven only at build time; not shipped in the final image.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first and pre-fetch dependencies so this layer is cached across
# code changes (faster rebuilds — deps only re-download when pom.xml changes).
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Now copy sources and build. Tests are skipped here because they boot the
# Spring context against a live DB, which isn't available in the build sandbox.
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ---- Runtime stage --------------------------------------------------------
# JRE-only image keeps the final container small and reduces attack surface.
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat jar produced above (wildcard avoids hardcoding the version).
COPY --from=build /app/target/*.jar app.jar

# Documentation only; Render maps the real port from the PORT env var, which
# Spring reads via server.port=${PORT:8080}.
EXPOSE 8080

# MaxRAMPercentage keeps the heap inside Render's 512 MB free tier so the
# container isn't OOM-killed. Shell form lets the JVM read container limits.
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75.0 -jar app.jar"]
