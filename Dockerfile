# Giai đoạn build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy wrapper và pom trước để tận dụng cache layer của Docker
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Tải các dependency trước (nếu không có thay đổi thì cache lại)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy toàn bộ source code
COPY . .

# Build project
RUN ./mvnw clean package -DskipTests

# Giai đoạn chạy
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy file WAR đã build
COPY --from=build /app/target/DrComputer-0.0.1-SNAPSHOT.war drcomputer.war

EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "drcomputer.war"]
