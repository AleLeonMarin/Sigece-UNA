module cr.ac.una.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphicsEmpty;
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
    requires com.calendarfx.view;

    opens cr.ac.una.admin.model;

    opens cr.ac.una.admin to javafx.fxml;

    exports cr.ac.una.admin;

    opens cr.ac.una.admin.controller to javafx.fxml, MaterialFX, javafx.graphics, javafx.controls;

}
