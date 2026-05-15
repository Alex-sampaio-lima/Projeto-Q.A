# 📚 Gerenciador de Biblioteca Pessoal

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?style=for-the-badge&logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular)
![CI](https://github.com/Alex-sampaio-lima/Projeto-Q.A/actions/workflows/ci.yml/badge.svg)

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

### Frontend
- **Angular 21**: Framework para a interface do usuário.
- **RxJS**: Programação reativa.
- **Vitest 4**: Framework de testes unitários.
- **happy-dom**: Ambiente DOM leve para execução dos testes.
- **Prettier**: Padronização de código.

### Banco de Dados
- **MongoDB**: Banco de dados orientado a documentos.

### CI/CD
- **GitHub Actions**: Pipeline de integração contínua com 4 jobs independentes.

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- **JDK 21** instalado.
- **Node.js 20+** e **npm** instalados.
- **MongoDB** rodando localmente (porta padrão 27017).

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

- **Autenticação**: Registro e login de usuários com segurança.
- **Catálogo de Livros**: Cadastro completo de livros (ISBN, Título, Autor, Ano, etc).
- **Gestão de Acervo**: Visualização, edição e exclusão de livros da coleção pessoal.
- **Busca Avançada**: Filtros por autor, título ou categoria.
- **Dashboard**: Visão geral da biblioteca e estatísticas de leitura.

---

## 🧪 Testes

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

### Frontend

**Rodar os testes com Vitest:**
```bash
cd frontend/PI-Gerenciador-de-Biblioteca-Pessoal
npm test
```

Os testes utilizam `happy-dom` como ambiente DOM e são configurados via `vitest.config.ts`.

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
| `backend-test` | Executa os testes unitários com MongoDB (mongo:8) |
| `backend-coverage` | Gera e faz upload do relatório JaCoCo como artefato |
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
│       │   └── test/           # Testes unitários e de integração
│       └── pom.xml             # Dependências e configuração do JaCoCo
├── frontend/
│   └── PI-Gerenciador-de-Biblioteca-Pessoal/  # Interface Angular
│       ├── src/
│       │   └── app/            # Componentes, serviços e testes (.spec.ts)
│       ├── vitest.config.ts    # Configuração do Vitest
│       └── angular.json        # Configuração do Angular CLI
├── RTM.md                      # Matriz de Rastreabilidade de Requisitos
├── Diagrama UML.md             # Diagramas UML (Sequência)
└── README.md                   # Documentação principal
```

---

## 👥 Desenvolvedores
- **Alexsander Sampaio Lima**
- **Ana Julia Ferreira Lima**
- **Sthephany Viana da Silva**

---
*Este projeto foi desenvolvido com foco em práticas de Qualidade de Software (Q.A).*
