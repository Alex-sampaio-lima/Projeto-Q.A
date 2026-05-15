# 💻 Frontend — Gerenciador de Biblioteca Pessoal

Este é o cliente web do sistema **Gerenciador de Biblioteca Pessoal**, desenvolvido com Angular 21 e integrado à API REST do backend Spring Boot.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| **Angular** | 21 | Framework principal |
| **TypeScript** | ~5.9 | Linguagem de programação |
| **RxJS** | ~7.8 | Programação reativa / HTTP |
| **Vitest** | ^4 | Framework de testes unitários |
| **happy-dom** | ^17 | Ambiente DOM para os testes |
| **Prettier** | ^3 | Padronização de código |

---

## 📦 Pré-requisitos

- **Node.js** v20 ou superior
- **npm** v10 ou superior
- Backend rodando em `http://localhost:8080`

---

## 🛠️ Configuração e Execução

**1. Instale as dependências:**
```bash
npm install
```

**2. Inicie o servidor de desenvolvimento:**
```bash
npm start
```

A interface estará disponível em `http://localhost:4200`.

---

## 🧪 Testes

O projeto utiliza **Vitest 4** com ambiente **happy-dom** (configurado em `vitest.config.ts`). Seguimos uma política de testes que prioriza a integração real com o backend e banco de dados, sem o uso de Mocks no sistema completo.

**Executar todos os testes (modo CI — sem watch):**
```bash
npm test -- --watch=false
```

**Executar em modo desenvolvimento (com recarga automática):**
```bash
npm test
```

### Resultado atual: 10/10 testes passando ✅

| Arquivo de Teste | Componente Testado |
|---|---|
| `app.spec.ts` | Componente raiz (`App`) |
| `login.spec.ts` | `LoginComponent` |
| `registro.spec.ts` | `RegistroComponent` |
| `navbar.spec.ts` | `Navbar` |
| `rodape.spec.ts` | `Rodape` |
| `formulario-livro.spec.ts` | `FormularioLivroComponent` |
| `lista-livros.spec.ts` | `ListaLivrosComponent` |
| `detalhe-livro.spec.ts` | `DetalheLivroComponent` |
| `auth.spec.ts` | `AuthService` |
| `livro.spec.ts` | `LivroService` |

---

## 🎨 Padronização de Código

O projeto utiliza **Prettier** para manter a consistência do código:
```bash
npx prettier --write .
```

---

## 🏗️ Estrutura de Pastas

```
src/
├── app/
│   ├── components/
│   │   ├── autenticacao/       # Login e Registro
│   │   ├── compartilhado/      # Navbar e Rodapé
│   │   └── livros/             # CRUD de livros
│   ├── interceptors/           # Interceptors HTTP (autenticação)
│   ├── models/                 # Interfaces TypeScript
│   ├── services/               # LivroService, AuthService
│   ├── app.ts                  # Componente raiz
│   ├── app.routes.ts           # Definição de rotas
│   └── app.config.ts           # Configuração da aplicação
├── index.html
└── main.ts
vitest.config.ts                # Configuração do Vitest (happy-dom, threads)
angular.json                    # Configuração do Angular CLI
```

---

*Parte do **Projeto Q.A** — Senac 2026.*