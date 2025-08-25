package com.example.projetosisu;

public class Nota {
    private int ano;
    private double minima;
    private double media;
    private double ampla;
    private double cotas;

    public Nota(int ano, double minima, double media, double ampla, double cotas) {
        this.ano = ano;
        this.minima = minima;
        this.media = media;
        this.ampla = ampla;
        this.cotas = cotas;
    }

    public int getAno() { return ano; }
    public double getMinima() { return minima; }
    public double getMedia() { return media; }
    public double getAmpla() { return ampla; }
    public double getCotas() { return cotas; }
}
