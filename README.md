# 📚 Gerenciador de Biblioteca Pessoal

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?style=for-the-badge&logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular)

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

### Frontend
- **Angular 21**: Framework para a interface do usuário.
- **RxJS**: Programação reativa.
- **Vitest**: Framework de testes unitários.
- **Prettier**: Padronização de código.

### Banco de Dados
- **MongoDB**: Banco de dados orientado a documentos.

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- **JDK 21** instalado.
- **Node.js** e **npm** instalados.
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
Para rodar os testes do Spring Boot:
```bash
./mvnw test
```

### Frontend
Para rodar os testes com Vitest:
```bash
npm test
```

---

## 📁 Estrutura do Projeto

```
Projeto-Q.A/
├── backend/            # API Spring Boot
│   └── projeto-qa/     # Código fonte Java, configurações e pom.xml
├── frontend/           # Interface Angular
│   └── PI-Gerenciador-de-Biblioteca-Pessoal/ # Componentes, serviços e assets
├── RTM.md              # Matriz de Rastreabilidade de Requisitos
├── DIAGRAMAS.md        # Diagramas UML (Sequência)
└── README.md           # Documentação principal
```

---

## 👥 Desenvolvedores
- **Alex Sampaio Lima**
- **Ana Julia Ferreira Lima**
- **Sthephany Viana da Silva**

---
*Este projeto foi desenvolvido com foco em práticas de Qualidade de Software (Q.A).*
