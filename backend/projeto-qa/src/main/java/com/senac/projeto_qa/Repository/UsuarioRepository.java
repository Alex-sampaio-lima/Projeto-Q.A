package com.senac.projeto_qa.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.senac.projeto_qa.entities.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    List<Usuario> findByEmail(String email);

    List<Usuario> findByNomeUsuario(String nomeUsuario);
    
};
