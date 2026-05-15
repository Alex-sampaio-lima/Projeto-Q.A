# 📋 Matriz de Rastreabilidade de Requisitos (RTM)

Este documento mapeia os requisitos do sistema **Gerenciador de Biblioteca Pessoal** às suas respectivas implementações e testes, garantindo a cobertura total das funcionalidades solicitadas.

## 1. Requisitos Funcionais (RF)

| ID | Descrição do Requisito | Módulo Backend | Módulo Frontend | Caso de Teste | Status |
|:---|:---|:---|:---|:---|:---|
| **RF01** | Permitir o cadastro de novos usuários. | `UsuarioController`, `UsuarioService` | `RegisterComponent` | `shouldRegisterNewUser` | ✅ Implementado |
| **RF02** | Permitir autenticação de usuários (Login). | `AuthController`, `SecurityConfig` | `LoginComponent` | `shouldAuthenticateUser` | ✅ Implementado |
| **RF03** | Permitir o cadastro de um novo livro. | `LivroController.create()` | `BookFormComponent` | `shouldCreateBook` | ✅ Implementado |
| **RF04** | Listar todos os livros cadastrados pelo usuário. | `LivroController.findAll()` | `BookListComponent` | `shouldListBooks` | ✅ Implementado |
| **RF05** | Permitir a edição dos dados de um livro. | `LivroController.update()` | `BookFormComponent` | `shouldUpdateBook` | ✅ Implementado |
| **RF06** | Permitir a exclusão de um livro do acervo. | `LivroController.delete()` | `BookListComponent` | `shouldDeleteBook` | ✅ Implementado |
| **RF07** | Garantir que um usuário veja apenas seus próprios livros. | `LivroService` (Filter by User) | `BookService` | `shouldOnlyShowUserBooks` | ✅ Implementado |
| **RF08** | Validar campos obrigatórios (ISBN, Título, Autor). | `LivroDTO` (@Valid) | `ReactiveForms` | `shouldValidateRequiredFields` | ✅ Implementado |

## 2. Requisitos Não Funcionais (RNF)

| ID | Descrição do Requisito | Categoria | Implementação | Validação | Status |
|:---|:---|:---|:---|:---|:---|
| **RNF01** | O sistema deve persistir dados de forma não relacional. | Persistência | MongoDB | `application.properties` | ✅ Implementado |
| **RNF02** | As senhas dos usuários devem ser criptografadas. | Segurança | `BCryptPasswordEncoder` | `SecurityTests` | ✅ Implementado |
| **RNF03** | A interface deve ser responsiva. | Usabilidade | CSS (Flexbox/Grid) | Inspeção Manual | ✅ Implementado |
| **RNF04** | O sistema deve rodar em Java 21+. | Performance | Maven / JDK 21 | Build Success | ✅ Implementado |
| **RNF05** | Cobertura mínima de testes unitários. | Qualidade | Vitest / JaCoCo | Coverage Report | ✅ Implementado |
| **RNF06** | O sistema deve possuir Integração Contínua (CI). | Automação | GitHub Actions | `.github/workflows/ci.yml` | ✅ Implementado |

---

## 🔗 Legenda
- **✅ Implementado**: Funcionalidade completa e testada.
- **🟡 Em Progresso**: Em fase de desenvolvimento ou aguardando testes.
- **❌ Pendente**: Requisito ainda não iniciado.

---
*Documento gerado para fins de Qualidade de Software (Q.A) - Senac 2026.*
