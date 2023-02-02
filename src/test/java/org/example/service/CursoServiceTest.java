package org.example.service;

import org.example.model.Aluno;
import org.example.model.Avaliacao;
import org.example.model.Disciplina;
import org.example.repository.AlunoRepository;
import org.example.repository.AvaliacaoRepository;
import org.example.repository.DisciplinaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {
    CursoService cursoService;
    static Aluno aluno;
    static Disciplina disciplina;
    static Avaliacao avaliacao;
    AlunoRepository mockAlunoRepository;
    AvaliacaoRepository mockAvaliacaoRepository;
    DisciplinaRepository mockDisciplinaRepository;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void beforeEach() {
        aluno = new Aluno(1, "aluno");
        disciplina = new Disciplina(1, "disciplina");
        aluno.setDisciplinas(new ArrayList<>(Arrays.asList(disciplina)));
        avaliacao = new Avaliacao(6.0, aluno, disciplina);

        mockAlunoRepository = mock(AlunoRepository.class);
        mockAvaliacaoRepository = mock(AvaliacaoRepository.class);
        mockDisciplinaRepository = mock(DisciplinaRepository.class);
        cursoService = new CursoService(mockAlunoRepository, mockAvaliacaoRepository, mockDisciplinaRepository);
    }

    @Test
    void salvarDados() {
        try {
            cursoService.salvarDados();

            verify(mockAlunoRepository, times(1)).saveToFile();
            verify(mockAvaliacaoRepository, times(1)).saveToFile();
            verify(mockDisciplinaRepository, times(1)).saveToFile();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void cadastrarAluno() {
        cursoService.cadastrarAluno(1, "nome");

        ArgumentCaptor<Aluno> argument = ArgumentCaptor.forClass(Aluno.class);
        verify(mockAlunoRepository).addAluno(argument.capture());
        assertEquals(1, argument.getValue().getCodigo());
        assertEquals("nome", argument.getValue().getNome());
    }

    @Test
    void cadastrarDisciplina() {
        cursoService.cadastrarDisciplina(1, "nome");

        ArgumentCaptor<Disciplina> argument = ArgumentCaptor.forClass(Disciplina.class);
        verify(mockDisciplinaRepository).add(argument.capture());
        assertEquals(1, argument.getValue().getCodigo());
        assertEquals("nome", argument.getValue().getNome());
    }

    @Test
    void cadastrarAvaliacao() {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);

        cursoService.cadastrarAvaliacao(1, 1, 6.0);

        ArgumentCaptor<Avaliacao> argument = ArgumentCaptor.forClass(Avaliacao.class);
        verify(mockAvaliacaoRepository).add(argument.capture());
        assertTrue(new ReflectionEquals(avaliacao).matches(argument.getValue()));
    }

    @Test()
    void cadastrarAvaliacaoAlunoNaoExiste() {
        Exception thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.cadastrarAvaliacao(1, 1, 6.0)
        );

        assertTrue(thrown.getMessage().contains("O aluno informado não existe."));
    }

    @Test
    void cadastrarAvaliacaoDisciplinaNaoExiste() {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);

        Exception thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.cadastrarAvaliacao(1, 1, 6.0)
        );

        assertTrue(thrown.getMessage().contains("A disciplina informada não existe."));
    }

    @Test
    void cadastrarAvaliacaoAlunoNaoMatriculado() {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(new Aluno(1, "aluno sem disciplina"));
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);


        Exception thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.cadastrarAvaliacao(1, 1, 6.0)
        );

        assertTrue(thrown.getMessage().contains("O aluno não está matriculado na disciplina informada."));
    }

    @Test
    void cadastrarAvaliacaoNotaInvalida() {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);

        Exception thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.cadastrarAvaliacao(1, 1, 11.0)
        );

        assertTrue(thrown.getMessage().contains("Nota inválida."));
    }

    @Test
    void cadastrarAvaliacaoAvaliacaoPendente() {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao, avaliacao));

        Exception thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.cadastrarAvaliacao(1, 1, 6.0)
        );

        assertTrue(thrown.getMessage().contains("O aluno já possui nota nas duas avaliações da disciplina."));
    }

    @Test
    void getAllAlunos() {
        when(mockAlunoRepository.getAlunos()).thenReturn(List.of(aluno, aluno));
        assertEquals(List.of(aluno, aluno), cursoService.getAllAlunos());
    }

    @Test
    void matricula() {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);

        cursoService.matricula(1, 1);
        assertEquals(2, aluno.getDisciplinas().size());
    }

    @Test
    void mediaAritmetica() throws Exception {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao, avaliacao));

        assertEquals(6.0, cursoService.mediaAritmetica(1, 1));
    }

    @Test
    void mediaAritmeticaNotasInsuficientes() throws Exception {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao));

        Exception thrown = assertThrows(
                Exception.class,
                () -> cursoService.mediaAritmetica(1, 1)
        );

        assertTrue(thrown.getMessage().contains("não possui notas suficientes para a disciplina"));
    }

    @Test
    void mediaPonderada() throws Exception {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao, avaliacao));

        assertEquals(6.0, cursoService.mediaPonderada(1, 1));
    }

    @Test
    void mediaPonderadaNotasInsuficientes() throws Exception {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao));

        Exception thrown = assertThrows(
                Exception.class,
                () -> cursoService.mediaPonderada(1, 1)
        );

        assertTrue(thrown.getMessage().contains("não possui notas suficientes para a disciplina"));
    }

    @Test
    void alunoAprovadoFalse() throws Exception {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao, avaliacao));

        assertFalse(cursoService.alunoAprovado(1, 1));
    }

    @Test
    void alunoAprovadoTrue() throws Exception {
        when(mockAlunoRepository.getAluno(anyInt())).thenReturn(aluno);
        when(mockDisciplinaRepository.get(anyInt())).thenReturn(disciplina);
        avaliacao = new Avaliacao(8.0, aluno, disciplina);
        when(mockAvaliacaoRepository.get(any(Aluno.class), any(Disciplina.class)))
                .thenReturn(List.of(avaliacao, avaliacao));

        assertTrue(cursoService.alunoAprovado(1, 1));
    }
}