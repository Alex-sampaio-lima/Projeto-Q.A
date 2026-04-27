package com.senac.projeto_qa.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.senac.projeto_qa.entities.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNome(String nome);

    Optional<Usuario> findBySenha(String senha);
};
