package com.senac.projeto_qa.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")

@Document(collection = "livros")
public class Livro {

    @Id
    private String id;

    @NotBlank(message = "O Título é obrigatório")
    @Size(min = 3, max = 100, message = "O Título deve ter entre 3 e 100 caracteres")
    private String titulo;

    @NotBlank(message = "O autor é obrigatório")
    private String autor;

    @NotBlank(message = "O Gênero é obrigatório")
    private String genero;

    @NotNull(message = "O Ano é obrigatório")
    private Integer ano;

    private String capaUrl;
    private String status = "Não Lido";
    private Integer nota;
    private String resumo;
    private String usuarioId;

    public void setStatus(String status) {
        // Aqui estou validando apenas para ser entre "Lido" ou "Não Lido"
        if (status != null && (status.equals("Lido") || status.equals("Não lido"))) {
            this.status = status;
        } else if (status == null) {
            this.status = "Não lido";
        } else {
            throw new IllegalArgumentException("Status deve ser 'Lido' ou 'Não lido'");
        }
    };

    public void setNota(Integer nota) {
        if (nota != null && (nota < 1 || nota > 5)) {
            throw new IllegalArgumentException("Nota deve ser entre 1 e 5");
        }
        this.nota = nota;
    }
};