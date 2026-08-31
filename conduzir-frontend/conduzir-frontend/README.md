# ⚛️ Conduzir — Frontend

> **Guiando o seu negócio pelo melhor caminho.**

Interface web da aplicação **Conduzir**, para gestão de veículos e concessionárias. Consome a API REST do backend Spring Boot.

---

## 🛠️ Tecnologias

- **React 18 + TypeScript**
- **Vite** (build e dev server)
- **TanStack Query (React Query)** — busca e cache de dados da API
- **React Hook Form** — formulários
- **Zod** — validação (incluindo CNPJ com dígitos verificadores)
- **React Router** — navegação entre telas
- **Axios** — cliente HTTP

---

## 🚀 Como Executar

Requisitos: **Node.js 18+** e o **backend rodando** em `http://localhost:8080`.

```bash
# 1. Instalar as dependências
npm install

# 2. Rodar em modo desenvolvimento
npm run dev
```

Acesse: **http://localhost:5173**

> O Vite faz **proxy** de `/api` para `http://localhost:8080`, evitando problemas de CORS.

Para gerar a versão de produção:

```bash
npm run build
```

---

## 📱 Funcionalidades

- ✅ **CRUD de Veículos** (listar, cadastrar, editar, excluir)
- ✅ **CRUD de Concessionárias**
- ✅ **Associação** de veículo a concessionária (via `<select>`)
- ✅ **Busca automática de endereço via ViaCEP** ao digitar o CEP
- ✅ **Validações client-side** com Zod (campos obrigatórios, CNPJ, enum de combustível)
- ✅ **Feedback visual**: loading, tratamento de erros, toasts de sucesso/erro
- ✅ **Navegação entre telas** com React Router
- ✅ **Responsividade** (layout adaptável para mobile)

---

## 🗂️ Estrutura

```
src/
├── api/            # Chamadas à API (client Axios + funções por recurso)
├── components/     # Layout, Toast, estados de UX
├── pages/          # Telas (lista e formulário de cada recurso)
├── schemas/        # Validações Zod (veículo e concessionária)
├── types/          # Tipos TypeScript compartilhados
├── App.tsx         # Rotas (React Router)
├── main.tsx        # Ponto de entrada (providers)
└── index.css       # Identidade visual Conduzir
```

---

## 🎨 Identidade

- **Nome:** Conduzir
- **Slogan:** *Guiando o seu negócio pelo melhor caminho.*
- **Cores:** Azul `#1e5a9e` / Verde `#2ec4a6`
