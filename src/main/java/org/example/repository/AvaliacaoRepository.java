package org.example.repository;

import com.google.gson.Gson;
import org.example.model.Aluno;
import org.example.model.Avaliacao;
import org.example.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvaliacaoRepository {
    private static final Gson gson = new Gson();
    private static final String caminho = "src/main/resources/avaliacoes.json";
    private List<Avaliacao> avaliacoes;

    public void add(Avaliacao avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public List<Avaliacao> get(Aluno aluno, Disciplina disciplina) {
        List<Avaliacao> temp = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacoes) {
            if (avaliacao.getAluno().equals(aluno) && avaliacao.getDisciplina().equals(disciplina))
                temp.add(avaliacao);
        }

        return temp;
    }

    public void getFromFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho));
        Avaliacao[] Avaliacao = gson.fromJson(bufferedReader, Avaliacao[].class);
        bufferedReader.close();
        avaliacoes = new ArrayList<>(Arrays.asList(Avaliacao));
    }

    public void saveToFile() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(caminho));
        bufferedWriter.write(gson.toJson(avaliacoes));
        bufferedWriter.close();
    }
}
