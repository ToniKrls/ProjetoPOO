package com.example.projetosisu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.util.List;

public class MainController {

    public void gerarGraficoMedias(BarChart<String, Number> chart, List<Curso> cursos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nota Média dos Cursos");

        for (Curso curso : cursos) {
            // calcular média das médias dos aprovados do curso
            double soma = 0;
            for (Nota nota : curso.getNotas()) {
                soma += nota.getMedia();
            }
            double mediaCurso = curso.getNotas().isEmpty() ? 0 : soma / curso.getNotas().size();

            series.getData().add(new XYChart.Data<>(curso.getNome(), mediaCurso));
        }

        chart.getData().clear();
        chart.getData().add(series);
    }

}
