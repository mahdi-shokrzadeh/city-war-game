module org.example.citywars {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.databind;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;

    opens org.example.citywars;
    opens models to com.fasterxml.jackson.databind;
    opens models.card to com.fasterxml.jackson.databind;
    opens models.game to com.fasterxml.jackson.databind;
    exports org.example.citywars;
    exports models to com.fasterxml.jackson.databind;
    exports models.card to com.fasterxml.jackson.databind;
    exports models.game to com.fasterxml.jackson.databind;
}