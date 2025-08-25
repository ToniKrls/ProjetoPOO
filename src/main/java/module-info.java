module com.example.projetosisu {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projetosisu to javafx.fxml;
    exports com.example.projetosisu;
}