# ============================================================
# DOCKERFILE = a "receita" para construir a caixa (imagem) do app.
# Usamos DUAS etapas (multi-stage) para a caixa final ficar pequena.
# ============================================================

# ---------- ETAPA 1: BUILD (a "cozinha") ----------
# Aqui compilamos o codigo Java e geramos o arquivo .jar
FROM maven:3.9-eclipse-temurin-21 AS build

# Pasta de trabalho dentro do container
WORKDIR /app

# 1) Copia so o pom.xml primeiro e baixa as dependencias.
#    (Truque: se o pom nao mudar, o Docker reaproveita o cache = build mais rapido)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2) Agora copia o codigo-fonte e compila, gerando o .jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- ETAPA 2: RUNTIME (a "caixa de entrega") ----------
# Imagem so com o Java para RODAR (bem menor, sem o Maven)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia SOMENTE o .jar pronto da etapa de build
COPY --from=build /app/target/*.jar app.jar

# A aplicacao roda na porta 8080
EXPOSE 8080

# Comando que liga a aplicacao quando o container inicia
ENTRYPOINT ["java", "-jar", "app.jar"]
