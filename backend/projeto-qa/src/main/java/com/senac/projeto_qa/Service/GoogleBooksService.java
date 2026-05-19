package com.senac.projeto_qa.Service;

import com.senac.projeto_qa.entities.Livro;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate;
    private String googleBooksApiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public GoogleBooksService() {
        this.restTemplate = new RestTemplate();
    }

    // Permite injetar a URL do mock durante os testes
    public void setGoogleBooksApiUrl(String url) {
        this.googleBooksApiUrl = url;
    }

    public Livro buscarPorIsbn(String isbn) {
        try {
            String url = googleBooksApiUrl + isbn;
            Map response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

                if (items != null && !items.isEmpty()) {
                    Map<String, Object> volumeInfo = (Map<String, Object>) items.get(0).get("volumeInfo");

                    if (volumeInfo != null) {
                        Livro livro = new Livro();
                        
                        livro.setTitulo((String) volumeInfo.getOrDefault("title", "Título Desconhecido"));
                        
                        List<String> authors = (List<String>) volumeInfo.get("authors");
                        if (authors != null && !authors.isEmpty()) {
                            livro.setAutor(authors.get(0));
                        } else {
                            livro.setAutor("Autor Desconhecido");
                        }
                        
                        List<String> categories = (List<String>) volumeInfo.get("categories");
                        if (categories != null && !categories.isEmpty()) {
                            livro.setGenero(categories.get(0));
                        } else {
                            livro.setGenero("Gênero Desconhecido");
                        }
                        
                        String publishedDate = (String) volumeInfo.get("publishedDate");
                        if (publishedDate != null && publishedDate.length() >= 4) {
                            livro.setAno(Integer.parseInt(publishedDate.substring(0, 4)));
                        } else {
                            livro.setAno(0);
                        }
                        
                        Map<String, String> imageLinks = (Map<String, String>) volumeInfo.get("imageLinks");
                        if (imageLinks != null && imageLinks.containsKey("thumbnail")) {
                            livro.setCapaUrl(imageLinks.get("thumbnail"));
                        }

                        livro.setStatus("Não lido");

                        return livro;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar livro no Google Books: " + e.getMessage());
        }
        
        return null;
    }
}
