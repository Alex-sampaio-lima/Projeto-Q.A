# 📚 Gerenciador de Biblioteca Pessoal

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?style=for-the-badge&logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular)
![CI](https://github.com/Alex-sampaio-lima/Projeto-Q.A/actions/workflows/ci.yml/badge.svg)
![SonarCloud](https://img.shields.io/badge/SonarCloud-100%25-brightgreen?style=for-the-badge&logo=sonarcloud)

Este projeto é um sistema completo para o gerenciamento de uma biblioteca pessoal, permitindo que usuários organizem seus livros, acompanhem leituras e gerenciem sua coleção de forma eficiente. Desenvolvido como parte do **Projeto Q.A (Qualidade de Software)** no Senac.

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21**: Linguagem principal.
- **Spring Boot 4.0.5**: Framework para construção de APIs REST.
- **Spring Data MongoDB**: Integração com banco de dados NoSQL.
- **Spring Security**: Controle de autenticação e autorização.
- **Lombok**: Redução de código boilerplate.
- **Maven**: Gerenciador de dependências e build.
- **JaCoCo 0.8.12**: Ferramenta de análise de cobertura de código.
- **Testcontainers**: Para testes de integração com MongoDB real.
- **WireMock (VCR)**: Utilizado para mockar e gravar chamadas HTTP da API do Google Books.

### Frontend
- **Angular 21**: Framework para a interface do usuário.
- **RxJS**: Programação reativa.
- **Vitest 4**: Framework de testes unitários de alta performance.
- **happy-dom**: Ambiente DOM leve para execução dos testes.
- **Prettier**: Padronização de código.

### Banco de Dados
- **MongoDB**: Banco de dados orientado a documentos.

### CI/CD
- **GitHub Actions**: Pipeline de integração contínua com 4 jobs independentes validando Frontend e Backend em paralelo.

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- **JDK 21** instalado.
- **Node.js 20+** e **npm** instalados.
- **MongoDB** rodando localmente (porta padrão 27017) ou via Docker.
- **Docker** (necessário para rodar os testes de integração do backend com Testcontainers).

### 1. Clonar o Repositório
```bash
git clone https://github.com/Alex-sampaio-lima/Projeto-Q.A.git
cd Projeto-Q.A
```

### 2. Executar o Backend
```bash
cd backend/projeto-qa
./mvnw spring-boot:run
```
O servidor backend estará disponível em `http://localhost:8080`.

### 3. Executar o Frontend
```bash
cd frontend/PI-Gerenciador-de-Biblioteca-Pessoal
npm install
npm start
```
O frontend estará disponível em `http://localhost:4200`.

---

## ✨ Funcionalidades Principais

- **Autenticação**: Registro e login de usuários com segurança e criptografia de senhas.
- **Catálogo de Livros**: Cadastro completo de livros (ISBN, Título, Autor, Ano, etc) integrado com API externa do Google Books.
- **Gestão de Acervo**: Visualização, edição, verificação de detalhes e exclusão de livros da coleção pessoal.

---

## 🧪 Testes e Qualidade

O projeto foca fortemente na qualidade do software, implementando testes nos diferentes níveis.

### Backend

**Rodar somente os testes:**
```bash
cd backend/projeto-qa
./mvnw clean test
```

**Rodar testes + gerar relatório de cobertura JaCoCo:**
```bash
cd backend/projeto-qa
./mvnw clean verify
```

O relatório HTML será gerado em:
```
backend/projeto-qa/target/site/jacoco/index.html
```
Abra este arquivo no navegador para visualizar a cobertura linha a linha.

> [!NOTE]
> Os testes de backend não utilizam **Mocks de Banco de Dados**. Eles utilizam **Testcontainers** (para subir um MongoDB real temporário) garantindo a auditoria completa. Além disso, utilizamos **WireMock (VCR)** para simular chamadas HTTP para o Google Books e **Testes Parametrizados** com `@ParameterizedTest` para varrer cenários robustos no cadastro de usuários.

#### 🐳 Solução de Problemas com Docker (Testcontainers) no Windows

Se você encontrar o erro `Could not find a valid Docker environment` ou `BadRequestException (Status 400)` ao rodar os testes do backend, faça o seguinte:

1. **Ajuste o Contexto do Docker**:
   No terminal, mude para o contexto padrão:
   ```bash
   docker context use default
   ```
2. **Configure a Versão da API**:
   Crie um arquivo chamado **`.docker-java.properties`** na pasta do seu usuário do Windows (ex: `C:\Users\SEU_USUARIO`) e insira:
   ```properties
   api.version=1.44
   ```
   Depois disso, execute `./mvnw clean test` novamente.

### Frontend

O frontend utiliza testes unitários construídos com **Vitest** e **happy-dom**, testando componentes vitais como o *DetalheLivroComponent*.

**Rodar os testes com Vitest:**
```bash
cd frontend/PI-Gerenciador-de-Biblioteca-Pessoal
npm test
```

---

## ⚙️ Pipeline CI/CD (GitHub Actions)

O pipeline é executado automaticamente a cada `push` ou `pull_request` em qualquer branch.

```
backend-build ──→ backend-test ──→ backend-coverage (JaCoCo)
frontend-test  (executa em paralelo com os jobs de backend)
```

| Job | Descrição |
|---|---|
| `backend-build` | Compila o projeto Spring Boot sem rodar testes |
| `backend-test` | Executa os testes unitários e E2E com MongoDB (Testcontainers) |
| `backend-coverage` | Gera o JaCoCo, envia análise pro SonarQube e publica o artefato |
| `frontend-test` | Instala dependências, faz build e executa os testes Vitest |

### Baixar o relatório JaCoCo do CI

1. Acesse a aba **Actions** no GitHub
2. Clique no run mais recente
3. Na seção **Artifacts**, baixe **`jacoco-report`**
4. Extraia o `.zip` e abra o `index.html`

---

## 📁 Estrutura do Projeto

```
Projeto-Q.A/
├── .github/
│   └── workflows/
│       └── ci.yml              # Pipeline de CI/CD
├── backend/
│   └── projeto-qa/             # API Spring Boot
│       ├── src/
│       │   ├── main/           # Código fonte Java
│       │   └── test/           # Testes unitários, e2e (Testcontainers) e wiremock
│       └── pom.xml             # Dependências e configuração do JaCoCo
├── frontend/
│   └── PI-Gerenciador-de-Biblioteca-Pessoal/  # Interface Angular
│       ├── src/
│       │   └── app/            # Componentes (ex: DetalheLivro), serviços e testes (.spec.ts)
│       ├── vitest.config.ts    # Configuração do Vitest
│       └── package.json        # Dependências NPM
├── RTM.md                      # Matriz de Rastreabilidade de Requisitos
├── Diagrama UML.md             # Diagramas UML (Classe e Sequência)
└── README.md                   # Documentação principal
```

---

## 👥 Desenvolvedores
- **Alexsander Sampaio Lima**
- **Ana Julia Ferreira Lima**
- **Sthephany Viana da Silva**
- **Thalyta Cristina Santana Silva**

---
*Este projeto foi desenvolvido com foco em práticas de Qualidade de Software (Q.A).*
