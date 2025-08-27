package com.example.projetosisu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Eixos
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Carregar dados
        SisuService service = new SisuService();
        List<Curso> cursos = service.carregarCursos();

        // Gerar gráfico
        MainController controller = new MainController();
        controller.gerarGraficoMedias(barChart, cursos);

        // Layout
        VBox root = new VBox(barChart);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("SISU - Análise Exploratória");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
