package com.senac.projeto_qa.Service;

import com.senac.projeto_qa.Repository.LivroRepository;
import com.senac.projeto_qa.entities.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@DisplayName("LivroService - Testes de Integração (Sem Mocks)")
class LivroServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    private Livro livro1;
    private Livro livro2;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();

        livro1 = new Livro();
        livro1.setTitulo("Dom Casmurro");
        livro1.setAutor("Machado de Assis");
        livro1.setGenero("Romance");
        livro1.setAno(1899);
        livro1.setUsuarioId("usuario-1");

        livro2 = new Livro();
        livro2.setTitulo("O Cortiço");
        livro2.setAutor("Aluísio Azevedo");
        livro2.setGenero("Naturalismo");
        livro2.setAno(1890);
        livro2.setUsuarioId("usuario-2");

        livro1 = livroRepository.save(livro1);
        livro2 = livroRepository.save(livro2);
    }

    @Test
    @DisplayName("findAll - Deve retornar todos os livros salvos no MongoDB")
    void findAll_deveRetornarTodosOsLivros() {
        List<Livro> resultado = livroService.findAll();
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Livro::getTitulo).containsExactlyInAnyOrder("Dom Casmurro", "O Cortiço");
    }

    @Test
    @DisplayName("findByUsuarioId - Deve filtrar corretamente os livros do usuário")
    void findByUsuarioId_deveRetornarLivrosDoUsuario() {
        List<Livro> resultado = livroService.findByUsuarioId("usuario-1");
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Dom Casmurro");
    }

    @Test
    @DisplayName("findById - Deve encontrar o livro pelo ID gerado pelo MongoDB")
    void findById_deveRetornarLivroQuandoIdExiste() {
        Optional<Livro> resultado = livroService.findById(livro1.getId());
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Dom Casmurro");
    }

    @Test
    @DisplayName("updateParcial - Deve atualizar e persistir as mudanças no MongoDB")
    void updateParcial_deveAtualizarEContextualizarNoBanco() {
        Map<String, Object> updates = Map.of("titulo", "Dom Casmurro - Edição de Teste");
        Livro resultado = livroService.updateParcial(livro1.getId(), updates);
        assertThat(resultado.getTitulo()).isEqualTo("Dom Casmurro - Edição de Teste");
        
        Livro noBanco = livroRepository.findById(livro1.getId()).orElseThrow();
        assertThat(noBanco.getTitulo()).isEqualTo("Dom Casmurro - Edição de Teste");
    }

    @Test
    @DisplayName("updateParcial - Deve validar nota real (1-5)")
    void updateParcial_deveValidarNota() {
        Map<String, Object> updates = Map.of("nota", 10);
        assertThatThrownBy(() -> livroService.updateParcial(livro1.getId(), updates))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deleteById - Deve remover o documento do MongoDB")
    void deleteById_deveRemoverDoBanco() {
        livroService.deleteById(livro1.getId());
        assertThat(livroRepository.findById(livro1.getId())).isEmpty();
    }
}
