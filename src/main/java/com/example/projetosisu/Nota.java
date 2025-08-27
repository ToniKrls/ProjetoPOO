package com.example.projetosisu;

public class Nota {
    private int ano;
    private String demanda;   // Ampla concorrência, cotas, etc.
    private double media;
    private int colocacao;

    public Nota(int ano, String demanda, double media, int colocacao) {
        this.ano = ano;
        this.demanda = demanda;
        this.media = media;
        this.colocacao = colocacao;
    }

    public int getAno() { return ano; }
    public String getDemanda() { return demanda; }
    public double getMedia() { return media; }
    public int getColocacao() { return colocacao; }
}
