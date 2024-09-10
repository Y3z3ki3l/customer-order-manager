# Utiliza directamente la imagen de JDK 17 en Alpine para una imagen ligera
FROM openjdk:17-jdk-alpine

# Información del autor
LABEL authors="Cap"

# Crea un volumen temporal
VOLUME /tmp

# Copia el archivo JAR generado en el directorio de trabajo del contenedor
COPY target/customer-order-microservice.jar app.jar

# Comando de inicio de la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
