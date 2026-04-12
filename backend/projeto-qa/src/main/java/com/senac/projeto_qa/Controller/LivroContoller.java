package com.senac.projeto_qa.Controller;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.senac.projeto_qa.Service.LivroService;
import com.senac.projeto_qa.entities.Livro;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "*")
public class LivroContoller {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<List<Livro>> getAllLivros() {
        return ResponseEntity.ok(livroService.findAll());
    };

    @GetMapping("usuarioId")
    public ResponseEntity<List<Livro>> getLivrosByUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(livroService.findByUsuarioId(usuarioId));
    };

    @PostMapping
    public ResponseEntity<Livro> createLivro(@RequestBody Livro livro) {
        Livro savedLivro = livroService.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLivro);
    };

    @PatchMapping("/{id}")
    public ResponseEntity<Livro> updateParcial(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Livro livroAtualizado = livroService.updateParcial(id, updates);
        return livroAtualizado != null ? ResponseEntity.ok(livroAtualizado) : ResponseEntity.notFound().build();
    };

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable String id) {
        livroService.deleteById(id);
        return ResponseEntity.noContent().build();
    };

};