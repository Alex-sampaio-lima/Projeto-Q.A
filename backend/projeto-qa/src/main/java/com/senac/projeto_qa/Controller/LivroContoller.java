package com.senac.projeto_qa.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senac.projeto_qa.Repository.LivroRepository;
import com.senac.projeto_qa.Repository.UsuarioRepository;
import com.senac.projeto_qa.Service.LivroService;
import com.senac.projeto_qa.entities.Livro;
import com.senac.projeto_qa.entities.Usuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "*")
public class LivroContoller {

    @Autowired
    private LivroService livroService;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para pegar usuário que tá logado
    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println("Esse aqui é o auth.getName() = " + email);
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado !"));
    };

    @GetMapping
    public ResponseEntity<List<Livro>> getAllLivros() {
        return ResponseEntity.ok(livroService.findAll());
    };

    @GetMapping("/meusLivros")
    public ResponseEntity<List<Livro>> getMeusLivros() {
        Usuario usuario = getUsuarioLogado();
        List<Livro> meusLivros = livroService.findByUsuarioId(usuario.getId());
        return ResponseEntity.ok(meusLivros);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Livro>> getLivrosByUsuario(@PathVariable String usuarioId) {
        Usuario usuario = getUsuarioLogado();
        Livro livro = livroService.findById(usuarioId).orElse(null);

        if (livro == null || !livro.getUsuarioId().equals(usuario.getId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(livroService.findByUsuarioId(usuarioId));
    };

    @PostMapping
    public ResponseEntity<Livro> createLivro(@Valid @RequestBody Livro livro) {
        Usuario usuario = getUsuarioLogado();
        livro.setUsuarioId(usuario.getId());
        Livro savedLivro = livroRepository.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLivro);
    };

    @PatchMapping("/{livroId}")
    public ResponseEntity<Livro> updateParcial(@PathVariable String livroId, @RequestBody Map<String, Object> updates) {
        Usuario usuario = getUsuarioLogado();
        Livro livro = livroService.findById(livroId).orElse(null);

        // Verificando se o livro existe e é do usuário logado
        if (livro == null || !livro.getUsuarioId().equals(usuario.getId())) {
            return ResponseEntity.notFound().build();
        }

        Livro livroAtualizado = livroService.updateParcial(livroId, updates);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable String id) {
        Usuario usuario = getUsuarioLogado();
        Livro livro = livroService.findById(id).orElse(null);

        // Verifica se o livro é do usuário logado
        if (livro == null || !livro.getUsuarioId().equals(usuario.getId())) {
            return ResponseEntity.notFound().build();
        }

        livroService.deleteById(id);
        return ResponseEntity.noContent().build();
    };

};