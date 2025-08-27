package com.example.projetosisu;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String nome;
    private String campus;
    private List<Nota> notas;

    public Curso(String nome, String campus) {
        this.nome = nome;
        this.campus = campus;
        this.notas = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public String getCampus() { return campus; }
    public List<Nota> getNotas() { return notas; }

    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
    }
}
