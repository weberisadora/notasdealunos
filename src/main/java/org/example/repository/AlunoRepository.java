package org.example.repository;

import com.google.gson.Gson;
import org.example.model.Aluno;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlunoRepository {
    private static Gson gson = new Gson();
    private static String caminho = "src/main/resources/alunos.json";
    private List<Aluno> alunos;

    public AlunoRepository() {
        this.alunos = new ArrayList<>();
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public Aluno getAluno(int codigo) {
        for (Aluno aluno : alunos) {
            if (aluno.getCodigo() == codigo)
                return aluno;
        }

        return null;
    }

    public void addAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public void getFromFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho));
        Aluno[] arrayAlunos = gson.fromJson(bufferedReader, Aluno[].class);
        bufferedReader.close();
        alunos = new ArrayList<>(Arrays.asList(arrayAlunos));
    }

    public void saveToFile() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(caminho));
        bufferedWriter.write(gson.toJson(alunos));
        bufferedWriter.close();
    }
}
