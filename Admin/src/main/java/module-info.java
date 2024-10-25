module cr.ac.una.admin {
    requires javafx.controls;
    requires javafx.fxml;

    opens cr.ac.una.admin to javafx.fxml;
    exports cr.ac.una.admin;
}
