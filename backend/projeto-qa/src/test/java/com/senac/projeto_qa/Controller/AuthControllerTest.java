package com.senac.projeto_qa.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Usuario;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("AuthController - Testes de API E2E (Sem Mocks)")
class AuthControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /auth/me - Deve retornar os dados do usuario logado usando Basic Auth real")
    void getCurrentUser_sucesso() {
        Usuario u = new Usuario();
        u.setNome("Auth User");
        u.setEmail("auth@test.com");
        u.setSenha(passwordEncoder.encode("123")); // Salva no BD com hash
        usuarioRepository.save(u);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("auth@test.com", "123");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/auth/me",
                HttpMethod.GET,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("auth@test.com");
        assertThat(response.getBody()).contains("Auth User");
    }

    @Test
    @DisplayName("GET /auth/me - Deve retornar 401 Unauthorized para usuario inexistente/senha errada")
    void getCurrentUser_inexistente() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("inexistente@test.com", "errada");
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange("http://localhost:" + port + "/auth/me", HttpMethod.GET, request, String.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }
}
