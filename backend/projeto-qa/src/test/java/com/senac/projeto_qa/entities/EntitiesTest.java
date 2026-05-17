package com.senac.projeto_qa.entities;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntitiesTest {

    @Test
    void testLivro() {
        Livro l = new Livro();
        l.setId("1");
        l.setTitulo("T");
        l.setAutor("A");
        l.setGenero("G");
        l.setAno(2020);
        l.setCapaUrl("url");
        l.setResumo("R");
        l.setUsuarioId("U");
        
        assertThat(l.getId()).isEqualTo("1");
        assertThat(l.getTitulo()).isEqualTo("T");
        assertThat(l.getAutor()).isEqualTo("A");
        assertThat(l.getGenero()).isEqualTo("G");
        assertThat(l.getAno()).isEqualTo(2020);
        assertThat(l.getCapaUrl()).isEqualTo("url");
        assertThat(l.getResumo()).isEqualTo("R");
        assertThat(l.getUsuarioId()).isEqualTo("U");

        l.setStatus("Lido");
        assertThat(l.getStatus()).isEqualTo("Lido");
        l.setStatus("Não lido");
        assertThat(l.getStatus()).isEqualTo("Não lido");
        l.setStatus(null);
        assertThat(l.getStatus()).isEqualTo("Não lido");

        assertThatThrownBy(() -> l.setStatus("Invalido"))
            .isInstanceOf(IllegalArgumentException.class);

        l.setNota(5);
        assertThat(l.getNota()).isEqualTo(5);

        assertThatThrownBy(() -> l.setNota(6))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUsuario() {
        Usuario u = new Usuario();
        u.setId("1");
        u.setNome("N");
        u.setEmail("E");
        u.setSenha("S");
        u.setConfirmarSenha("CS");

        assertThat(u.getId()).isEqualTo("1");
        assertThat(u.getNome()).isEqualTo("N");
        assertThat(u.getEmail()).isEqualTo("E");
        assertThat(u.getSenha()).isEqualTo("S");
        assertThat(u.getConfirmarSenha()).isEqualTo("CS");
    }
}
