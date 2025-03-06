module com.example.astrojumppseudocode {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.astrojumppseudocode to javafx.fxml;
    exports com.example.astrojumppseudocode;
}