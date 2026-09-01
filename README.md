# 🚗 Conduzir - Gestão de Veículos

> **"Guiando o seu negócio pelo melhor caminho"**

Sistema Full Stack para **gestão de veículos**, desenvolvido como desafio técnico para a vaga de **Full Stack Developer Júnior** (projeto EcoPartners / cliente Telefónica-Vivo).

O projeto foi construído do zero, cobrindo backend, frontend, integração com APIs externas, containerização, documentação de API e deploy em produção na AWS.

---

## 🌐 Aplicação em Produção

| Camada | Link | Status |
|---|---|---|
| **Frontend (React)** | http://conduzir-frontend.s3-website-us-east-1.amazonaws.com/veiculos | ✅ Online |
| **Backend (Spring Boot)** | http://desafio-conduzir-env.eba-ipu5dsfz.us-east-1.elasticbeanstalk.com | ✅ Online |
| **Documentação da API (Swagger)** | http://desafio-conduzir-env.eba-ipu5dsfz.us-east-1.elasticbeanstalk.com/swagger-ui.html | ✅ Disponível |

> ⚠️ **Observação:** tanto o Frontend (S3 Static Website Hosting) quanto o Backend (Elastic Beanstalk sem certificado configurado) respondem apenas via **HTTP** (não HTTPS). Ao acessar, utilize os links exatamente como estão, sem adicionar `https://`. A migração para **HTTPS** (CloudFront no frontend / Load Balancer com certificado no backend) está listada em [Melhorias Futuras](#-melhorias-futuras).
>
> 💡 Este é um ambiente de **avaliação técnica/portfólio** e pode ser desativado após o período de análise, para controle de custos na AWS.

### ✅ Validação em Produção

O fluxo completo de **CRUD (Create, Read, Update, Delete)** foi testado diretamente no ambiente publicado (frontend no S3 se comunicando com o backend no Elastic Beanstalk), confirmando que:

- ✅ Listagem de veículos carrega corretamente
- ✅ Cadastro de novo veículo funciona de ponta a ponta (Frontend → CORS → Backend → Banco de dados)
- ✅ CORS configurado corretamente entre os domínios do S3 e do Elastic Beanstalk

---

## 📋 Sobre o Projeto

O **Conduzir** é uma aplicação que permite o cadastro, consulta, atualização e remoção de veículos, com recursos adicionais como:

- ✅ Preenchimento automático de endereço via **API ViaCEP**
- ✅ Geração de recomendações/resumos inteligentes via **API Gemini (Google AI)**, com fallback local em caso de indisponibilidade do serviço
- ✅ Documentação interativa da API via **Swagger**
- ✅ Persistência de dados com **PostgreSQL** (produção) e **H2** (testes locais)
- ⚠️ `Dockerfile` disponível no repositório (containerização planejada), porém não validado em ambiente local devido a restrição corporativa de acesso ao Docker Desktop (política de TI)

---

## 🛠️ Tecnologias Utilizadas

**Backend**
- Java 17
- Spring Boot (Web, Data JPA, Validation)
- Maven
- PostgreSQL / H2 Database
- Swagger / OpenAPI
- Integração com API Gemini (Google AI) e ViaCEP

**Frontend**
- React + TypeScript
- Vite
- Consumo de API REST via Axios/Fetch

**Infraestrutura / DevOps**
- Docker (`Dockerfile` incluído no repositório)
- AWS Elastic Beanstalk (backend)
- AWS S3 Static Website Hosting (frontend)
- Git / GitHub

---

## 📁 Estrutura do Projeto

```text
DesafioConduzir/
├── gestao-veiculos-backend/
│   └── desafio/                # Código-fonte do backend (pom.xml aqui)
│       ├── src/main/java/...
│       │   ├── controller/
│       │   ├── service/
│       │   ├── repository/
│       │   ├── dto/
│       │   └── model/
│       └── src/main/resources/
│           └── application.properties
│
└── conduzir-frontend/
    └── conduzir-frontend/       # Código-fonte do frontend (package.json aqui)
        ├── src/
        │   ├── components/
        │   ├── pages/
        │   └── services/
        └── public/
```

---

## ▶️ Como Executar Localmente

### Pré-requisitos
- Java 17+
- Node.js 18+
- Maven
- Git

### Backend

```bash
cd gestao-veiculos-backend/desafio
mvn spring-boot:run
```

A API sobe por padrão em:
```text
http://localhost:8080
```

Documentação Swagger disponível em:
```text
http://localhost:8080/swagger-ui.html
```

### Frontend

```bash
cd conduzir-frontend/conduzir-frontend
npm install
npm run dev
```

A aplicação sobe por padrão em:
```text
http://localhost:5173
```

---

## 🐞 Desafios Técnicos e Erros Corrigidos Durante o Desenvolvimento

Um dos objetivos deste desafio, além de "fazer funcionar", foi **entender profundamente o código** e ser capaz de identificar e corrigir problemas reais — simulando o dia a dia de um desenvolvedor júnior em produção. Abaixo está o histórico dos principais problemas enfrentados e como foram resolvidos:

### 1. Falha na integração com a API Gemini (503 Service Unavailable)
- **Sintoma:** logs do backend indicavam `Falha ao consultar Gemini, usando resumo local. Motivo: 503 Service Unavailable`.
- **Causa:** instabilidade/indisponibilidade temporária do serviço externo da Google AI.
- **Solução:** implementado tratamento de exceção no `RecomendacaoService`, garantindo que, quando a API Gemini falha, o sistema **não quebra** — ele automaticamente gera um resumo local como fallback, mantendo a aplicação funcional (resiliência a falhas externas).

### 2. Erro 502 Bad Gateway no deploy do backend (AWS Elastic Beanstalk)
- **Sintoma:** ao acessar a URL do backend publicado, a aplicação retornava `502 Bad Gateway`.
- **Causa:** comum em deploys do Elastic Beanstalk logo após o upload do `.jar`, geralmente por tempo de inicialização da aplicação, porta incorreta configurada, ou variáveis de ambiente ausentes.
- **Solução:** revisão dos logs do ambiente no console do Elastic Beanstalk, validação da porta (`8080`) exposta pela aplicação Spring Boot e reconfiguração das variáveis de ambiente necessárias para conexão com o banco de dados.

### 3. Frontend publicado exibindo tela em branco / arquivo incorreto
- **Sintoma:** dúvida se o arquivo gerado pelo build (`index.html`) estava correto, chegando a ser confundido com um "documento de erro".
- **Causa:** o `index.html` gerado pelo Vite (`dist/index.html`) é o próprio ponto de entrada da aplicação React — não um arquivo de erro.
- **Solução:** esclarecido o papel do `index.html` dentro da pasta `dist/`, e garantido que o upload para o S3 fosse feito a partir do **conteúdo da pasta `dist`**, e não da raiz do projeto.

### 4. Site acessível via HTTP, mas não via HTTPS
- **Sintoma:** o link do frontend não abria em alguns dispositivos móveis, mas funcionava normalmente em navegador desktop (modo anônimo).
- **Causa:** o **Amazon S3 Static Website Hosting** não fornece certificado SSL — ele só responde em `http://`. Ao tentar acessar com `https://`, o navegador não encontra um certificado válido e bloqueia o acesso.
- **Solução (aplicada):** identificado que removendo o `s` de `https://` o site funciona normalmente.
- **Solução definitiva (planejada):** configurar **Amazon CloudFront** na frente do bucket S3 para habilitar HTTPS de forma nativa (ver [Melhorias Futuras](#-melhorias-futuras)).

### 5. Dúvida sobre a versão da política do bucket S3 (`"Version": "2012-10-17"`)
- **Contexto:** ao configurar a *bucket policy* para tornar o site público, surgiu a dúvida sobre o campo `"Version": "2012-10-17"`.
- **Esclarecimento:** trata-se da **versão da linguagem de política da AWS IAM** (não uma data do projeto) — é o valor padrão e obrigatório utilizado pela AWS desde 2012 para o schema de policies, e deve ser mantido inalterado.

### 6. Logo/imagem não aparecendo após o deploy do frontend
- **Sintoma:** após publicar a nova versão no S3, a logo do sistema não era exibida.
- **Causa:** cache do navegador servindo a versão anterior do `index.html`/assets, e/ou arquivo de imagem não incluído no upload mais recente.
- **Solução:** novo upload garantindo a inclusão de todos os assets estáticos (`dist/assets`) e limpeza de cache no navegador para validar a atualização.

### 7. Organização do repositório Git antes da entrega
- **Sintoma:** necessidade de remover arquivos sensíveis/desnecessários (builds, dependências, configs locais) do repositório antes da entrega final.
- **Causa:** arquivos como `node_modules/`, `target/` e configurações locais haviam sido versionados por engano.
- **Solução:** limpeza do repositório com `.gitignore` adequado, remoção dos arquivos do rastreamento do Git (mantendo-os localmente) e novo commit/push com o histórico organizado.

### 8. Restrição corporativa de acesso ao Docker Desktop
- **Sintoma:** não foi possível executar/validar o `Dockerfile` localmente.
- **Causa:** a máquina de trabalho é corporativa (Accenture) e possui **BeyondTrust** bloqueando a instalação/execução do Docker Desktop, por política interna de segurança de TI.
- **Solução adotada:** o `Dockerfile` foi mantido no repositório (documentando a intenção de containerização e servindo de referência técnica), enquanto a execução do projeto localmente segue via **Maven** (backend) e **NPM** (frontend), conforme instruções da seção [Como Executar Localmente](#️-como-executar-localmente). O deploy em produção não depende de Docker, sendo feito diretamente via **Elastic Beanstalk** (backend, a partir do `.jar`) e **S3** (frontend, a partir do build estático).
- **Aprendizado:** situação real de ambiente corporativo, onde é preciso adaptar o fluxo de desenvolvimento a restrições de infraestrutura sem comprometer a entrega do projeto.

### 9. Erros de build do frontend (TypeScript + Vite)
- **Sintoma:** validação do processo `tsc -b && vite build` antes do deploy.
- **Solução:** build validado localmente com sucesso, gerando os artefatos finais (`dist/index.html`, `assets/*.css`, `assets/*.js`) prontos para publicação no S3.

---

## 🚀 Deploy em Produção (AWS)

| Etapa | Serviço utilizado | Link |
|---|---|---|
| Backend (API Spring Boot) | AWS Elastic Beanstalk | http://desafio-conduzir-env.eba-ipu5dsfz.us-east-1.elasticbeanstalk.com |
| Frontend (React build) | AWS S3 - Static Website Hosting | http://conduzir-frontend.s3-website-us-east-1.amazonaws.com/veiculos |
| Versionamento | GitHub | — |

---

## 🔭 Melhorias Futuras

- [ ] Configurar **Amazon CloudFront** para servir o frontend via **HTTPS**
- [ ] Domínio próprio (ex: `conduzir.app.br`)
- [ ] Pipeline de CI/CD (GitHub Actions) para deploy automatizado
- [ ] Testes automatizados (JUnit no backend / Jest no frontend)
- [ ] Monitoramento e logs centralizados (CloudWatch)

---

## 👨‍💻 Autor

**Leandro Gomes de Brito**
Custom Software Engineering Associate @ Accenture (AABG)

---

## 📄 Licença

Desenvolvido como parte do Desafio Técnico de Desenvolvedor Full Stack / AWS.
