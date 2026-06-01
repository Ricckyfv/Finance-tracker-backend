# --- Fase 1: Compilación ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el archivo pom.xml y descargar dependencias (para aprovechar la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo JAR omitiendo los tests
COPY src ./src
RUN mvn package -DskipTests

# --- Fase 2: Ejecución ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar el JAR generado desde la fase de compilación
COPY --from=build /app/target/*.jar app.jar

# Render asigna el puerto dinámicamente mediante la variable de entorno PORT
EXPOSE 8080

# Ejecutar la aplicación pasando el puerto dinámico de Render
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]