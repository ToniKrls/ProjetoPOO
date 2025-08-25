package com.example.projetosisu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SisuService {

    public List<Curso> carregarCursos(String caminho) {
        List<Curso> cursos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            br.readLine(); // pula cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] colunas = linha.split(";");
                // Validação de dados
                if (colunas.length < 8) {
                    mostrarAlerta("Dados inválidos na linha: " + linha);
                    continue; // Pula para a próxima linha
                }
                try {
                    String nome = colunas[0];
                    int vagas = Integer.parseInt(colunas[1]);
                    int candidatos = Integer.parseInt(colunas[2]);
                    int ano = Integer.parseInt(colunas[3]);
                    double min = Double.parseDouble(colunas[4]);
                    double med = Double.parseDouble(colunas[5]);
                    double ampla = Double.parseDouble(colunas[6]);
                    double cotas = Double.parseDouble(colunas[7]);
                    Curso curso = encontrarOuCriar(cursos, nome, vagas, candidatos);
                    curso.adicionarNota(new Nota(ano, min, med, ampla, cotas));
                } catch (NumberFormatException e) {
                    mostrarAlerta("Erro de formatação de número na linha: " + linha);
                }
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
        return cursos;
    }

    private Curso encontrarOuCriar(List<Curso> cursos, String nome, int vagas, int candidatos) {
        for (Curso c : cursos) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        Curso novo = new Curso(nome, vagas, candidatos);
        cursos.add(novo);
        return novo;
    }
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
