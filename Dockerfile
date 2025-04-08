# Giai đoạn build
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy tất cả vào container
COPY . .

# Cấp quyền thực thi cho mvnw (chỉ cần trên Unix/Linux)
RUN chmod +x mvnw

# Build project, skip test
RUN ./mvnw clean package -DskipTests

# Giai đoạn chạy
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy file jar từ giai đoạn build
COPY --from=build /app/target/*.jar app.jar

# Expose cổng (nếu deploy lên Render thì mặc định là PORT env)
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
