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
import org.springframework.http.MediaType;
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
import com.senac.projeto_qa.Service.UsuarioService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("UsuarioController - Testes de API E2E (Sem Mocks)")
class UsuarioControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate(
            new org.springframework.http.client.JdkClientHttpRequestFactory());

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /usuarios - Deve retornar todos os usuarios (Rota Publica)")
    void getAllUsuarios() {
        Usuario u = new Usuario();
        u.setNome("Teste");
        u.setEmail("teste@email.com");
        u.setSenha(passwordEncoder.encode("123"));
        usuarioRepository.save(u);

        ResponseEntity<Usuario[]> response = restTemplate.getForEntity("http://localhost:" + port + "/usuarios",
                Usuario[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()[0].getNome()).isEqualTo("Teste");
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Deve retornar usuario existente (Rota Publica)")
    void getUsuarioById_existente() {
        Usuario u = new Usuario();
        u.setNome("Buscado");
        u.setEmail("busca@email.com");
        u.setSenha(passwordEncoder.encode("123"));
        u = usuarioRepository.save(u);

        ResponseEntity<Usuario> response = restTemplate
                .getForEntity("http://localhost:" + port + "/usuarios/" + u.getId(), Usuario.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Buscado");
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Deve retornar 404 para usuario nao existente")
    void getUsuarioById_naoExistente() {
        try {
            restTemplate.getForEntity("http://localhost:" + port + "/usuarios/ID_FALSO", String.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    @DisplayName("POST /usuarios - Deve criar usuario (Rota Publica)")
    void createUsuario() {
        String json = """
                {
                    "nome": "Novo Usuario",
                    "email": "novo@email.com",
                    "senha": "senha",
                    "confirmarSenha": "senha"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Usuario> response = restTemplate.postForEntity("http://localhost:" + port + "/usuarios", request,
                Usuario.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Novo Usuario");
    }

    @Test
    @DisplayName("PATCH /usuarios/{id} - Deve atualizar parcialmente o usuario (Requer Autenticacao)")
    void updateParcial_existente() {
        Usuario u = new Usuario();
        u.setNome("Antigo");
        u.setEmail("antigo@email.com");
        u.setSenha(passwordEncoder.encode("123"));
        u = usuarioRepository.save(u);

        String json = """
                {
                    "nome": "Atualizado"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("antigo@email.com", "123");
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Usuario> response = restTemplate.exchange(
                "http://localhost:" + port + "/usuarios/" + u.getId(),
                HttpMethod.PATCH,
                request,
                Usuario.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Atualizado");
    }

    @Test
    @DisplayName("PATCH /usuarios/{id} - Deve retornar 404 para nao existente")
    void updateParcial_naoExistente() {
        Usuario auth = new Usuario();
        auth.setNome("Auth");
        auth.setEmail("auth@email.com");
        auth.setSenha(passwordEncoder.encode("123"));
        usuarioRepository.save(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("auth@email.com", "123");
        HttpEntity<String> request = new HttpEntity<>("{\"nome\":\"Algo\"}", headers);

        try {
            restTemplate.exchange(
                    "http://localhost:" + port + "/usuarios/ID_FALSO",
                    HttpMethod.PATCH,
                    request,
                    String.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - Deve deletar usuario (Requer Autenticacao)")
    void deleteUsuario() {
        Usuario u = new Usuario();
        u.setNome("Deletar");
        u.setEmail("del@email.com");
        u.setSenha(passwordEncoder.encode("123"));
        u = usuarioRepository.save(u);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("del@email.com", "123");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/usuarios/" + u.getId(),
                HttpMethod.DELETE,
                request,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(usuarioRepository.findById(u.getId())).isEmpty();
    }
}
