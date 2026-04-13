package com.senac.projeto_qa.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senac.projeto_qa.Repository.LivroRepository;
import com.senac.projeto_qa.entities.Livro;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> findAll() {
        return livroRepository.findAll();
    };

    public List<Livro> findByUsuarioId(String usuarioId) {
        return livroRepository.findByUsuarioId(usuarioId);
    };

    public Optional<Livro> findById(String id) {
        return livroRepository.findById(id);
    };

    public Livro updateParcial(String id, Map<String, Object> updates) {
        return livroRepository.findById(id).map(livro -> {
            // Aplica apenas os campos que foram enviados
            updates.forEach((campo, valor) -> {
                switch (campo) {
                    case "titulo" -> livro.setTitulo((String) valor);
                    case "autor" -> livro.setAutor((String) valor);
                    case "genero" -> livro.setGenero((String) valor);
                    case "ano" -> livro.setAno((Integer) valor);
                    case "capaUrl" -> livro.setCapaUrl((String) valor);
                    case "status" -> livro.setStatus((String) valor);
                    case "nota" -> livro.setNota((Integer) valor);
                    case "resumo" -> livro.setResumo((String) valor);
                    case "usuarioId" -> livro.setUsuarioId((String) valor);
                }
            });
            return livroRepository.save(livro);
        }).orElse(null);
    }

    public void deleteById(String id) {
        livroRepository.deleteById(id);
    };

};
