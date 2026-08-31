# ☁️ Diferencial 4 — Deploy na AWS

## 🧒 O que vamos fazer

Vamos colocar sua aplicação "no ar" de verdade, com um link público que qualquer pessoa pode acessar — não só você, no seu computador.

```
Frontend (S3)  →  Backend (Elastic Beanstalk)  →  Banco (RDS PostgreSQL)
```

Vamos pelo caminho **mais simples possível**, usando o **Console da AWS** (o site, sem precisar de comandos complicados).

> ⚠️ **Sobre custos:** vamos usar tudo dentro do **Free Tier** (camada gratuita). Ainda assim, configure um alerta de gastos no Passo 0 — é rapidinho e te protege de sustos.

---

## 💰 Passo 0 — Proteção contra custos (faça isso primeiro!)

1. Acesse o Console AWS → busque **"Billing"** (Faturamento).
2. Menu lateral → **Budgets** → **Create budget**.
3. Escolha **"Zero spend budget"** (orçamento de gasto zero) — ele te avisa por e-mail se qualquer coisa começar a cobrar.
4. Salve.

Feito isso, pode seguir tranquilo. 😊

---

## 🗄️ Passo 1 — Criar o banco de dados (RDS PostgreSQL)

1. No Console AWS, busque **"RDS"**.
2. Clique em **"Create database"**.
3. Escolha:
   - **Engine:** PostgreSQL
   - **Templates:** **Free tier** ✅
   - **DB instance identifier:** `conduzir-db`
   - **Master username:** `postgres`
   - **Master password:** crie uma senha forte e **anote em algum lugar seguro**
4. Em **"Public access"**, marque **Yes** (para o Elastic Beanstalk conseguir alcançar).
5. Clique em **"Create database"** e espere uns 5-10 minutos (status vira "Available").
6. Clique no banco criado e **copie o "Endpoint"** (algo como `conduzir-db.xxxxx.us-east-1.rds.amazonaws.com`). Você vai precisar dele já já.

### ⚠️ Liberar o acesso (Security Group)
1. Ainda na tela do banco, clique no **Security Group** (VPC security group).
2. Aba **"Inbound rules"** → **Edit inbound rules** → **Add rule**:
   - Type: **PostgreSQL**
   - Source: **Anywhere-IPv4** (0.0.0.0/0) — *(para o desafio está OK; em produção real seria mais restrito)*
3. Salve.

---

## ☕ Passo 2 — Publicar o Backend (Elastic Beanstalk)

O Elastic Beanstalk é o jeito **mais fácil** de colocar seu `.jar` na nuvem — a AWS cuida do servidor para você.

### 2.1 — Gerar o `.jar` de produção
No Eclipse ou terminal, dentro da pasta do backend:
```
mvnw clean package -DskipTests
```
Isso gera um arquivo em `target/gestao-veiculos-1.0.0.jar` (o nome pode variar um pouco).

### 2.2 — Criar o ambiente no Console
1. Busque **"Elastic Beanstalk"** no Console AWS.
2. **Create application**.
3. Preencha:
   - **Application name:** `conduzir-backend`
   - **Platform:** **Java** (escolha a versão **Corretto 21**)
   - **Application code:** **Upload your code** → selecione o `.jar` que você gerou
4. Clique em **Create application** (demora uns 5 minutos para provisionar).

### 2.3 — Configurar as variáveis de ambiente (conectar no banco)
1. Depois de criado, vá em **Configuration** → **Software** → **Edit**.
2. Em **"Environment properties"**, adicione:

| Nome | Valor |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://SEU-ENDPOINT-RDS:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | (a senha que você criou no Passo 1) |
| `SPRING_PROFILES_ACTIVE` | `docker` (reaproveita o mesmo perfil PostgreSQL que já criamos!) |
| `GEMINI_API_KEY` | (sua chave do Gemini, se for usar o diferencial 3) |

3. Clique em **Apply** e espere reiniciar.

### 2.4 — Testar
Copie a **URL** que aparece no topo do ambiente (algo como `conduzir-backend.us-east-1.elasticbeanstalk.com`) e acesse:
```
http://SUA-URL/swagger-ui.html
```
Se abrir o Swagger → backend publicado! 🎉

---

## ⚛️ Passo 3 — Publicar o Frontend (S3)

### 3.1 — Gerar a versão de produção
No terminal, dentro da pasta do frontend:
```
npm run build
```
Isso cria uma pasta `dist/` com os arquivos prontos.

### 3.2 — Antes de subir: aponte o frontend para o backend da nuvem
No arquivo `src/api/client.ts`, troque a `baseURL` para a URL do Elastic Beanstalk (ao invés do proxy local). Exemplo:
```ts
export const api = axios.create({
  baseURL: 'http://SUA-URL-DO-BACKEND.elasticbeanstalk.com',
})
```
Rode `npm run build` de novo depois dessa mudança.

### 3.3 — Criar o bucket S3
1. Busque **"S3"** no Console AWS.
2. **Create bucket** → nome: `conduzir-frontend-seu-nome` (tem que ser único globalmente).
3. **Desmarque** "Block all public access" (para o site poder ser visto publicamente).
4. Crie o bucket.

### 3.4 — Ativar hospedagem de site estático
1. Entre no bucket → aba **Properties**.
2. Role até **"Static website hosting"** → **Edit** → **Enable**.
3. Index document: `index.html`
4. Error document: `index.html` (importante para o React Router funcionar!)
5. Salve.

### 3.5 — Enviar os arquivos
1. Aba **Objects** → **Upload** → selecione **todo o conteúdo** da pasta `dist/` (não a pasta em si, o que está dentro dela).
2. Depois de enviado, defina a política do bucket para permitir leitura pública:
   - Aba **Permissions** → **Bucket policy** → cole:
   ```json
   {
     "Version": "2012-10-17",
     "Statement": [{
       "Sid": "PublicReadGetObject",
       "Effect": "Allow",
       "Principal": "*",
       "Action": "s3:GetObject",
       "Resource": "arn:aws:s3:::conduzir-frontend-seu-nome/*"
     }]
   }
   ```
   (troque `conduzir-frontend-seu-nome` pelo nome real do seu bucket)

### 3.6 — Acessar
Volte em **Properties → Static website hosting** e copie a URL (algo como `http://conduzir-frontend-seu-nome.s3-website-us-east-1.amazonaws.com`).

Abra essa URL — **sua aplicação está no ar, publicamente!** 🌍🎉

---

## 🧯 Se algo der errado (não se preocupe!)

| Problema | Causa provável |
|---|---|
| Backend não conecta no banco | Confira o Security Group do RDS (Passo 1) e as variáveis de ambiente (Passo 2.3) |
| Frontend abre mas não busca dados | Confira se trocou a `baseURL` no `client.ts` e gerou o build de novo |
| Erro 403 no S3 | Confira a Bucket Policy (Passo 3.5) |
| Custos aparecendo | Confira se escolheu "Free tier" no RDS e instâncias pequenas no EB |

---

## 🧹 Importante: desligar depois (para não gerar custo!)

Quando terminar de apresentar/testar:
1. **Elastic Beanstalk** → Actions → **Terminate environment**
2. **RDS** → Actions → **Delete**
3. **S3** → Delete bucket

---

## 🎤 Frase para a apresentação

> "Publiquei a aplicação na AWS: o backend Spring Boot roda no Elastic Beanstalk, conectado a um banco RDS PostgreSQL, e o frontend React é servido como site estático no S3. Toda a configuração sensível (credenciais do banco, chave de API) é injetada por variáveis de ambiente, sem nada exposto no código."
