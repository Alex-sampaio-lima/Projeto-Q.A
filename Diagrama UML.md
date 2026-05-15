# 📊 Diagramas UML de Sequência

Este documento contém os diagramas de sequência que descrevem o fluxo de interação entre os componentes do sistema **Gerenciador de Biblioteca Pessoal**.

---

## 1. Fluxo de Autenticação (Login)

Este diagrama descreve o processo desde a entrada das credenciais pelo usuário até o recebimento do acesso ao sistema.

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as Frontend (Angular)
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Usuario->>Front: Insere E-mail e Senha
    Front->>Back: POST /auth/login (credentials)
    Back->>DB: Busca usuário por e-mail
    DB-->>Back: Retorna dados do usuário (hash senha)
    
    alt Credenciais Válidas
        Back->>Back: Valida senha (BCrypt)
        Back-->>Front: 200 OK (Token JWT + Perfil)
        Front->>Usuario: Redireciona para Dashboard
    else Credenciais Inválidas
        Back-->>Front: 401 Unauthorized
        Front->>Usuario: Exibe mensagem de erro
    end
```

---

## 2. Fluxo de Cadastro de Livro

Descreve como um novo livro é adicionado à coleção pessoal do usuário.

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as Frontend (Angular)
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Usuario->>Front: Preenche formulário de livro
    Front->>Back: POST /api/books (BookDTO + JWT)
    
    Note over Back: Filtro de Segurança valida o Token
    
    Back->>Back: Associa livro ao ID do usuário autenticado
    Back->>DB: Salva documento do livro
    DB-->>Back: Confirma persistência
    
    Back-->>Front: 201 Created (Livro salvo)
    Front-->>Usuario: Atualiza lista e exibe sucesso
```

---

## 3. Fluxo de Listagem de Livros

Descreve a recuperação segura dos livros pertencentes a um usuário específico.

```mermaid
sequenceDiagram
    participant Front as Frontend (Angular)
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Front->>Back: GET /api/books (JWT Header)
    Note over Back: Extrai UserID do Token
    Back->>DB: findByUsuarioId(userId)
    DB-->>Back: Lista de livros
    Back-->>Front: 200 OK (Array de Livros)
    Front->>Front: Renderiza lista no template
```

---
*Documentação gerada como parte do processo de modelagem de sistema.*
