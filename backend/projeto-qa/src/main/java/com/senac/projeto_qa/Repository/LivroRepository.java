package com.senac.projeto_qa.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.senac.projeto_qa.entities.Livro;

@Repository
public interface LivroRepository extends MongoRepository<Livro, String> {
    List<Livro> findByUsuarioId(String usuarioId);

    List<Livro> findByStatus(String status);
};
