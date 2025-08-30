package com.example.projetosisu;

import javafx.collections.FXCollections;
import javafx.scene.chart.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.Tooltip;

public class MainController {

    // 🔹 Conjunto de códigos que significam Ampla Concorrência
    private static final Set<String> DEMANDAS_AMPLA = Set.of("AC", "A0");

    // 1) Top 10 cursos com maiores notas de corte (Ampla Concorrência)
    public void gerarGraficoTop10NotasCorteAmpla(LineChart<String, Number> chart, List<Curso> cursos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top 10 - Nota de Corte (Ampla Concorrência)");

        List<Map.Entry<String, Double>> notasCorte = cursos.stream()
                .map(curso -> {
                    double notaCorte = curso.getNotas().stream()
                            .filter(n -> DEMANDAS_AMPLA.contains(n.getDemanda().toUpperCase())) // 🔹 AC ou A0
                            .mapToDouble(Nota::getMedia)
                            .min()
                            .orElse(0);
                    return Map.entry(curso.getNome(), notaCorte);
                })
                .filter(e -> e.getValue() > 0)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .toList();

        for (Map.Entry<String, Double> entry : notasCorte) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().clear();
        chart.getData().add(series);

        chart.lookup(".default-color0.chart-series-line")
                .setStyle("-fx-stroke: blue; -fx-stroke-width: 2px;");
    }

    // 1b) Top 10 cursos com maiores notas de corte (Cotas)
    public void gerarGraficoTop10NotasCorteCotas(LineChart<String, Number> chart, List<Curso> cursos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top 10 - Nota de Corte (Cotas)");

        List<Map.Entry<String, Double>> notasCorte = cursos.stream()
                .map(curso -> {
                    double notaCorte = curso.getNotas().stream()
                            .filter(n -> !DEMANDAS_AMPLA.contains(n.getDemanda().toUpperCase())) // 🔹 só cotas
                            .mapToDouble(Nota::getMedia)
                            .min()
                            .orElse(0);
                    return Map.entry(curso.getNome(), notaCorte);
                })
                .filter(e -> e.getValue() > 0)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .toList();

        for (Map.Entry<String, Double> entry : notasCorte) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().clear();
        chart.getData().add(series);

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

        for (PieChart.Data data : chart.getData()) {
            String text = data.getName() + " (" + (int) data.getPieValue() + ")";
            Tooltip.install(data.getNode(), new Tooltip(text));
        }
    }

    // 3) Comparação Ampla Concorrência x Cotas
    public void gerarGraficoAmplaVsCotasTop10(BarChart<String, Number> chart, List<Curso> cursos) {
        List<Map.Entry<Curso, Double>> diffs = cursos.stream()
                .map(curso -> {
                    List<Double> ampla = curso.getNotas().stream()
                            .filter(n -> DEMANDAS_AMPLA.contains(n.getDemanda().toUpperCase()))
                            .map(Nota::getMedia)
                            .toList();

                    List<Double> cotas = curso.getNotas().stream()
                            .filter(n -> !DEMANDAS_AMPLA.contains(n.getDemanda().toUpperCase()))
                            .map(Nota::getMedia)
                            .toList();

                    double mediaAmpla = ampla.isEmpty() ? 0 : ampla.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    double mediaCotas = cotas.isEmpty() ? 0 : cotas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    double diff = Math.abs(mediaAmpla - mediaCotas);

                    return Map.entry(curso, diff);
                })
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .toList();

        XYChart.Series<String, Number> serieAmpla = new XYChart.Series<>();
        serieAmpla.setName("Ampla Concorrência");

        XYChart.Series<String, Number> serieCotas = new XYChart.Series<>();
        serieCotas.setName("Cotas");

        for (Map.Entry<Curso, Double> entry : diffs) {
            Curso curso = entry.getKey();

            List<Double> ampla = curso.getNotas().stream()
                    .filter(n -> DEMANDAS_AMPLA.contains(n.getDemanda().toUpperCase()))
                    .map(Nota::getMedia)
                    .toList();

            List<Double> cotas = curso.getNotas().stream()
                    .filter(n -> !DEMANDAS_AMPLA.contains(n.getDemanda().toUpperCase()))
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
