package org.example;

import org.example.repository.AlunoRepository;
import org.example.repository.AvaliacaoRepository;
import org.example.repository.DisciplinaRepository;
import org.example.service.CursoService;
import org.example.ui.CursoUI;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AlunoRepository alunoRepository;
        AvaliacaoRepository avaliacaoRepository;
        DisciplinaRepository disciplinaRepository;

        try {
            alunoRepository = new AlunoRepository();
            disciplinaRepository = new DisciplinaRepository();
            avaliacaoRepository = new AvaliacaoRepository();

            alunoRepository.getFromFile();
            disciplinaRepository.getFromFile();
            avaliacaoRepository.getFromFile();

            CursoService cursoService = new CursoService(alunoRepository, avaliacaoRepository, disciplinaRepository);

            Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
            CursoUI ui = new CursoUI(scanner, cursoService);

            ui.menu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
