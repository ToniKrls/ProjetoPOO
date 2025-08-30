package com.example.projetosisu;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SisuService {

    // Carregar Arquivo
    public List<Curso> carregarCursosDeArquivo(File arquivo) {
        List<Curso> cursos = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(arquivo);
             Scanner scanner = new Scanner(fis)) {

            if (!scanner.hasNextLine()) {
                mostrarAlerta("Arquivo vazio.");
                return cursos;
            }

            boolean dadosIniciaram = false;

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();

                if (linha.contains("N. ENEM") && linha.contains("Curso")) {
                    dadosIniciaram = true;
                    continue;
                }
                if (!dadosIniciaram || linha.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] colunas = linha.trim().split("\\s{2,}");
                    if (colunas.length < 7) continue;

                    String cursoNome = colunas[2].trim();
                    String campus = colunas[3].trim();
                    String demanda = colunas[4].trim();
                    double media = Double.parseDouble(colunas[5].replace(",", "."));
                    int colocacao = Integer.parseInt(colunas[6].replace("º", "").trim());

                    Curso curso = encontrarOuCriar(cursos, cursoNome, campus);
                    Nota nota = new Nota(2024, demanda, media, colocacao);
                    curso.adicionarNota(nota);

                } catch (Exception e) {
                    mostrarAlerta("Erro ao processar linha: " + linha);
                }
            }

        } catch (Exception e) {
            mostrarAlerta("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        return cursos;
    }


    // Procura Um Curso Na Lista Ou Cria Um
    private Curso encontrarOuCriar(List<Curso> cursos, String nome, String campus) {
        for (Curso c : cursos) {
            if (c.getNome().equalsIgnoreCase(nome) && c.getCampus().equalsIgnoreCase(campus)) {
                return c;
            }
        }
        Curso novo = new Curso(nome, campus);
        cursos.add(novo);
        return novo;
    }


    // Alerta de Erro
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}