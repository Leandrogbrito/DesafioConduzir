# ============================================================
# DOCKERFILE do FRONTEND (React + Vite)
# Local exato: conduzir-frontend/conduzir-frontend/Dockerfile
# (mesma pasta onde esta o package.json)
# ============================================================

# ---------- ETAPA 1: BUILD (compila o React) ----------
FROM node:22-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build
# Gera a pasta /app/dist com os arquivos estaticos prontos (HTML/CSS/JS)

# ---------- ETAPA 2: SERVIDOR (nginx entrega os arquivos) ----------
FROM nginx:alpine

# Copia os arquivos prontos para o nginx servir
COPY --from=build /app/dist /usr/share/nginx/html

# Configuracao para o React Router funcionar (evita erro 404 ao atualizar a pagina)
# e para encaminhar chamadas de API para o backend
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
