# Utiliza una imagen oficial de Java como base
FROM eclipse-temurin:21-jdk-alpine

# Establece un directorio de trabajo dentro del contenedor
WORKDIR /app

COPY target/sof-transaction-root-0.0.1-SNAPSHOT.jar app-transaction-0.0.1.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app-transaction-0.0.1.jar"]