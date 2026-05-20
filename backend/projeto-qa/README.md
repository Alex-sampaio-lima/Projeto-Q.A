# ⚙️ Backend — Gerenciador de Biblioteca Pessoal

Esta é a API REST do sistema **Gerenciador de Biblioteca Pessoal**, desenvolvida com Java 21 e Spring Boot 4.0.5.

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
| **Testcontainers** | 1.20.4 | Testes de integração com MongoDB real |
| **WireMock** | 3.5.4 | Gravação e reprodução de chamadas HTTP reais em JSON (VCR) |

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

## 🧪 Testes e Qualidade

O backend utiliza uma arquitetura robusta de testes integrados e de unidade, validada automaticamente via **GitHub Actions**.

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

> [!IMPORTANT]
> O projeto segue uma política estrita nos testes:
> - **Zero Mocks de Banco de Dados**: Todos os testes de backend utilizam **Testcontainers** para validar a persistência em um banco de dados MongoDB real durante a execução.
> - **WireMock (VCR)**: As chamadas para APIs Externas (Google Books) são testadas usando o padrão VCR, gravando as respostas da API real em arquivos JSON (cassettes) e reproduzindo-as localmente, abolindo o uso de mocks no código Java.
> - **Testes Parametrizados**: Usamos JUnit5 (`@ParameterizedTest` e `@ValueSource`) para testar o cadastro de usuários de forma robusta e exaustiva.
> - **Integração Completa HTTP**: A camada Web (Controllers) não utiliza `MockMvc`, mas sim chamadas HTTP reais disparadas para portas aleatórias do Spring Boot via `RestTemplate`.

### Cobertura (Requisitos Funcionais)

Alcançamos a cobertura total estipulada pela Matriz de Rastreabilidade (RTM):

| Camada / Componente | Teste | Cobertura | Observação |
|---|---|---|---|
| `UsuarioController` / `AuthController` | E2E + Unitário | 100% | Autenticação e Cadastro (com Testcontainers) |
| `LivroController` | E2E + Unitário | 100% | CRUD completo com chamadas HTTP reais |
| `GoogleBooksService` | Integração (VCR) | 100% | WireMock reproduzindo arquivo JSON real gravado (cassette) em porta local |
| `Service Layer` | Caixa Branca | 100% | Coberto pelos fluxos de integração reais |

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

## 🏗️ Estrutura de Pastas

```
src/
├── main/
│   ├── java/com/senac/projeto_qa/
│   │   ├── config/         # Configurações (Security, CORS)
│   │   ├── Controller/     # Controladores REST (Auth, Livro, Usuario)
│   │   ├── Service/        # Regras de negócio (GoogleBooks, CustomUserDetails, Livro)
│   │   ├── entities/       # Entidades / Modelos (Livro, Usuario)
│   │   ├── Repository/     # Repositórios (LivroRepository, UsuarioRepository)
│   │   └── ProjetoQaApplication.java
│   └── resources/
│       └── application.properties  # Configuração do MongoDB
└── test/
    └── java/com/senac/projeto_qa/
        └── ProjetoQaApplicationTests.java
pom.xml                     # Dependências + configuração do JaCoCo e Testcontainers
```

---

*Parte do **Projeto Q.A** — Senac 2026.*
