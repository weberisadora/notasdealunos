package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Aluno {
    private int codigo;
    private String nome;
    private ArrayList<Disciplina> disciplinas = new ArrayList<>();

    public Aluno(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }
}
