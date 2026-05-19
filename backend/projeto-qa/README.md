# ⚙️ Backend — Gerenciador de Biblioteca Pessoal

Esta é a API REST do sistema **Gerenciador de Biblioteca Pessoal**, desenvolvida com Java 21 e Spring Boot 4.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 4.0.5 | Framework REST |
| **Spring Data MongoDB** | — | Persistência NoSQL |
| **Spring Security** | — | Autenticação e autorização |
| **Lombok** | — | Redução de boilerplate |
| **Maven Wrapper** | — | Build e gerenciamento de dependências |
| **JaCoCo** | 0.8.12 | Análise de cobertura de código |
| **SonarQube** | — | Qualidade contínua e análise estática (via CI) |
| **Testcontainers** | 1.19.7 | Testes de integração com MongoDB real |
| **WireMock** | 3.5.4 | Gravação e simulação de chamadas (VCR) para APIs externas |

---

## 📦 Pré-requisitos

- **JDK 21** ou superior instalado
- **MongoDB** rodando em `localhost:27017` (para execução)
- **Docker** (necessário para rodar os testes de integração)

---

## 🛠️ Configuração e Execução

**1. Acesse a pasta do backend:**
```bash
cd backend/projeto-qa
```

**2. Compile e execute:**
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

> **Windows:** use `mvnw.cmd spring-boot:run`

---

## 📡 Endpoints da API

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastro de novo usuário |
| `GET` | `/usuarios` | Autenticação (Basic Auth) |
| `GET` | `/livros` | Listar todos os livros do usuário |
| `POST` | `/livros` | Cadastrar novo livro |
| `GET` | `/livros/{id}` | Buscar livro por ID |
| `GET` | `/livros/busca-google/{isbn}` | Consultar dados de um livro na API do Google Books |
| `PUT` | `/livros/{id}` | Atualizar dados de um livro |
| `DELETE` | `/livros/{id}` | Excluir um livro |

---

## 🧪 Testes

### Executar apenas os testes:
```bash
./mvnw clean test
```

### Executar testes + gerar relatório de cobertura (JaCoCo):
```bash
./mvnw clean verify
```

### Visualizar o relatório JaCoCo:
Após rodar `./mvnw clean verify`, abra o arquivo gerado no navegador:
```
target/site/jacoco/index.html
```

O relatório mostra a cobertura por **pacote**, **classe**, **método** e **linha**, com destaque visual (verde/vermelho).

> [!IMPORTANT]
> O projeto segue uma política de **Zero Mocks de Código** (ex: proibido Mockito). 
> - Todos os testes de backend utilizam **Testcontainers** para validar a persistência em um banco de dados real durante a execução.
> - As chamadas para APIs Externas (Google Books) são testadas utilizando a estratégia **VCR** com o **WireMock**, interceptando e simulando requisições HTTP reais (via Sockets/Rede).
> - Também utilizamos **Testes Parametrizados** com JUnit5 (`@ParameterizedTest` e `@ValueSource`) para validar múltiplos cenários em uma única execução.

---

### 🐳 Solução de Problemas com Docker (Testcontainers)

Se ao rodar `./mvnw clean test` você encontrar o erro `Could not find a valid Docker environment` ou `BadRequestException (Status 400)`, siga estes passos para ajustar a integração com o Docker Desktop no Windows:

#### 1. Verificar o Contexto do Docker
Certifique-se de que o Docker está usando o contexto `default` (que se conecta no Named Pipe correto do Windows):
```bash
# Listar contextos
docker context ls

# Alterar para o contexto padrão
docker context use default
```

#### 2. Corrigir Incompatibilidade de API (Docker Desktop v29+)
Versões modernas do Docker Desktop exigem versões de API mais recentes que as configuradas por padrão em bibliotecas antigas do Testcontainers. Para forçar a versão correta da API:

1. Acesse o seu diretório de usuário (ex: `C:\Users\SEU_USUARIO`).
2. Crie ou edite o arquivo chamado **`.docker-java.properties`** (garanta que não possui extensão `.txt`).
3. Adicione a seguinte linha dentro do arquivo:
   ```properties
   api.version=1.44
   ```
4. Salve o arquivo e execute os testes novamente com `./mvnw clean test`.

---

### Cobertura atual (Instruções):

| Pacote | Cobertura | Observação |
|---|---|---|
| `config` | ~100% | Totalmente coberto |
| `Service` | **~53%** | Coberto por testes de Caixa Branca |
| `Controller` | **~4%** | Coberto por testes de Caixa Preta |
| **Total** | **~35%** | Foco em integração real |

---

## 🏗️ Estrutura de Pastas

```
src/
├── main/
│   ├── java/com/senac/projeto_qa/
│   │   ├── config/         # Configurações (Security, CORS)
│   │   ├── Controller/     # Controladores REST
│   │   ├── Service/        # Regras de negócio
│   │   ├── entitles/       # Entidades / Modelos
│   │   └── ProjetoQaApplication.java
│   └── resources/
│       └── application.properties  # Configuração do MongoDB
└── test/
    └── java/com/senac/projeto_qa/
        └── ProjetoQaApplicationTests.java
pom.xml                     # Dependências + configuração do JaCoCo
```

---

*Parte do **Projeto Q.A** — Senac 2026.*
