# Etapa 1: compilación con Maven
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn ./.mvn
COPY src ./src

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Etapa 2: ejecución
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Perfil utilizado por defecto en el contenedor.
ENV SPRING_PROFILES_ACTIVE=prod

# Usa PORT si la plataforma de despliegue lo proporciona; localmente utiliza 8080.
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
