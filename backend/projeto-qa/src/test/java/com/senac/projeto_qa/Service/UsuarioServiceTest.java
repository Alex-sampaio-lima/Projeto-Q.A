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
    }
}
