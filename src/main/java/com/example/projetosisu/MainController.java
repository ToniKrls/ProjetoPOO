package com.example.projetosisu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.util.List;

public class MainController {

    public void gerarGraficoVagas(BarChart<String, Number> chart, List<Curso> cursos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Número de Vagas");

        for (Curso curso : cursos) {
            series.getData().add(new XYChart.Data<>(curso.getNome(), curso.getVagas()));
        }

        chart.getData().clear();
        chart.getData().add(series);
    }
}
