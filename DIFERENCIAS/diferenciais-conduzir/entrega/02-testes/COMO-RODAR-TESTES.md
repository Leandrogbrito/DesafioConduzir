# 🧪 Diferencial 2 — Testes Automatizados

## 🧒 O que são testes?

É como um "inspetor" que aperta cada botão do seu código sozinho e confere se o resultado é o esperado. Se você mudar algo no futuro e quebrar sem querer, o teste **avisa na hora** — antes de virar um problema em produção.

---

## 📁 Passo 1 — Onde colocar os arquivos

No Eclipse (Package Explorer), sua estrutura de pastas tem duas "raízes" de código:

```
desafio
├── src/main/java      <- seu código da aplicação (Controller, Service...)
└── src/test/java      <- os TESTES ficam aqui (mesma estrutura de pacotes)
```

Coloque os 3 arquivos que te entreguei em:

```
src/test/java/com/montadora/gestao/validation/CnpjValidatorTest.java
src/test/java/com/montadora/gestao/service/VeiculoServiceTest.java
src/test/java/com/montadora/gestao/service/ConcessionariaServiceTest.java
```

> 💡 Se as pastas `service` e `validation` não existirem dentro de `src/test/java/com/montadora/gestao/`, crie-as: botão direito → New → Package → digite o nome completo (ex: `com.montadora.gestao.service`).

---

## 🔧 Passo 2 — Confirme as dependências no `pom.xml`

Confira se estas 2 dependências existem (você já deve ter a de teste):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Essa dependência já traz **JUnit 5**, **Mockito** e **AssertJ** juntos — não precisa adicionar mais nada!

---

## ▶️ Passo 3 — Rodar os testes no Eclipse

### Opção A — Rodar TODOS os testes de uma vez
1. Botão direito no projeto **`desafio`**
2. **Run As → Maven test**
3. Veja o resultado no Console

### Opção B — Rodar UM teste específico
1. Abra o arquivo do teste (ex: `CnpjValidatorTest.java`)
2. Botão direito dentro do arquivo → **Run As → JUnit Test**

---

## ✅ Como saber se deu certo

No Console, procure por algo como:

```
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

Se aparecer **`Failures: 0, Errors: 0`** → **todos os testes passaram!** 🎉

Se algum falhar, vai aparecer em vermelho com o nome do teste e o motivo. Me manda o print que eu te ajudo a corrigir.

---

## 💻 Passo 4 — Rodar pelo terminal (bônus)

Se quiser rodar sem abrir o Eclipse (útil para o Docker/CI depois), no cmd, dentro da pasta do projeto:

```
mvnw test
```

(no Windows, use `mvnw.cmd test` se o `mvnw` sozinho não funcionar)

---

## 📊 Bônus — Ver a cobertura de testes (JaCoCo)

Se quiser mostrar visualmente "quantos % do código está testado", adicione isso no `pom.xml`, dentro de `<build><plugins>`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

Depois de rodar `mvn test`, abra no navegador:
```
target/site/jacoco/index.html
```

Vai mostrar um relatório colorido (verde = testado, vermelho = não testado). Ótimo para print na apresentação! 📈

---

## 🎤 Frase para a apresentação

> "Implementei testes unitários com JUnit 5 e Mockito, cobrindo as regras de negócio mais críticas: validação de CNPJ, tratamento de erro 404 e a integração com o ViaCEP, usando mocks para isolar cada camada."
