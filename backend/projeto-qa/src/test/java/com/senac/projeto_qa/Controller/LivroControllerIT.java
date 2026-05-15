package com.senac.projeto_qa.Controller;

import com.senac.projeto_qa.Repository.LivroRepository;
import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Livro;
import com.senac.projeto_qa.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("LivroController - Testes de Caixa Preta (Endpoints de API)")
class LivroControllerIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /livros - Deve retornar 401 Unauthorized quando não autenticado")
    void getAllLivros_deveRetornar401QuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/livros"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser // Simula o contexto de segurança sem precisar criar o usuário no banco para este teste de caixa preta
    @DisplayName("POST /livros - Deve cadastrar um livro com sucesso (Caixa Preta)")
    void createLivro_deveRetornar201QuandoValido() throws Exception {
        String json = """
                {
                    "titulo": "Aventuras em Java",
                    "autor": "Autor Desconhecido",
                    "genero": "Tecnologia",
                    "ano": 2024
                }
                """;

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Aventuras em Java"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /livros - Deve retornar lista de livros para usuário autenticado")
    void getAllLivros_deveRetornarListaQuandoAutenticado() throws Exception {
        Livro l = new Livro();
        l.setTitulo("Livro Teste");
        l.setAutor("Autor");
        l.setGenero("Genero");
        l.setAno(2000);
        livroRepository.save(l);

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Livro Teste"));
    }
}
