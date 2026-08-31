# ============================================================
# DOCKERFILE do BACKEND (Spring Boot)
# Onde colocar: na RAIZ do projeto backend (mesma pasta do pom.xml)
# ============================================================

# ---------- ETAPA 1: BUILD (compila o Java) ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia so o pom.xml primeiro (acelera builds futuros com cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o codigo e compila, gerando o .jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- ETAPA 2: RUNTIME (roda o .jar, imagem pequena) ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia so o .jar pronto da etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
