package com.senac.projeto_qa.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.entities.Usuario;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=========================================");
        System.out.println("Tentando autenticar email: '" + email + "'");
        System.out.println("=========================================");
        
        // Busca no banco
        Optional<Usuario> optional = usuarioRepository.findByEmail(email);
        
        if (optional.isEmpty()) {
            System.out.println("❌ Usuário NÃO encontrado para email: '" + email + "'");
            
            // Lista todos os usuários para debug
            System.out.println("Usuários no banco:");
            usuarioRepository.findAll().forEach(u -> 
                System.out.println("  - '" + u.getEmail() + "'")
            );
            
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }
        
        Usuario usuario = optional.get();
        System.out.println("✅ Usuário encontrado: " + usuario.getEmail());
        System.out.println("Senha no banco (criptografada): " + usuario.getSenha());
        System.out.println("=========================================");
        
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}