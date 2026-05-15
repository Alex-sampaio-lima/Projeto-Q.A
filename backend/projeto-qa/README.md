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

---

## 📦 Pré-requisitos

- **JDK 21** ou superior instalado
- **MongoDB** rodando em `localhost:27017`

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

### Cobertura atual:

| Pacote | Cobertura de Linhas |
|---|---|
| `config` | ~100% |
| `projeto_qa` (main) | ~37% |
| `Controller` | ~4% |
| `Service` | ~2% |
| `entitles` | ~0% |
| **Total** | **~11%** |

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
