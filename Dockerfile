# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy pom.xml first for better layer caching
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn -B -DskipTests clean package

# Runtime stage
FROM tomcat:10.1-jdk17-temurin

# Installation de curl pour health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the built WAR file
COPY --from=build /workspace/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Set JVM options for better performance
ENV CATALINA_OPTS="-Xms512m -Xmx1024m -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker"

# Create logs directory
RUN mkdir -p /usr/local/tomcat/logs

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/health/simple || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]