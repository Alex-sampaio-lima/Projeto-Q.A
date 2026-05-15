# ⚙️ Backend - Gerenciador de Biblioteca Pessoal

Esta é a API REST do sistema de Gerenciador de Biblioteca Pessoal, desenvolvida com Java e Spring Boot.

## 🚀 Tecnologias
- **Java 21**
- **Spring Boot 4.0.5**
- **Spring Data MongoDB**
- **Spring Security** (Autenticação JWT/Sessão)
- **Lombok**
- **Maven**

## 📦 Pré-requisitos
- JDK 21+
- MongoDB rodando em `localhost:27017`

## 🛠️ Configuração e Execução

1. Navegue até a pasta do projeto:
   ```bash
   cd backend/projeto-qa
   ```

2. Compile e execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

A API estará disponível em `http://localhost:8080`.

## 📡 Endpoints Principais (Exemplos)
- `POST /auth/register` - Cadastro de novos usuários.
- `POST /auth/login` - Autenticação.
- `GET /api/books` - Listagem de livros do usuário.
- `POST /api/books` - Cadastro de um novo livro.

## 🧪 Testes
Para executar os testes unitários e de integração:
```bash
./mvnw test
```

### Cobertura de Código (JaCoCo)
Após rodar os testes, o relatório de cobertura será gerado em:
`target/site/jacoco/index.html`

Abra este arquivo no navegador para visualizar a cobertura detalhada por classe e método.

## 🏗️ Estrutura de Pastas
- `src/main/java`: Código fonte da aplicação (Controllers, Services, Models, Repositories).
- `src/main/resources`: Arquivos de configuração (`application.properties`).
- `src/test/java`: Testes da aplicação.

---
*Parte do Projeto Q.A - Senac.*
