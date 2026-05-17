# 📋 Matriz de Rastreabilidade de Requisitos (RTM)

Este documento mapeia os requisitos do sistema **Gerenciador de Biblioteca Pessoal** às suas respectivas implementações e testes, garantindo a cobertura total das funcionalidades solicitadas.

---

## 1. Requisitos Funcionais (RF)

| ID | Descrição do Requisito | Módulo Backend | Módulo Frontend | Arquivo de Teste | Status |
|:---|:---|:---|:---|:---|:---|
| **RF01** | Permitir o cadastro de novos usuários. | `UsuarioController.createUsuario()`, `UsuarioService.registrar()` | `RegistroComponent` | `registro.spec.ts` | ✅ Implementado |
| **RF02** | Permitir autenticação de usuários (Login). | `AuthController.getCurrentUser()`, `SecurityConfig` | `LoginComponent` | `login.spec.ts` | ✅ Implementado |
| **RF03** | Permitir o cadastro de um novo livro. | `LivroController.createLivro()` | `FormularioLivroComponent` | `formulario-livro.spec.ts` | ✅ Implementado |
| **RF04** | Listar todos os livros cadastrados pelo usuário. | `LivroController.getMeusLivros()` | `ListaLivrosComponent` | `lista-livros.spec.ts` | ✅ Implementado |
| **RF05** | Permitir a visualização do detalhe de um livro. | `LivroController.getLivroById()` | `DetalheLivroComponent` | `detalhe-livro.spec.ts` | ✅ Implementado |
| **RF06** | Permitir a edição dos dados de um livro. | `LivroController.updateParcial()` | `FormularioLivroComponent` | `formulario-livro.spec.ts` | ✅ Implementado |
| **RF07** | Permitir a exclusão de um livro do acervo. | `LivroController.deleteLivro()` | `ListaLivrosComponent` | `lista-livros.spec.ts` | ✅ Implementado |
| **RF08** | Garantir que um usuário veja apenas seus próprios livros. | `LivroService.findByUsuarioId()` | `LivroService` | `livro.spec.ts` | ✅ Implementado |
| **RF09** | Validar campos obrigatórios (ISBN, Título, Autor). | `Livro.java` (`@NotBlank`, `@Valid`) | `ReactiveFormsModule` | `formulario-livro.spec.ts` | ✅ Implementado |

---

## 2. Requisitos Não Funcionais (RNF)

| ID | Descrição do Requisito | Categoria | Implementação | Validação | Status |
|:---|:---|:---|:---|:---|:---|
| **RNF01** | O sistema deve persistir dados de forma não relacional. | Persistência | MongoDB (porta 27017) | `application.properties` | ✅ Implementado |
| **RNF02** | As senhas dos usuários devem ser criptografadas. | Segurança | `BCryptPasswordEncoder` | `SecurityConfig` | ✅ Implementado |
| **RNF03** | A interface deve ser responsiva. | Usabilidade | CSS (Flexbox/Grid + SCSS) | Inspeção Manual | ✅ Implementado |
| **RNF04** | O sistema deve rodar em Java 21+. | Portabilidade | Maven Wrapper / JDK 21 | Build do CI (`backend-build`) | ✅ Implementado |
| **RNF05** | O frontend deve ser testado com cobertura automatizada. | Qualidade | **Vitest 4** + `happy-dom` | `vitest.config.ts` / CI `frontend-test` | ✅ Implementado |
| **RNF06** | O backend deve ter análise de cobertura de código. | Qualidade | **JaCoCo 0.8.12** (`mvnw verify`) | Relatório `target/site/jacoco/` / CI `backend-coverage` | ✅ Implementado |
| **RNF07** | O sistema deve possuir Integração Contínua (CI). | Automação | **GitHub Actions** (4 jobs) | `.github/workflows/ci.yml` | ✅ Implementado |
| **RNF08** | O pipeline de CI deve validar o build antes dos testes. | Automação | Job `backend-build` (compila sem testar) | GitHub Actions | ✅ Implementado |
| **RNF09** | O pipeline de CI deve gerar e publicar artefatos de cobertura. | Automação | Job `backend-coverage` (upload JaCoCo + Surefire + JAR) | GitHub Actions Artifacts | ✅ Implementado |

---

## 3. Rastreabilidade dos Testes Automatizados

### 3.1 Frontend — Vitest (10 testes)

| Arquivo de Teste | Componente / Serviço Testado | RF Coberto | Resultado |
|:---|:---|:---|:---|
| `app.spec.ts` | `App` (componente raiz) | — | ✅ Passa |
| `login.spec.ts` | `LoginComponent` | RF02 | ✅ Passa |
| `registro.spec.ts` | `RegistroComponent` | RF01 | ✅ Passa |
| `navbar.spec.ts` | `Navbar` (compartilhado) | — | ✅ Passa |
| `rodape.spec.ts` | `Rodape` (compartilhado) | — | ✅ Passa |
| `formulario-livro.spec.ts` | `FormularioLivroComponent` | RF03, RF06, RF09 | ✅ Passa |
| `lista-livros.spec.ts` | `ListaLivrosComponent` | RF04, RF07 | ✅ Passa |
| `detalhe-livro.spec.ts` | `DetalheLivroComponent` | RF05 | ✅ Passa |
| `auth.spec.ts` | `AuthService` | RF02 | ✅ Passa |
| `livro.spec.ts` | `LivroService` | RF08 | ✅ Passa |

### 3.2 Backend — JUnit / JaCoCo / Testcontainers (10 testes)

| Arquivo de Teste | Técnica | Tipo de Teste | Observação |
|:---|:---|:---|:---|
| `LivroServiceTest.java` | Testcontainers | **Caixa Branca** | Valida caminhos internos e lógica de negócio |
| `UsuarioServiceTest.java` | Testcontainers | **Caixa Branca** | Valida fluxos de exceção (senha, e-mail duplicado) |
| `LivroControllerIT.java` | Testcontainers | **Caixa Preta** | Valida endpoints da API via chamadas HTTP reais |
| `ProjetoQaApplicationTests.java` | Testcontainers | Smoke Test | Valida subida do contexto total do sistema |

| Estratégia | Descrição | Status |
|:---|:---|:---|
| **Caixa Branca** | Foco na lógica interna, decisões e caminhos do código Java. | ✅ Implementado |
| **Caixa Preta** | Foco nos requisitos, entradas e saídas (HTTP Status, JSON). | ✅ Implementado |
| **Sem Mocks** | Todo o sistema é testado com banco real em containers. | ✅ Garantido |

| Pacote | Estratégia de Teste | Cobertura Atual (Instruções) |
|:---|:---|:---|
| `config` | Contexto Real | ~100% |
| `Service` | Integração Real (Mongo) | **~53%** |
| `Controller` | Integração de API | **~4%** |
| **Total** | **Foco em Integração** | **~35%** |

> [!IMPORTANT]
> O projeto não utiliza Mocks. Toda a persistência é validada usando containers Docker (Testcontainers).

---

## 🔗 Legenda

| Símbolo | Significado |
|:---|:---|
| ✅ Implementado | Funcionalidade completa e com teste automatizado passando. |
| 🟡 Em Progresso | Em fase de desenvolvimento ou aguardando testes. |
| ❌ Pendente | Requisito ainda não iniciado. |

---
*Documento mantido para fins de Qualidade de Software (Q.A) — Senac 2026.*
