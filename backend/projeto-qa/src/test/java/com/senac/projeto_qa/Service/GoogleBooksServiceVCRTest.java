package com.senac.projeto_qa.Service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.senac.projeto_qa.entities.Livro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GoogleBooksService - Testes de VCR com WireMock (API Externa)")
class GoogleBooksServiceVCRTest {

    private WireMockServer wireMockServer;
    private GoogleBooksService googleBooksService;

    @BeforeEach
    void setUp() {
        // Inicializa o WireMock na porta aleatória
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);

        googleBooksService = new GoogleBooksService();
        // Redireciona o serviço para bater no nosso servidor Mock
        googleBooksService.setGoogleBooksApiUrl("http://localhost:8089/books/v1/volumes?q=isbn:");
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("VCR Test: Deve usar o arquivo JSON (cassette) gravado para simular a API do Google Books sem internet")
    void buscarPorIsbn_comWireMock() {
        String isbn = "9780132350884"; // Clean Code (Original)
        
        // O WireMock vai reproduzir automaticamente o arquivo JSON que gravamos
        Livro livro = googleBooksService.buscarPorIsbn(isbn);

        assertThat(livro).isNotNull();
        assertThat(livro.getTitulo()).contains("Clean Code");
        assertThat(livro.getAutor()).contains("Martin");
        assertThat(livro.getAno()).isEqualTo(2009);
    }
}
