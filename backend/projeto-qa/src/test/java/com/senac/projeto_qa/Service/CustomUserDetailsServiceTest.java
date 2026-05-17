package com.senac.projeto_qa.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@DisplayName("CustomUserDetailsService - Testes")
class CustomUserDetailsServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("loadUserByUsername - Deve retornar UserDetails quando usuario existe")
    void loadUserByUsername_sucesso() {
        Usuario u = new Usuario();
        u.setNome("Detalhe");
        u.setEmail("detalhe@email.com");
        u.setSenha("123");
        usuarioRepository.save(u);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("detalhe@email.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("detalhe@email.com");
        assertThat(userDetails.getPassword()).isEqualTo("123");
    }

    @Test
    @DisplayName("loadUserByUsername - Deve lancar UsernameNotFoundException quando nao existe")
    void loadUserByUsername_inexistente() {
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("falso@email.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado: falso@email.com");
    }
}
