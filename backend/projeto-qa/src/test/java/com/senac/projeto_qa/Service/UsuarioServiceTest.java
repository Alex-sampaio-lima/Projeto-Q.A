package com.senac.projeto_qa.Service;

import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@DisplayName("UsuarioService - Testes de Caixa Branca (Lógica Interna)")
class UsuarioServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("registrar - Deve codificar a senha e salvar o usuário (Caminho Feliz)")
    void registrar_deveSalvarComSenhaCodificada() {
        Usuario usuario = new Usuario();
        usuario.setNome("Alex");
        usuario.setEmail("alex@email.com");
        usuario.setSenha("senha123");
        usuario.setConfirmarSenha("senha123");

        Usuario salvo = usuarioService.registrar(usuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(passwordEncoder.matches("senha123", salvo.getSenha())).isTrue();
        assertThat(salvo.getConfirmarSenha()).isNull();
    }

    @Test
    @DisplayName("registrar - Deve lançar exceção se e-mail já existir (Caminho de Exceção)")
    void registrar_deveLancarExcecaoEmailExistente() {
        Usuario jaExistente = new Usuario();
        jaExistente.setNome("Existente");
        jaExistente.setEmail("duplicado@email.com");
        jaExistente.setSenha("123");
        jaExistente.setConfirmarSenha("123");
        usuarioRepository.save(jaExistente);

        Usuario novo = new Usuario();
        novo.setEmail("duplicado@email.com");
        novo.setSenha("456");
        novo.setConfirmarSenha("456");

        assertThatThrownBy(() -> usuarioService.registrar(novo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email já cadastrado!");
    }

    @Test
    @DisplayName("updateParcial - Deve atualizar a senha apenas se for diferente da atual")
    void updateParcial_deveGerenciarTrocaDeSenha() {
        Usuario usuario = new Usuario();
        usuario.setNome("Alex");
        usuario.setEmail("alex@email.com");
        usuario.setSenha(passwordEncoder.encode("velha"));
        usuario = usuarioRepository.save(usuario);
        String senhaOriginalCodificada = usuario.getSenha();

        Map<String, Object> updates = Map.of("senha", "velha");
        Usuario atualizado = usuarioService.updateParcial(usuario.getId(), updates);
        assertThat(atualizado.getSenha()).isEqualTo(senhaOriginalCodificada);

        updates = Map.of("senha", "nova");
        atualizado = usuarioService.updateParcial(usuario.getId(), updates);
        assertThat(atualizado.getSenha()).isNotEqualTo(senhaOriginalCodificada);
        assertThat(passwordEncoder.matches("nova", atualizado.getSenha())).isTrue();

        updates = Map.of("nome", "Novo Nome", "email", "novo@email.com", "confirmarSenha", "algumaCoisa");
        atualizado = usuarioService.updateParcial(usuario.getId(), updates);
        assertThat(atualizado.getNome()).isEqualTo("Novo Nome");
        assertThat(atualizado.getEmail()).isEqualTo("novo@email.com");
        assertThat(atualizado.getConfirmarSenha()).isNull();
    }

    @Test
    @DisplayName("findAll - Deve retornar todos os usuarios")
    void findAll_deveRetornarTodos() {
        Usuario u1 = new Usuario(); u1.setNome("U1"); u1.setEmail("u1@email.com"); u1.setSenha("123"); u1.setConfirmarSenha("123");
        Usuario u2 = new Usuario(); u2.setNome("U2"); u2.setEmail("u2@email.com"); u2.setSenha("123"); u2.setConfirmarSenha("123");
        usuarioService.registrar(u1);
        usuarioService.registrar(u2);

        assertThat(usuarioService.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("findById - Deve encontrar usuario")
    void findById_deveEncontrar() {
        Usuario u1 = new Usuario(); u1.setNome("U1"); u1.setEmail("u1@email.com"); u1.setSenha("123"); u1.setConfirmarSenha("123");
        u1 = usuarioService.registrar(u1);

        assertThat(usuarioService.findById(u1.getId())).isPresent();
    }

    @Test
    @DisplayName("deleteById - Deve remover usuario")
    void deleteById_deveRemover() {
        Usuario u1 = new Usuario(); u1.setNome("U1"); u1.setEmail("u1@email.com"); u1.setSenha("123"); u1.setConfirmarSenha("123");
        u1 = usuarioService.registrar(u1);

        usuarioService.deleteById(u1.getId());
        assertThat(usuarioService.findById(u1.getId())).isEmpty();
    }

    @Test
    @DisplayName("registrar - Deve lancar excecao se senhas diferentes")
    void registrar_deveLancarExcecaoSenhasDiferentes() {
        Usuario u1 = new Usuario(); u1.setNome("U1"); u1.setEmail("u1@email.com"); u1.setSenha("123"); u1.setConfirmarSenha("456");
        assertThatThrownBy(() -> usuarioService.registrar(u1))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("As senhas não conferem!");
    }
}
