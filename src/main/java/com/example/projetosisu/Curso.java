package com.example.projetosisu;


import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String nome;
    private int vagas;
    private int candidatos;
    private List<Nota> notas;

    public Curso(String nome, int vagas, int candidatos) {
        this.nome = nome;
        this.vagas = vagas;
        this.candidatos = candidatos;
        this.notas = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public int getVagas() { return vagas; }
    public int getCandidatos() { return candidatos; }
    public List<Nota> getNotas() { return notas; }

    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
    }

    public double getRelacaoCandidatoVaga() {
        if (vagas == 0) return 0;
        return (double) candidatos / vagas;
    }
}
