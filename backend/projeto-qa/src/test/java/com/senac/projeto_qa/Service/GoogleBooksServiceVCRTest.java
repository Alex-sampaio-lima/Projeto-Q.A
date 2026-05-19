package com.senac.projeto_qa.Service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.senac.projeto_qa.entities.Livro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
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
    @DisplayName("VCR Test: Deve buscar os dados do livro simulando a API do Google Books com WireMock")
    void buscarPorIsbn_comWireMock() {
        String isbn = "9788576082675";
        
        // Simula (Grava/VCR) a resposta do Google Books
        String mockResponse = """
                {
                  "items": [
                    {
                      "volumeInfo": {
                        "title": "Clean Code",
                        "authors": ["Robert C. Martin"],
                        "categories": ["Computers"],
                        "publishedDate": "2008-08-01",
                        "imageLinks": {
                          "thumbnail": "http://example.com/cleancode.jpg"
                        }
                      }
                    }
                  ]
                }
                """;

        stubFor(get(urlEqualTo("/books/v1/volumes?q=isbn:" + isbn))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));

        Livro livro = googleBooksService.buscarPorIsbn(isbn);

        assertThat(livro).isNotNull();
        assertThat(livro.getTitulo()).isEqualTo("Clean Code");
        assertThat(livro.getAutor()).isEqualTo("Robert C. Martin");
        assertThat(livro.getAno()).isEqualTo(2008);
        assertThat(livro.getCapaUrl()).isEqualTo("http://example.com/cleancode.jpg");
        assertThat(livro.getGenero()).isEqualTo("Computers");
    }
}
