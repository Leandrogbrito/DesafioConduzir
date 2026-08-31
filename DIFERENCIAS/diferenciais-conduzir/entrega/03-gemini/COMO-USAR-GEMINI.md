# 🤖 Diferencial 3 — Recomendação Inteligente com Gemini

## 🧒 Como funciona (resumo)

1. O usuário escolhe o que quer (modelo, cor, combustível, ano) — tudo opcional.
2. Nosso **algoritmo em Java** (não é IA, é lógica pura) dá uma "nota" de 0 a 100 para cada carro do estoque, comparando com o pedido.
3. Pegamos o **Top 5** melhores e pedimos para o **Gemini** (IA de verdade do Google) escrever uma explicação em texto, como um vendedor faria.
4. Se você **não configurar** a chave do Gemini, tudo continua funcionando — só que a explicação vem de um texto gerado localmente (sem IA). **A aplicação nunca quebra por causa disso.**

---

## 🔑 Passo 1 — Conseguir a chave gratuita do Gemini

1. Acesse: **https://aistudio.google.com/app/apikey**
2. Faça login com sua conta Google.
3. Clique em **"Create API Key"**.
4. Copie a chave gerada (algo como `AIzaSy...`).

> 💡 O Gemini tem uma camada **gratuita** generosa, ótima para projetos de estudo/desafio.

---

## 📁 Passo 2 — Colocar os arquivos do BACKEND

Coloque cada arquivo na pasta indicada (o comentário no topo de cada um já diz onde):

| Arquivo | Pasta destino |
|---|---|
| `RecomendacaoRequestDTO.java` | `src/main/java/.../dto/` |
| `VeiculoRecomendadoDTO.java` | `src/main/java/.../dto/` |
| `RecomendacaoResponseDTO.java` | `src/main/java/.../dto/` |
| `GeminiService.java` | `src/main/java/.../service/` |
| `RecomendacaoService.java` | `src/main/java/.../service/` |
| `RecomendacaoController.java` | `src/main/java/.../controller/` |

---

## ⚙️ Passo 3 — Configurar a chave

Abra `adicionar-no-application-properties.txt` neste pacote e siga as instruções (é só colar 1 linha no seu `application.properties`).

**Forma mais simples para testar agora:**
```properties
gemini.api.key=AIzaSyXXXXXXXXXXXXXXXXXXXXX
```
(cole sua chave de verdade no lugar do X)

---

## ⚛️ Passo 4 — Colocar os arquivos do FRONTEND

| Arquivo | Pasta destino |
|---|---|
| `recomendacao.ts` | `src/api/` |
| `RecomendacaoPage.tsx` | `src/pages/` |

Depois, siga o `onde-encaixar.txt` para adicionar a rota e o link no menu.

---

## ▶️ Passo 5 — Rodar e testar

1. Reinicie o backend no Eclipse (parar ⏹️ e rodar ▶️ de novo, para ler a nova chave).
2. No frontend, `npm run dev` (se já não estiver rodando).
3. Abra `http://localhost:5173/recomendacao`
4. Preencha, por exemplo:
   - Combustível: FLEX
   - Cor: Vermelho
5. Clique em **"Gerar Recomendação"**.

Você deve ver os veículos ordenados por **% de compatibilidade**, com uma explicação em texto no topo (do Gemini, se a chave estiver certa).

---

## 🧪 Testando também no Swagger (sem precisar do frontend)

`POST /recommendations` com o corpo:
```json
{
  "combustivel": "FLEX",
  "cor": "Vermelho",
  "quantidade": 5
}
```

---

## 🆘 Erros comuns

| Situação | O que fazer |
|---|---|
| `resumoIA` vem com texto genérico e `geradoPorIA: false` | A chave não foi lida — confira o `application.properties` e reinicie o backend |
| Erro 403 do Gemini | Chave inválida ou expirada — gere uma nova em aistudio.google.com |
| Erro 429 do Gemini | Limite gratuito atingido no momento — espere um pouco ou use o resumo local mesmo |
| Nada aparece na tela | Confira se adicionou a rota `/recomendacao` no `App.tsx` |

---

## 🎤 Frase para a apresentação

> "Implementei um sistema de recomendação inteligente: um algoritmo de pontuação por similaridade analisa o estoque com base nas preferências do cliente, e integrei a IA generativa do Google Gemini para traduzir esse resultado em uma explicação natural, como um consultor de vendas faria. A funcionalidade possui fallback local, garantindo que a aplicação funcione mesmo sem a IA configurada."
