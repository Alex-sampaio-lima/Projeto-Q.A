package com.senac.projeto_qa.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Usuario;

public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Cadastro - Usuários
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsById(usuario.getId())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        usuario.setNome(usuario.getNome());
        usuario.setEmail(usuario.getEmail());
        usuario.setSenha(usuario.getSenha());
        usuario.setConfirmarSenha(usuario.getConfirmarSenha());

        return usuarioRepository.save(usuario);
    };

    // CRUD - Usuários
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    };

    public Optional<Usuario> findById(String id) {
        return usuarioRepository.findById(id);
    };

    public Usuario updateParcial(String id, Map<String, Object> updates) {
        return usuarioRepository.findById(id).map(usuario -> {
            updates.forEach((campo, valor) -> {
                switch (campo) {
                    case "nome" -> usuario.setNome((String) valor);
                    case "email" -> usuario.setEmail((String) valor);
                    case "senha" -> usuario.setSenha((String) valor);
                    case "confirmarSenha" -> usuario.setConfirmarSenha((String) valor);
                }
            });
            return usuarioRepository.save(usuario);
        }).orElse(null);
    };

    public void deleteById(String id) {
        usuarioRepository.deleteById(id);
    };

};
