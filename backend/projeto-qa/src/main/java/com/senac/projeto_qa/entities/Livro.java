package com.senac.projeto_qa.entities;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
    private String titulo;
    private String autor;
    private String genero;
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