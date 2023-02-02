package org.example.ui;

import org.example.model.Aluno;
import org.example.model.Disciplina;
import org.example.service.CursoService;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CursoUI {
    Scanner scanner;
    CursoService cursoService;

    private int opcao;

    public CursoUI(Scanner scanner, CursoService cursoService) {
        this.scanner = scanner;
        this.cursoService = cursoService;
    }

    public void menu() throws IOException {
        do {
            System.out.println("MENU");
            exibeOpcoes();
            solicitaOpcao();
            validaOpcao();
            verificaOpcao();
        } while (opcao < 8);
    }

    private void exibeOpcoes() {
        System.out.println("[1] Adicionar aluno");
        System.out.println("[2] Adicionar disciplina");
        System.out.println("[3] Matricular aluno em uma disciplina");
        System.out.println("[4] Adicionar nota em uma avaliação de uma disciplina");
        System.out.println("[5] Calcular média aritmética simples de um aluno em uma disciplina");
        System.out.println("[6] Calcular média aritmética ponderada de um aluno em uma disciplina");
        System.out.println("[7] Listar informações de todos os alunos");
        System.out.println("[8] Sair do sistema");
    }

    private void solicitaOpcao() {
        String opcao;
        System.out.println("Digite o número correspondente à operação desejada:");
        opcao = scanner.next();
        try {
            this.opcao = Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida");
            exibeOpcoes();
            solicitaOpcao();
        }
    }

    private void validaOpcao() {
        while (opcao < 1 || opcao > 8) {
            System.out.println("Opção inválida");
            solicitaOpcao();
        }
    }

    private void verificaOpcao() throws IOException {
        int codigoAluno;
        int codigoDisciplina;

        switch (opcao) {
            case 1:
                adicionaAluno();
                break;

            case 2:
                adicionaDisciplina();
                break;

            case 3:
                matricularAlunoEmDisciplina();
                break;

            case 4:
                adicionaNotaEmUmaAvaliacaoDeUmaDisciplina();
                break;

            case 5:
                codigoAluno = solicitaCodigoDoAluno();
                codigoDisciplina = solicitaCodigoDaDisciplina();
                exibeMediaAritmeticaSimplesDeUmAlunoEmUmaDisciplina(codigoAluno, codigoDisciplina);
                break;

            case 6:
                codigoAluno = solicitaCodigoDoAluno();
                codigoDisciplina = solicitaCodigoDaDisciplina();
                exibeMediaAritmeticaPonderadaDeUmAlunoEmUmaDisciplina(codigoAluno, codigoDisciplina);
                break;

            case 7:
                exibeInformacoesDeTodosOsAlunos();
                break;

            case 8:
                sairDoSistema();
                break;
        }
    }

    private void sairDoSistema() throws IOException {
        System.out.println("Saindo...");
        cursoService.salvarDados();
    }

    private void adicionaAluno() {
        int codigo = solicitaCodigoDoAluno();
        scanner.nextLine();

        System.out.println("Digite o nome do aluno:");
        String nome = scanner.nextLine();

        cursoService.cadastrarAluno(codigo, nome);
    }

    private void adicionaDisciplina() {
        int codigo = solicitaCodigoDaDisciplina();
        scanner.nextLine();

        System.out.println("Digite o nome da disciplina:");
        String nome = scanner.nextLine();

        cursoService.cadastrarDisciplina(codigo, nome);
    }

    private void matricularAlunoEmDisciplina() {
        int codigoAluno = solicitaCodigoDoAluno();
        int codigoDisciplina = solicitaCodigoDaDisciplina();
        cursoService.matricula(codigoAluno, codigoDisciplina);
    }

    private void adicionaNotaEmUmaAvaliacaoDeUmaDisciplina() {
        int codigoAluno = solicitaCodigoDoAluno();
        scanner.nextLine();

        int codigoDisciplina = solicitaCodigoDaDisciplina();
        scanner.nextLine();

        System.out.println("Digite a nota:");
        double nota = scanner.nextDouble();

        cursoService.cadastrarAvaliacao(codigoAluno, codigoDisciplina, nota);
    }

    private void exibeMediaAritmeticaSimplesDeUmAlunoEmUmaDisciplina(int codigoAluno, int codigoDisciplina) {
        try {
            double media = cursoService.mediaAritmetica(codigoAluno, codigoDisciplina);
            System.out.println(media);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void exibeMediaAritmeticaPonderadaDeUmAlunoEmUmaDisciplina(int codigoAluno, int codigoDisciplina) {
        try {
            double media = cursoService.mediaPonderada(codigoAluno, codigoDisciplina);
            System.out.println(media);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int solicitaCodigoDoAluno() {
        System.out.println("Digite o código do aluno:");
        String codigoString = scanner.next();
        int codigo = -1;

        try {
            codigo = Integer.parseInt(codigoString);
        } catch (NumberFormatException e) {
            System.out.println("Código inválido.");
            solicitaCodigoDoAluno();
        }

        return codigo;
    }

    private int solicitaCodigoDaDisciplina() {
        System.out.println("Digite o código da disciplina:");
        return scanner.nextInt();
    }

    private void exibeResultadoFinalDoAlunoNaDisciplina(int codigoAluno, int codigoDisciplina) {
        try {
            if (cursoService.alunoAprovado(codigoAluno, codigoDisciplina))
                System.out.println("Aluno aprovado.");
            else
                System.out.println("Aluno reprovado.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void exibeInformacoesDeTodosOsAlunos() {
        List<Aluno> alunos = cursoService.getAllAlunos();

        for (Aluno aluno : alunos) {
            System.out.println("INFORMAÇÕES DO ALUNO:");
            System.out.println("Código do aluno: " + aluno.getCodigo());
            System.out.println("Nome do aluno: " + aluno.getNome());
            System.out.println();
            System.out.println("DISCIPLINAS DO ALUNO:");
            for (Disciplina disciplina : aluno.getDisciplinas()) {
                System.out.println("Código da disciplina: " + disciplina.getCodigo());
                System.out.println("Nome da disciplina: " + disciplina.getNome());
                System.out.println("Média aritmética simples do aluno em " + disciplina.getNome() + ": ");
                exibeMediaAritmeticaSimplesDeUmAlunoEmUmaDisciplina(aluno.getCodigo(), disciplina.getCodigo());
                System.out.println("Média aritmética ponderada do aluno em " + disciplina.getNome() + ": ");
                exibeMediaAritmeticaPonderadaDeUmAlunoEmUmaDisciplina(aluno.getCodigo(), disciplina.getCodigo());
                System.out.println("Resultado final:");
                exibeResultadoFinalDoAlunoNaDisciplina(aluno.getCodigo(), disciplina.getCodigo());
            }
            System.out.println();
        }
    }
}
