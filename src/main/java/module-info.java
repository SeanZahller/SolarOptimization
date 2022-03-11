module com.example.solaroptimization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;
    requires jim3dsModelImporterJFX;
    requires sunset1;
    requires java.desktop;

    opens com.example.solaroptimization to javafx.fxml;
    exports com.example.solaroptimization;
}