# 📊 Diagramas UML de Sequência

Este documento contém os diagramas de sequência que descrevem o fluxo de interação entre os componentes do sistema **Gerenciador de Biblioteca Pessoal**.

---

## 1. Fluxo de Autenticação (Login)

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

---

## 2. Fluxo de Cadastro de Usuário (Registro)

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

---

## 3. Fluxo de Cadastro de Livro

Descreve como um novo livro é adicionado à coleção pessoal do usuário autenticado.

```mermaid
sequenceDiagram
    actor Usuario as Usuário
    participant Front as FormularioLivroComponent (Angular)
    participant Interceptor as AuthInterceptor
    participant Back as Backend (Spring Boot)
    participant DB as MongoDB

    Usuario->>Front: Preenche formulário (título, autor, ISBN...)
    Front->>Interceptor: Requisição HTTP
    Interceptor->>Interceptor: Adiciona Basic Auth Header ao request
    Interceptor->>Back: POST /livros (Livro JSON + Auth Header)

    Note over Back: Spring Security valida as credenciais

    Back->>Back: Associa livro ao usuário autenticado (getUsuarioLogado)
    Back->>DB: Salva documento do livro
    DB-->>Back: Confirma persistência

    Back-->>Front: 201 Created (livro salvo)
    Front->>Usuario: Atualiza lista e exibe mensagem de sucesso
```

---

## 4. Fluxo de Listagem de Livros

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

---

## 5. Fluxo do Pipeline de CI/CD

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
    GH->>Front: Inicia em paralelo

    Build-->>GH: ✅ Build OK
    GH->>Test: Inicia (needs: backend-build)

    Test-->>GH: ✅ Testes OK
    GH->>Cov: Inicia (needs: backend-test)

    Cov->>Cov: ./mvnw clean verify
    Cov->>GH: Upload jacoco-report (artefato)
    Cov->>GH: Upload surefire-reports (artefato)
    Cov->>GH: Upload app-jar (artefato)
    Cov-->>GH: ✅ Coverage OK

    Front-->>GH: ✅ 10/10 testes passando
```

---

## 6. Diagramas de Sequência de Teste Unitário (Modelo de Teste de Fluxo)

Esta seção apresenta o modelo de testes unitários sem mocks sob a perspectiva de diagramas de sequência.

### A) Modelo Padrão do Professor (Cenário de Referência)

Este diagrama representa a estrutura exata fornecida no modelo de referência (Calculadora/Validador/Repositório):

```mermaid
sequenceDiagram
    participant Teste as Teste Unitário
    participant Calc as Calculadora
    participant Valid as Validador
    participant Repo as Repositório

    Teste->>Calc: somar(a, b)
    Calc->>Valid: validarEntradas(a, b)
    Valid-->>Calc: true
    Calc->>Calc: executarSoma(a, b)
    Calc->>Repo: registrarOperacao()
    Repo-->>Calc: OK
    Calc-->>Teste: resultado: int
```

### B) Modelo Aplicado ao Nosso Projeto (Cenário Real: Cadastro de Usuário)

Este diagrama demonstra a aplicação prática e literal do modelo do professor no nosso projeto real de Q.A, mapeando a classe de teste exercitando as regras de negócio sem mocks e utilizando exatamente as assinaturas de métodos presentes em `UsuarioService` e `UsuarioServiceTest`:

```mermaid
sequenceDiagram
    participant Teste as Teste Unitário (UsuarioServiceTest)
    participant Service as UsuarioService (Serviço)
    participant Encoder as PasswordEncoder (Validador/Helper)
    participant Repo as UsuarioRepository (Repositório MongoDB)

    Teste->>Service: registrar(usuario)
    
    Service->>Repo: findByEmail(usuario.getEmail())
    Repo-->>Service: Optional.empty()
    
    Note over Service: Valida se usuario.getSenha().equals(usuario.getConfirmarSenha())
    
    Service->>Encoder: encode(usuario.getSenha())
    Encoder-->>Service: senhaCriptografada: String
    
    Service->>Repo: save(usuario)
    Repo-->>Service: salvo: Usuario (com ID gerado)
    
    Service-->>Teste: salvo: Usuario
```

---

*Documentação gerada como parte do processo de modelagem de sistema — Projeto Q.A, Senac 2026.*
