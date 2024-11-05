package cr.ac.una.admin.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.service.GestionService;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import java.util.stream.Collectors;

public class SpecificSearchViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnExit;

    @FXML
    private MFXButton btnFilter;

    @FXML
    private MFXButton btnLoad;

    @FXML
    private TableColumn<GestionsDto, String> tbcAsignado;

    @FXML
    private TableColumn<GestionsDto, String> tbcAsunto;

    @FXML
    private TableColumn<GestionsDto, String> tbcDescripcion;

    @FXML
    private TableView<GestionsDto> tbvGestiones;

    @FXML
    private MFXTextField txfAsignado;

    @FXML
    private MFXTextField txfAsunto;

    @FXML
    private MFXTextField txfDescripcion;

    Object result;

    GestionService service;

    ObservableList<GestionsDto> gestiones;

    @SuppressWarnings("unchecked")
    private void loadGestions(String asignado, String asunto, String descripcion) {

        Respuesta res = service.getGestiones();
        if (res.getEstado()) {
            ObservableList<GestionsDto> gestionesService = FXCollections
                    .observableArrayList((List<GestionsDto>) res.getResultado("Gestiones"));
            gestiones.clear();

            // Aplicar los filtros
            if (asignado != null && !asignado.isEmpty()) {
                String asignadoLower = asignado.toLowerCase();
                gestionesService = gestionesService
                        .filtered(p -> p.getAssigned().getName().toLowerCase().contains(asignadoLower));
            }
            if (asunto != null && !asunto.isEmpty()) {
                String asuntoLower = asunto.toLowerCase();
                gestionesService = gestionesService.filtered(p -> p.getSubject().toLowerCase().contains(asuntoLower));
            }
            if (descripcion != null && !descripcion.isEmpty()) {
                String descripcionLower = descripcion.toLowerCase();
                gestionesService = gestionesService
                        .filtered(p -> p.getDescription().toLowerCase().contains(descripcionLower));
            }

            gestiones.addAll(gestionesService);
            tbvGestiones.setItems(gestiones);
            tbvGestiones.refresh();

        } else {
            new Mensaje().showModal(AlertType.ERROR, "Error en carga de gestiones", this.getStage(),
                    res.getMensaje());
        }
    }

    @FXML
    void onActionBtnExit(ActionEvent event) {

        this.getStage().close();

    }

    @FXML
    void onActionBtnFilter(ActionEvent event) {
        loadGestions(txfAsignado.getText(), txfAsunto.getText(), txfDescripcion.getText());
    }

    @FXML
    void onActionBtnLoad(ActionEvent event) {

        result = tbvGestiones.getSelectionModel().getSelectedItem();
        this.getStage().close();

    }

    @FXML
    void onMousePressedTbvGestiones(MouseEvent event) {

        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            onActionBtnLoad(null);
        }

    }

    public Object getResult() {
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tbcAsignado.setCellValueFactory(cd -> cd.getValue().Assigned.name);
        tbcAsunto.setCellValueFactory(cd -> cd.getValue().subject);
        tbcDescripcion.setCellValueFactory(cd -> cd.getValue().description);

    }

    @Override
    public void initialize() {

        gestiones = FXCollections.observableArrayList();
        service = new GestionService();

    }

}
