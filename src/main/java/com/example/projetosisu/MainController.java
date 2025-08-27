package com.example.projetosisu;

import javafx.collections.FXCollections;
import javafx.scene.chart.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.Tooltip;


public class MainController {

    // 1) Top 10 cursos com maiores médias
    // Top 10 cursos com maiores notas de corte (apenas Ampla Concorrência)
    public void gerarGraficoTop10NotasCorteAmpla(LineChart<String, Number> chart, List<Curso> cursos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top 10 - Nota de Corte (Ampla Concorrência)");

        // calcular nota de corte apenas para candidatos da ampla concorrência (AC)
        List<Map.Entry<String, Double>> notasCorte = cursos.stream()
                .map(curso -> {
                    double notaCorte = curso.getNotas().stream()
                            .filter(n -> n.getDemanda().equalsIgnoreCase("AC")) // 🔹 só ampla
                            .mapToDouble(Nota::getMedia)
                            .min()
                            .orElse(0);
                    return Map.entry(curso.getNome(), notaCorte);
                })
                .filter(e -> e.getValue() > 0) // remove cursos sem AC
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // maiores primeiro
                .limit(10)
                .toList();

        for (Map.Entry<String, Double> entry : notasCorte) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().clear();
        chart.getData().add(series);

        // estilizar a linha azul
        chart.lookup(".default-color0.chart-series-line")
                .setStyle("-fx-stroke: blue; -fx-stroke-width: 2px;");
    }

    // Top 10 cursos com maiores notas de corte (apenas Cotas)
    public void gerarGraficoTop10NotasCorteCotas(LineChart<String, Number> chart, List<Curso> cursos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top 10 - Nota de Corte (Cotas)");

        // calcular nota de corte apenas para candidatos de cotas (não AC)
        List<Map.Entry<String, Double>> notasCorte = cursos.stream()
                .map(curso -> {
                    double notaCorte = curso.getNotas().stream()
                            .filter(n -> !n.getDemanda().equalsIgnoreCase("AC")) // 🔹 só cotas
                            .mapToDouble(Nota::getMedia)
                            .min()
                            .orElse(0);
                    return Map.entry(curso.getNome(), notaCorte);
                })
                .filter(e -> e.getValue() > 0) // remove cursos sem cotas
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // maiores primeiro
                .limit(10)
                .toList();

        for (Map.Entry<String, Double> entry : notasCorte) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().clear();
        chart.getData().add(series);

        // estilizar a linha vermelha
        chart.lookup(".default-color0.chart-series-line")
                .setStyle("-fx-stroke: red; -fx-stroke-width: 2px;");
    }

    // 2) Gráfico de pizza - distribuição por campus
    public void gerarGraficoCampus(PieChart chart, List<Curso> cursos) {
        Map<String, Long> contagem = cursos.stream()
                .collect(Collectors.groupingBy(Curso::getCampus, Collectors.counting()));

        chart.getData().clear();
        for (Map.Entry<String, Long> entry : contagem.entrySet()) {
            chart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // 🔹 Adicionar tooltip em cada fatia
        for (PieChart.Data data : chart.getData()) {
            String text = data.getName() + " (" + (int) data.getPieValue() + ")";
            Tooltip.install(data.getNode(), new Tooltip(text));
        }
    }

    // 3) Comparação Ampla Concorrência x Cotas
    public void gerarGraficoAmplaVsCotasTop10(BarChart<String, Number> chart, List<Curso> cursos) {
        // lista com nome do curso e diferença absoluta entre AC e Cotas
        List<Map.Entry<Curso, Double>> diffs = cursos.stream()
                .map(curso -> {
                    List<Double> ampla = curso.getNotas().stream()
                            .filter(n -> n.getDemanda().equalsIgnoreCase("AC"))
                            .map(Nota::getMedia)
                            .toList();

                    List<Double> cotas = curso.getNotas().stream()
                            .filter(n -> !n.getDemanda().equalsIgnoreCase("AC"))
                            .map(Nota::getMedia)
                            .toList();

                    double mediaAmpla = ampla.isEmpty() ? 0 : ampla.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    double mediaCotas = cotas.isEmpty() ? 0 : cotas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    double diff = Math.abs(mediaAmpla - mediaCotas);

                    return Map.entry(curso, diff);
                })
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // maior diferença primeiro
                .limit(10) // pega só 10 cursos
                .toList();

        XYChart.Series<String, Number> serieAmpla = new XYChart.Series<>();
        serieAmpla.setName("Ampla Concorrência");

        XYChart.Series<String, Number> serieCotas = new XYChart.Series<>();
        serieCotas.setName("Cotas");

        for (Map.Entry<Curso, Double> entry : diffs) {
            Curso curso = entry.getKey();

            List<Double> ampla = curso.getNotas().stream()
                    .filter(n -> n.getDemanda().equalsIgnoreCase("AC"))
                    .map(Nota::getMedia)
                    .toList();

            List<Double> cotas = curso.getNotas().stream()
                    .filter(n -> !n.getDemanda().equalsIgnoreCase("AC"))
                    .map(Nota::getMedia)
                    .toList();

            double mediaAmpla = ampla.isEmpty() ? 0 : ampla.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            double mediaCotas = cotas.isEmpty() ? 0 : cotas.stream().mapToDouble(Double::doubleValue).average().orElse(0);

            serieAmpla.getData().add(new XYChart.Data<>(curso.getNome(), mediaAmpla));
            serieCotas.getData().add(new XYChart.Data<>(curso.getNome(), mediaCotas));
        }

        chart.getData().clear();
        chart.getData().addAll(serieAmpla, serieCotas);
    }
}
