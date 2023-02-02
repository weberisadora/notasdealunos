package org.example.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Disciplina {
    private int codigo;
    private String nome;

    public Disciplina(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }
}
