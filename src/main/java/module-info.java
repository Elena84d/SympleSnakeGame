module com.example.snake {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.snake to javafx.fxml;
    exports com.example.snake;
}