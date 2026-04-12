package com.senac.projeto_qa.Service;

import java.util.List;
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

    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    };

    public void deleteById(String id) {
        livroRepository.deleteById(id);
    };

    public Livro updateStatus(String id, String status) {
        Optional<Livro> livroOpt = livroRepository.findById(id);
        if (livroOpt.isPresent()) {
            Livro livro = livroOpt.get();
            livro.setStatus(status);
            return livroRepository.save(livro);
        }
        return null;
    };
    
};
