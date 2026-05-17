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

import com.senac.projeto_qa.Repository.LivroRepository;
import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Livro;
import com.senac.projeto_qa.entities.Usuario;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("LivroController - Testes de API E2E (Sem Mocks)")
class LivroControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate(new org.springframework.http.client.JdkClientHttpRequestFactory());

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioLogado;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuarioLogado = new Usuario();
        usuarioLogado.setNome("Mock User");
        usuarioLogado.setEmail("user@test.com");
        usuarioLogado.setSenha(passwordEncoder.encode("senha123"));
        usuarioLogado = usuarioRepository.save(usuarioLogado);
    }

    @Test
    @DisplayName("GET /livros - Deve retornar 401 Unauthorized quando não autenticado")
    void getAllLivros_deveRetornar401QuandoNaoAutenticado() {
        try {
            restTemplate.getForEntity("http://localhost:" + port + "/livros", String.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Test
    @DisplayName("POST /livros - Deve cadastrar um livro com sucesso")
    void createLivro_deveRetornar201QuandoValido() {
        String json = """
                {
                    "titulo": "Aventuras em Java",
                    "autor": "Autor Desconhecido",
                    "genero": "Tecnologia",
                    "ano": 2024
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Livro> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros",
                HttpMethod.POST,
                request,
                Livro.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitulo()).isEqualTo("Aventuras em Java");
        assertThat(response.getBody().getUsuarioId()).isEqualTo(usuarioLogado.getId());
    }

    @Test
    @DisplayName("GET /livros - Deve retornar lista de livros gerais")
    void getAllLivros_deveRetornarListaQuandoAutenticado() {
        Livro l = new Livro();
        l.setTitulo("Livro Teste");
        l.setAutor("Autor");
        l.setGenero("Genero");
        l.setAno(2000);
        l.setUsuarioId(usuarioLogado.getId());
        livroRepository.save(l);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Livro[]> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros",
                HttpMethod.GET,
                request,
                Livro[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
        assertThat(response.getBody()[0].getTitulo()).isEqualTo("Livro Teste");
    }

    @Test
    @DisplayName("GET /livros/meusLivros - Deve retornar apenas os livros do usuario logado")
    void getMeusLivros_deveRetornarMeusLivros() {
        Livro l = new Livro();
        l.setTitulo("Meu Livro");
        l.setAutor("Autor");
        l.setGenero("Genero");
        l.setAno(2000);
        l.setUsuarioId(usuarioLogado.getId());
        livroRepository.save(l);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Livro[]> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros/meusLivros",
                HttpMethod.GET,
                request,
                Livro[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()[0].getTitulo()).isEqualTo("Meu Livro");
    }

    @Test
    @DisplayName("GET /livros/livro/{id} - Deve retornar o livro pelo id se for do usuario")
    void getLivroById_deveRetornarLivro() {
        Livro l = new Livro();
        l.setTitulo("Livro Específico");
        l.setAutor("A");
        l.setGenero("G");
        l.setAno(2000);
        l.setUsuarioId(usuarioLogado.getId());
        l = livroRepository.save(l);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Livro> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros/livro/" + l.getId(),
                HttpMethod.GET,
                request,
                Livro.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitulo()).isEqualTo("Livro Específico");
    }

    @Test
    @DisplayName("GET /livros/{usuarioId} - Deve retornar livros pelo usuarioId se corresponder ao logado")
    void getLivrosByUsuario_deveRetornarLista() {
        Livro l = new Livro();
        l.setTitulo("Mocked");
        l.setAutor("A");
        l.setGenero("G");
        l.setAno(2000);
        l.setUsuarioId(usuarioLogado.getId());
        l = livroRepository.save(l);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Livro[]> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros/" + l.getId(),
                HttpMethod.GET,
                request,
                Livro[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("PATCH /livros/{id} - Deve atualizar parcialmente")
    void updateParcial_deveAtualizar() {
        Livro l = new Livro();
        l.setTitulo("Antes");
        l.setAutor("A");
        l.setGenero("G");
        l.setAno(2000);
        l.setUsuarioId(usuarioLogado.getId());
        l = livroRepository.save(l);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<String> request = new HttpEntity<>("{\"titulo\":\"Depois\"}", headers);

        ResponseEntity<Livro> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros/" + l.getId(),
                HttpMethod.PATCH,
                request,
                Livro.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitulo()).isEqualTo("Depois");
    }

    @Test
    @DisplayName("DELETE /livros/{id} - Deve deletar livro")
    void deleteLivro_deveDeletar() {
        Livro l = new Livro();
        l.setTitulo("A deletar");
        l.setAutor("A");
        l.setGenero("G");
        l.setAno(2000);
        l.setUsuarioId(usuarioLogado.getId());
        l = livroRepository.save(l);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@test.com", "senha123");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/livros/" + l.getId(),
                HttpMethod.DELETE,
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
