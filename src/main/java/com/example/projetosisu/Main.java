package com.example.projetosisu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione o arquivo de dados");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Arquivo não selecionado");
            alert.setHeaderText(null);
            alert.setContentText("Você deve selecionar um arquivo para continuar.");
            alert.showAndWait();
            primaryStage.close();
            return;
        }
        SisuService service = new SisuService();
        List<Curso> cursos = service.carregarCursosDeArquivo(selectedFile);

        MainController controller = new MainController();

        // Gráfico 1: Top 10 Corte (Ampla)
        CategoryAxis xAxis1 = new CategoryAxis();
        NumberAxis yAxis1 = new NumberAxis();
        yAxis1.setAutoRanging(false);
        yAxis1.setLowerBound(0);
        yAxis1.setUpperBound(1000);
        yAxis1.setTickUnit(50);
        LineChart<String, Number> chartTop10 = new LineChart<>(xAxis1, yAxis1);
        controller.gerarGraficoTop10NotasCorteAmpla(chartTop10, cursos);

        // Gráfico 2: Top 10 Notas de Corte (Cotas)
        CategoryAxis xAxis4 = new CategoryAxis();
        NumberAxis yAxis4 = new NumberAxis();
        yAxis4.setAutoRanging(false);
        yAxis4.setLowerBound(0);
        yAxis4.setUpperBound(1000);
        yAxis4.setTickUnit(50);
        LineChart<String, Number> chartCotas = new LineChart<>(xAxis4, yAxis4);
        controller.gerarGraficoTop10NotasCorteCotas(chartCotas, cursos);

        // Gráfico 3: Distribuição por Campus
        PieChart chartCampus = new PieChart();
        controller.gerarGraficoCampus(chartCampus, cursos);

        // Gráfico 4: Ampla vs Cotas
        CategoryAxis xAxis2 = new CategoryAxis();
        NumberAxis yAxis2 = new NumberAxis();
        BarChart<String, Number> chartAmplaCotas = new BarChart<>(xAxis2, yAxis2);
        controller.gerarGraficoAmplaVsCotasTop10(chartAmplaCotas, cursos);

        // Configurar Abas
        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Top 10 Notas de Corte (Ampla)", chartTop10);
        Tab tab2 = new Tab("Top 10 Notas de Corte (Cotas)", chartCotas);
        Tab tab3 = new Tab("Distribuição por Campus", chartCampus);
        Tab tab4 = new Tab("Ampla x Cotas (Top 10 Diferenças)", chartAmplaCotas);

        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);
        tab4.setClosable(false);

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);

        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setTitle("SISU - Análise Exploratória");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}