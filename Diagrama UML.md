# 📊 Diagramas UML do Sistema

Este documento contém os diagramas de sequência e classes que descrevem a estrutura e o fluxo de interação entre os componentes do sistema **Gerenciador de Biblioteca Pessoal**.

---

## 1. Diagramas de Classe

### A) Backend (Spring Boot)

Estrutura principal das Entidades e Relacionamentos no Backend.

```mermaid
classDiagram
    class Usuario {
        +String id
        +String nome
        +String email
        +String senha
        +String confirmarSenha
    }

    class Livro {
        +String id
        +String titulo
        +String autor
        +String isbn
        +int anoPublicacao
        +String status
        +int nota
        +String usuarioId
    }

    class LivroRepository {
        <<interface>>
        +findByUsuarioId(String usuarioId) List~Livro~
    }

    class UsuarioRepository {
        <<interface>>
        +findByEmail(String email) Optional~Usuario~
    }

    class LivroService {
        +salvar(Livro livro) Livro
        +findByUsuarioId(String usuarioId) List~Livro~
        +buscarPorIsbnGoogleBooks(String isbn) LivroDTO
    }

    class UsuarioService {
        +registrar(Usuario usuario) Usuario
    }

    Usuario "1" --> "*" Livro : Possui
    LivroService ..> LivroRepository : Usa
    UsuarioService ..> UsuarioRepository : Usa
```

### B) Frontend (Angular)

Estrutura principal de Modelos e Serviços no Frontend.

```mermaid
classDiagram
    class UsuarioModel {
        +string id
        +string nome
        +string email
        +string token
    }

    class LivroModel {
        +string id
        +string titulo
        +string autor
        +string isbn
        +number anoPublicacao
        +string status
        +number nota
    }

    class AuthService {
        +login(email, senha) Observable
        +registrar(usuario) Observable
        +logout() void
        +getUsuarioLogado() UsuarioModel
    }

    class LivroService {
        +getLivros() Observable
        +getLivroById(id) Observable
        +salvarLivro(livro) Observable
        +atualizarLivro(id, livro) Observable
        +deletarLivro(id) Observable
    }

    class DetalheLivroComponent {
        -LivroService livroService
        +LivroModel livro
        +ngOnInit()
    }
    
    class FormularioLivroComponent {
        -LivroService livroService
        +salvar()
    }

    DetalheLivroComponent ..> LivroService : Consome
    FormularioLivroComponent ..> LivroService : Consome
    AuthService ..> UsuarioModel : Retorna
    LivroService ..> LivroModel : Manipula
```

---

## 2. Diagramas de Sequência (Fluxos)

### 2.1 Fluxo de Autenticação (Login)

O usuário insere suas credenciais. O frontend envia uma requisição com **Basic Auth** ao backend, que valida via Spring Security e retorna o perfil do usuário.

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as LoginComponent (Angular)
    participant Guard as AuthGuard
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Usuario->>Front: Insere e-mail e senha
    Front->>Back: GET /auth/me (Basic Auth Header)
    Back->>DB: Busca usuário por e-mail
    DB-->>Back: Retorna dados do usuário (senha criptografada)

    alt Credenciais Válidas (BCrypt match)
        Back-->>Front: 200 OK (dados do usuário)
        Front->>Front: Salva credenciais no localStorage (Base64)
        Front->>Guard: Rota /livros (canActivate)
        Guard-->>Front: Acesso permitido
        Front->>Usuario: Redireciona para lista de livros
    else Credenciais Inválidas
        Back-->>Front: 401 Unauthorized
        Front->>Usuario: Exibe mensagem de erro
    end
```

### 2.2 Fluxo de Cadastro de Usuário (Registro)

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as RegistroComponent (Angular)
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Usuario->>Front: Preenche nome, e-mail e senha
    Front->>Back: POST /usuarios (nome, email, senha, confirmarSenha)

    Back->>Back: Valida campos (@Valid / DTO)
    Back->>DB: Verifica se e-mail já existe

    alt E-mail disponível
        Back->>Back: Criptografa senha (BCrypt)
        Back->>DB: Salva novo usuário
        DB-->>Back: Confirma persistência
        Back-->>Front: 201 Created
        Front->>Usuario: Redireciona para Login
    else E-mail já cadastrado
        Back-->>Front: 400 Bad Request
        Front->>Usuario: Exibe mensagem de erro
    end
```

### 2.3 Fluxo de Cadastro de Livro (com Wiremock/VCR)

Descreve como um novo livro é adicionado à coleção pessoal, e os testes de API integrados.

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as FormularioLivroComponent (Angular)
    participant Interceptor as AuthInterceptor
    participant Back as Backend (Spring Boot)
    participant Ext as Google Books API (WireMock)
    participant DB as MongoDB

    Usuario->>Front: Preenche formulário (título, autor, ISBN...)
    Front->>Interceptor: Requisição HTTP
    Interceptor->>Interceptor: Adiciona Basic Auth Header ao request
    Interceptor->>Back: POST /livros (Livro JSON + Auth Header)

    Note over Back: Spring Security valida as credenciais
    
    opt Consulta ISBN para autocompletar
        Back->>Ext: GET /volumes?q=isbn:{isbn}
        Ext-->>Back: Retorna dados do Livro JSON (Mockado no Teste)
    end

    Back->>Back: Associa livro ao usuário autenticado (getUsuarioLogado)
    Back->>DB: Salva documento do livro
    DB-->>Back: Confirma persistência

    Back-->>Front: 201 Created (livro salvo)
    Front->>Usuario: Atualiza lista e exibe mensagem de sucesso
```

### 2.4 Fluxo de Listagem de Livros

Descreve a recuperação segura dos livros do usuário autenticado.

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as ListaLivrosComponent (Angular)
    participant Interceptor as AuthInterceptor
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Usuario->>Front: Acessa página de livros
    Front->>Interceptor: GET /livros/meusLivros
    Interceptor->>Interceptor: Adiciona Basic Auth Header
    Interceptor->>Back: GET /livros/meusLivros (Auth Header)

    Note over Back: Extrai usuário autenticado do contexto de segurança

    Back->>DB: Busca livros do usuário (findByUsuarioId)
    DB-->>Back: Retorna lista de livros
    Back-->>Front: 200 OK (array de livros)
    Front->>Usuario: Renderiza lista na tela
```

### 2.5 Fluxo do Pipeline de CI/CD

Descreve a sequência de execução automática do GitHub Actions a cada push.

```mermaid
sequenceDiagram
    participant Dev as Desenvolvedor
    participant GH as GitHub
    participant Build as Job: backend-build
    participant Test as Job: backend-test
    participant Cov as Job: backend-coverage (JaCoCo)
    participant Front as Job: frontend-test (Vitest)

    Dev->>GH: git push
    GH->>Build: Inicia (compile sem testes)
    GH->>Front: Inicia em paralelo (Testes Vitest)

    Build-->>GH: ✅ Build OK
    GH->>Test: Inicia (needs: backend-build)

    Test-->>GH: ✅ Testes E2E (Testcontainers) e Unitários OK
    GH->>Cov: Inicia (needs: backend-test)

    Cov->>Cov: ./mvnw clean verify
    Cov->>GH: Upload jacoco-report e sonar-cloud
    Cov-->>GH: ✅ Coverage OK

    Front-->>GH: ✅ Testes de Componente passando (happy-dom)
```

---

*Documentação atualizada como parte do processo de modelagem de sistema — Projeto Q.A, Senac 2026.*
