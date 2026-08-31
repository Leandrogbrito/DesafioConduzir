# 🐳 Diferencial 1 — Docker + PostgreSQL

## 🧒 O que é isso, de novo?

Você tem 3 "peças" que precisam rodar juntas:
1. 🗄️ Banco de dados (PostgreSQL)
2. ☕ Backend (Spring Boot)
3. ⚛️ Frontend (React)

Hoje você liga cada uma na mão (Eclipse, terminal, npm run dev). Com Docker, você digita **1 comando** e as 3 sobem sozinhas, já conversando entre si.

---

## 📁 Passo 1 — Organize as pastas

Sua estrutura final deve ficar assim:

```
DesafioConduzir/                 <- pasta principal
├── docker-compose.yml           <- (arquivo novo, vai aqui)
├── backend/                     <- sua pasta "desafio" (renomeie ou copie para "backend")
│   ├── Dockerfile                <- (arquivo novo, vai aqui)
│   ├── pom.xml
│   └── src/
│       └── main/resources/
│           ├── application.properties
│           └── application-docker.properties   <- (arquivo novo, vai aqui)
└── frontend/                    <- sua pasta "conduzir-frontend" (renomeie para "frontend")
    ├── Dockerfile                <- (renomeie "Dockerfile-frontend" para "Dockerfile")
    ├── nginx.conf                <- (arquivo novo, vai aqui)
    └── package.json
```

> 💡 Se preferir **não renomear** suas pastas atuais, é só editar o `docker-compose.yml` e trocar `./backend` e `./frontend` pelos nomes reais das suas pastas (ex: `./desafio` e `./conduzir-frontend/conduzir-frontend`).

---

## 📦 Passo 2 — Instalar o Docker Desktop

1. Baixe em: **https://www.docker.com/products/docker-desktop**
2. Instale (next, next, finish)
3. Abra o Docker Desktop e espere a "baleia" ficar verde/estável no canto 🐳

**Teste:** abra o `cmd` e digite:
```
docker --version
```
Deve aparecer um número de versão.

---

## ▶️ Passo 3 — Adicionar o PostgreSQL no `pom.xml`

Abra o `pom.xml` do backend e confirme que existe esta dependência (se não tiver, adicione):

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

> Isso não atrapalha o H2 — você pode ter os dois! O Spring escolhe qual usar baseado no "profile" ativo.

---

## 🚀 Passo 4 — Subir tudo!

1. Abra o **cmd** ou **PowerShell**.
2. Navegue até a pasta `DesafioConduzir` (a pasta PAI, onde está o `docker-compose.yml`):
   ```
   cd C:\Users\l.gomes.de.brito\...\DesafioConduzir
   ```
3. Rode:
   ```
   docker compose up --build
   ```
4. Espere baixar as imagens e construir (a **primeira vez demora uns 3-5 minutos**, é normal).

Quando parar de rolar texto e aparecer algo como:
```
conduzir-backend  | Started GestaoVeiculosApplication...
```
🎉 **Está tudo no ar!**

---

## 🌐 Passo 5 — Testar

- Backend: `http://localhost:8080/swagger-ui.html`
- Frontend: `http://localhost:5173`
- Banco: fica escondido, rodando na porta `5432` (o pgAdmin ou DBeaver conseguem conectar nele se quiser ver as tabelas)

---

## ⏹️ Como parar tudo

No mesmo terminal, aperte `Ctrl + C`, depois rode:
```
docker compose down
```

Para rodar de novo depois, é só `docker compose up` (sem precisar do `--build` de novo, a menos que mude o código).

---

## 🆘 Erros comuns

| Erro | Solução |
|---|---|
| `port is already allocated` | Feche o Eclipse/backend local antes (a porta 8080 já está em uso) |
| `Cannot connect to Docker daemon` | Abra o Docker Desktop primeiro, espere ele carregar |
| Build muito lento | Normal na primeira vez; as próximas ficam rápidas (cache) |
| Frontend não acha o backend | Confira se o `nginx.conf` foi copiado certinho |

---

## 🎤 Frase para a apresentação

> "Containerizei a aplicação com Docker multi-stage, separando build e runtime para reduzir o tamanho da imagem. Orquestrei backend, frontend e banco PostgreSQL via Docker Compose, com healthcheck garantindo a ordem de inicialização."
