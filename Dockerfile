# Giai đoạn build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Giai đoạn chạy
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Tạo thư mục lưu ảnh
RUN mkdir -p /app/images

# Copy jar file
COPY --from=build /app/target/*.jar app.jar

# Copy thư mục ảnh (nếu bạn có ảnh mẫu từ local)
# COPY --from=build /app/src/public/images /app/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
