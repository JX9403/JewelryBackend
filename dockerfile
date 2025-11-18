# Stage 1: Build ứng dụng
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B package -DskipTests

# Stage 2: Run app
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render sẽ truyền biến PORT, ta phải dùng nó
ENV PORT=8080

# Lắng nghe port do Render đặt
EXPOSE 8080

# Spring Boot bắt buộc chạy bằng cổng PORT do Render cấp
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
