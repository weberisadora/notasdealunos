package org.example.service;

import org.example.model.Aluno;
import org.example.model.Avaliacao;
import org.example.model.Disciplina;
import org.example.repository.AlunoRepository;
import org.example.repository.AvaliacaoRepository;
import org.example.repository.DisciplinaRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CursoService {
    AlunoRepository alunoRepository;
    AvaliacaoRepository avaliacaoRepository;
    DisciplinaRepository disciplinaRepository;

    public CursoService(AlunoRepository alunoRepository, AvaliacaoRepository avaliacaoRepository, DisciplinaRepository disciplinaRepository) {
        this.alunoRepository = alunoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public void salvarDados() throws IOException {
        alunoRepository.saveToFile();
        avaliacaoRepository.saveToFile();
        disciplinaRepository.saveToFile();
    }

    public void cadastrarAluno(int codigo, String nome) {
        Aluno aluno = new Aluno(codigo, nome);
        alunoRepository.addAluno(aluno);
    }

    public void cadastrarDisciplina(int codigo, String nome) {
        Disciplina disciplina = new Disciplina(codigo, nome);
        disciplinaRepository.add(disciplina);
    }

    public void cadastrarAvaliacao(int codigoAluno, int codigoDisciplina, double nota) {
        Aluno aluno = alunoRepository.getAluno(codigoAluno);
        Disciplina disciplina = disciplinaRepository.get(codigoDisciplina);

        if (aluno == null) throw new IllegalArgumentException("O aluno informado não existe.");

        if (disciplina == null) throw new IllegalArgumentException("A disciplina informada não existe.");

        if (!aluno.getDisciplinas().contains(disciplina))
            throw new IllegalArgumentException("O aluno não está matriculado na disciplina informada.");

        if (nota < 0 || nota > 10)
            throw new IllegalArgumentException("Nota inválida. O valor da nota deve estar entre 0 e 10.");

        if (!alunoTemAvaliacaoPendente(aluno, disciplina))
            throw new IllegalArgumentException("O aluno já possui nota nas duas avaliações da disciplina.");

        Avaliacao avaliacao = new Avaliacao(nota, aluno, disciplina);
        avaliacaoRepository.add(avaliacao);
    }

    public List<Aluno> getAllAlunos() {
        return alunoRepository.getAlunos();
    }

    public void matricula(int codigoAluno, int codigoDisciplina) {
        Aluno aluno = alunoRepository.getAluno(codigoAluno);
        Disciplina disciplina = disciplinaRepository.get(codigoDisciplina);

        if (aluno == null) throw new IllegalArgumentException("O aluno informado não existe.");

        if (disciplina == null) throw new IllegalArgumentException("A disciplina informada não existe.");

        aluno.getDisciplinas().add(disciplina);
    }

    private boolean alunoTemAvaliacaoPendente(Aluno aluno, Disciplina disciplina) {
        int cont = 0;

        for (Avaliacao ignored : avaliacaoRepository.get(aluno, disciplina))
            cont++;

        return cont < 2;
    }

    public double mediaAritmetica(int codigoAluno, int codigoDisciplina) throws Exception {
        Aluno aluno = alunoRepository.getAluno(codigoAluno);
        Disciplina disciplina = disciplinaRepository.get(codigoDisciplina);

        if (aluno == null) throw new IllegalArgumentException("O aluno informado não existe.");

        if (disciplina == null) throw new IllegalArgumentException("A disciplina informada não existe.");

        List<Double> notas = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacaoRepository.get(aluno, disciplina))
            notas.add(avaliacao.getNota());

        if (notas.size() < 2)
            throw new Exception("O aluno " + aluno.getNome() + " não possui notas suficientes para a disciplina " + disciplina.getNome() + ".");

        return (notas.get(0) + notas.get(1)) / 2;
    }

    public double mediaPonderada(int codigoAluno, int codigoDisciplina) throws Exception {
        Aluno aluno = alunoRepository.getAluno(codigoAluno);
        Disciplina disciplina = disciplinaRepository.get(codigoDisciplina);

        if (aluno == null) throw new IllegalArgumentException("O aluno informado não existe.");

        if (disciplina == null) throw new IllegalArgumentException("A disciplina informada não existe.");

        List<Double> notas = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacaoRepository.get(aluno, disciplina))
            notas.add(avaliacao.getNota());

        if (notas.size() < 2)
            throw new Exception("O aluno " + aluno.getNome() + " não possui notas suficientes para a disciplina " + disciplina.getNome() + ".");

        return (notas.get(0) + notas.get(1) * 2) / 3;
    }

    public boolean alunoAprovado(int codigoAluno, int codigoDisciplina) throws Exception {
        double notaFinal = mediaPonderada(codigoAluno, codigoDisciplina);
        return notaFinal >= 7;
    }
}
