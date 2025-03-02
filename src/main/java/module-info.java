module com.example.astrojumppseudocode {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.astrojumppseudocode to javafx.fxml;
    exports com.example.astrojumppseudocode;
}