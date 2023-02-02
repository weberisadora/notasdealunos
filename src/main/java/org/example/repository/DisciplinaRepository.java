package org.example.repository;

import com.google.gson.Gson;
import org.example.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisciplinaRepository {
    private static Gson gson = new Gson();
    private static String caminho = "src/main/resources/disciplinas.json";
    private List<Disciplina> disciplinas;

    public void add(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }

    public Disciplina get(int codigo) {
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getCodigo() == codigo)
                return disciplina;
        }

        return null;
    }

    public void getFromFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho));
        Disciplina[] arrDisciplinas = gson.fromJson(bufferedReader, Disciplina[].class);
        bufferedReader.close();
        disciplinas = new ArrayList<>(Arrays.asList(arrDisciplinas));
    }

    public void saveToFile() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(caminho));
        bufferedWriter.write(gson.toJson(disciplinas));
        bufferedWriter.close();
    }
}
