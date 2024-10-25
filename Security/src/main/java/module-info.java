module cr.ac.una.security {

    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires jakarta.ws.rs;
    requires java.logging;
    requires jakarta.json;
    requires javassist;
    requires java.base;
    requires java.sql;
    requires jakarta.xml.bind;
    requires jakarta.json.bind;
    requires jersey.common;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.swing;
    requires webcam.capture;

    opens cr.ac.una.security.model;

    opens cr.ac.una.security to javafx.fxml;
    exports cr.ac.una.security;

    opens cr.ac.una.security.controller to javafx.fxml, MaterialFX, javafx.graphics, javafx.controls;

}
