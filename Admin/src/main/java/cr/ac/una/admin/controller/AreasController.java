package cr.ac.una.admin.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.admin.model.ActivitiesDto;
import cr.ac.una.admin.model.AreasDto;
import cr.ac.una.admin.model.SubactivitiesDto;
import cr.ac.una.admin.service.ActivitiesService;
import cr.ac.una.admin.service.AreasService;
import cr.ac.una.admin.service.SubactivitiesService;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Formato;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.List;

public class AreasController extends Controller implements Initializable {

    @FXML
    private MFXButton bntExit;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXCheckbox chkState;

    @FXML
    private TableColumn<ActivitiesDto, String> tbcIDAct;

    @FXML
    private TableColumn<AreasDto, String> tbcIDArea;

    @FXML
    private TableColumn<SubactivitiesDto, String> tbcIDSub;

    @FXML
    private TableColumn<ActivitiesDto, String> tbcNombreAct;

    @FXML
    private TableColumn<AreasDto, String> tbcNombreArea;

    @FXML
    private TableColumn<SubactivitiesDto, String> tbcNombreSub;

    @FXML
    private TableView<ActivitiesDto> tbvActividades;

    @FXML
    private TableView<AreasDto> tbvAreas;

    @FXML
    private TableView<SubactivitiesDto> tbvSubactividades;

    @FXML
    private MFXTextField txfIDAct;

    @FXML
    private MFXTextField txfIDArea;

    @FXML
    private MFXTextField txfIDSub;

    @FXML
    private MFXTextField txfNombreAct;

    @FXML
    private MFXTextField txfNombreArea;

    @FXML
    private MFXTextField txfNombreSub;

    @FXML
    private Tab tptActividades;

    @FXML
    private Tab tptAreas;

    @FXML
    private Tab tptSubactividades;

    @FXML
    private TabPane tpbAreas;

    AreasDto area;

    ActivitiesDto activity;

    SubactivitiesDto subactivity;

    @Override
    public void initialize() {
        chargeArea();
    }

    // Table load methods

    private void loadTableAreas() {
        tbcIDArea.setCellValueFactory(cd -> cd.getValue().id);
        tbcNombreArea.setCellValueFactory(cd -> cd.getValue().name);

    }

    private void loadTableActivities() {
        tbcIDAct.setCellValueFactory(cd -> cd.getValue().id);
        tbcNombreAct.setCellValueFactory(cd -> cd.getValue().name);
    }

    private void loadTableSubactivities() {
        tbcIDSub.setCellValueFactory(cd -> cd.getValue().id);
        tbcNombreSub.setCellValueFactory(cd -> cd.getValue().name);
    }

    // bind methods

    private void bindAreas(Boolean isNew) {

        if (!isNew) {
            txfIDArea.textProperty().bind(area.id);
        }
        txfNombreArea.textProperty().bindBidirectional(area.name);
        chkState.selectedProperty().bindBidirectional(area.state);
    }

    private void bindActivities(Boolean isNew) {
        if (!isNew) {
            txfIDAct.textProperty().bind(this.activity.id);
        }
        txfNombreAct.textProperty().bindBidirectional(this.activity.name);

    }

    private void bindSubactivities(Boolean isNew) {
        if (!isNew) {
            txfIDSub.textProperty().bind(this.subactivity.id);
        }
        txfNombreSub.textProperty().bindBidirectional(this.subactivity.name);

    }

    // unbind methods

    private void unbindAreas() {
        txfIDArea.textProperty().unbind();
        txfNombreArea.textProperty().unbindBidirectional(area.name);
        chkState.selectedProperty().unbindBidirectional(area.state);

    }

    private void unbindActivities() {
        txfIDAct.textProperty().unbind();
        txfNombreAct.textProperty().unbindBidirectional(this.activity.name);

    }

    private void unbindSubactivities() {
        txfIDSub.textProperty().unbind();
        txfNombreSub.textProperty().unbindBidirectional(this.subactivity.name);

    }

    // clear methods

    private void newArea() {

        unbindAreas();
        area = new AreasDto();
        bindAreas(true);
        txfIDArea.clear();
        txfIDArea.requestFocus();

    }

    private void newActivity() {

        unbindActivities();
        activity = new ActivitiesDto();
        bindActivities(true);
        txfIDAct.clear();
        txfIDAct.requestFocus();

    }

    private void newSubactivity() {

        unbindSubactivities();
        subactivity = new SubactivitiesDto();
        bindSubactivities(true);
        txfIDSub.clear();
        txfIDSub.requestFocus();

    }

    // charging methods

    private void chargeArea() {
        try {
            AreasService service = new AreasService();
            Respuesta respuesta = service.getAreas();
            if (respuesta.getEstado()) {
                tbvAreas.getItems().clear();
                List<AreasDto> areas = (List<AreasDto>) respuesta.getResultado("Areas");
                tbvAreas.getItems().addAll(areas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void chargeArea(Long id) {
        try {
            AreasService service = new AreasService();
            Respuesta respuesta = service.getArea(id);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Cargar Area", getStage(), respuesta.getMensaje());
            } else {
                unbindAreas();
                this.area = (AreasDto) respuesta.getResultado("Area");
                bindAreas(false);

                ObservableList<ActivitiesDto> activities = FXCollections.observableArrayList(this.area.getActivities());
                tbvActividades.setItems(activities);
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error cargando el area", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar area", getStage(), "Error cargando el area.");
        }

    }

    private void chargeActivity(Long id) {
        try {
            ActivitiesService service = new ActivitiesService();
            Respuesta respuesta = service.getActivity(id);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Cargar Actividad", getStage(), respuesta.getMensaje());
            } else {
                unbindActivities();
                this.activity = (ActivitiesDto) respuesta.getResultado("Activity");
                bindActivities(false);

                ObservableList<SubactivitiesDto> subactivities = FXCollections
                        .observableArrayList(this.activity.getSubactivities());
                tbvSubactividades.setItems(subactivities);
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error cargando la actividad", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Actividad", getStage(), "Error cargando la actividad.");
        }

    }

    // populate txf methods

    private void populateTxfArea() {
        AreasDto area = tbvAreas.getSelectionModel().getSelectedItem();
        if (area != null) {
            unbindAreas();
            this.area = area;
            bindAreas(false);
        }
    }

    private void populateTxfActivity() {
        ActivitiesDto activity = tbvActividades.getSelectionModel().getSelectedItem();
        if (activity != null) {
            unbindActivities();
            this.activity = activity;
            bindActivities(false);
        }
    }

    private void populateTxfSubactivity() {
        SubactivitiesDto subactivity = tbvSubactividades.getSelectionModel().getSelectedItem();
        if (subactivity != null) {
            unbindSubactivities();
            this.subactivity = subactivity;
            bindSubactivities(false);
        }
    }

    // save methods

    private void saveArea() {
        try {
            AreasService service = new AreasService();
            Respuesta respuesta = service.createArea(area);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Guardar Area", getStage(), respuesta.getMensaje());
            } else {
                unbindAreas();
                this.area = (AreasDto) respuesta.getResultado("Area");
                bindAreas(false);
                chargeArea();
                new Mensaje().showModal(AlertType.INFORMATION, "Guardar Area", getStage(),
                        "Area guardada correctamente.");
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error guardando el area", e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Area", getStage(), "Error guardando el area.");
        }
    }

    private void saveActivity() {
        try {
            if (activity.getArea() == null) {
                activity.setArea(area);
            }
            ActivitiesService service = new ActivitiesService();
            Respuesta respuesta = service.createActivity(activity);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Guardar Actividad", getStage(), respuesta.getMensaje());
            } else {
                unbindActivities();
                this.activity = (ActivitiesDto) respuesta.getResultado("Activity");
                bindActivities(false);
                chargeArea();
                chargeArea(Long.valueOf(txfIDArea.getText()));
                new Mensaje().showModal(AlertType.INFORMATION, "Guardar Actividad", getStage(),
                        "Actividad guardada correctamente.");
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error guardando la actividad", e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Actividad", getStage(), "Error guardando la actividad.");
        }
    }

    private void saveSubactivity() {
        try {
            if (subactivity.getActivity() == null) {
                subactivity.setActivity(activity);
            }
            SubactivitiesService service = new SubactivitiesService();
            Respuesta respuesta = service.createSubactivity(subactivity);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Guardar Subactividad", getStage(), respuesta.getMensaje());
            } else {
                unbindSubactivities();
                this.subactivity = (SubactivitiesDto) respuesta.getResultado("Subactivity");
                bindSubactivities(false);
                chargeArea();
                chargeArea(Long.valueOf(txfIDArea.getText()));
                chargeActivity(Long.valueOf(txfIDAct.getText()));
                new Mensaje().showModal(AlertType.INFORMATION, "Guardar Subactividad", getStage(),
                        "Subactividad guardada correctamente.");
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error guardando la subactividad", e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Subactividad", getStage(),
                    "Error guardando la subactividad.");
        }

    }

    // delete methods

    private void deleteArea() {
        try {
            AreasService service = new AreasService();
            Respuesta respuesta = service.deleteArea(Long.valueOf(txfIDArea.getText()));
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Eliminar Area", getStage(), respuesta.getMensaje());
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, "Eliminar Area", getStage(),
                        "Area eliminada correctamente.");
                chargeArea();
                newArea();
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error eliminando el area", e);
            new Mensaje().showModal(AlertType.ERROR, "Eliminar Area", getStage(), "Error eliminando el area.");
        }
    }

    private void deleteActivity() {
        try {
            ActivitiesService service = new ActivitiesService();
            Respuesta respuesta = service.deleteActivities(Long.valueOf(txfIDAct.getText()));
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Eliminar Actividad", getStage(), respuesta.getMensaje());
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, "Eliminar Actividad", getStage(),
                        "Actividad eliminada correctamente.");
                chargeArea(Long.valueOf(txfIDArea.getText()));
                newActivity();
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error eliminando la actividad", e);
            new Mensaje().showModal(AlertType.ERROR, "Eliminar Actividad", getStage(),
                    "Error eliminando la actividad.");
        }

    }

    private void deleteSubactivity() {
        try {
            SubactivitiesService service = new SubactivitiesService();
            Respuesta respuesta = service.deleteSubactivities(Long.valueOf(txfIDSub.getText()));
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Eliminar Subactividad", getStage(), respuesta.getMensaje());
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, "Eliminar Subactividad", getStage(),
                        "Subactividad eliminada correctamente.");
                chargeActivity(Long.valueOf(txfIDAct.getText()));
                newSubactivity();
            }
        } catch (Exception e) {
            Logger.getLogger(AreasController.class.getName()).log(Level.SEVERE, "Error eliminando la subactividad", e);
            new Mensaje().showModal(AlertType.ERROR, "Eliminar Subactividad", getStage(),
                    "Error eliminando la subactividad.");
        }

    }

    // listeners

    private void listernerAreas() {
        tbvAreas.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends AreasDto> observable, AreasDto oldValue, AreasDto newValue) -> {
                    if (newValue != null) {
                        this.area = newValue;
                        populateTxfArea();
                        chargeArea(Long.valueOf(txfIDArea.getText()));

                        Platform.runLater(() -> tbvAreas.getSelectionModel().clearSelection());
                    }
                });
    }

    private void listenerActivities() {
        tbvActividades.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends ActivitiesDto> observable, ActivitiesDto oldValue,
                        ActivitiesDto newValue) -> {
                    if (newValue != null) {
                        this.activity = newValue;
                        populateTxfActivity();
                        chargeActivity(Long.valueOf(txfIDAct.getText()));

                        Platform.runLater(() -> tbvActividades.getSelectionModel().clearSelection());
                    }
                });
    }

    private void listenerSubactivities() {
        tbvSubactividades.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends SubactivitiesDto> observable, SubactivitiesDto oldValue,
                        SubactivitiesDto newValue) -> {
                    if (newValue != null) {
                        this.subactivity = newValue;
                        populateTxfSubactivity();

                        Platform.runLater(() -> tbvSubactividades.getSelectionModel().clearSelection());
                    }
                });
    }

    // format method

    private void formatTxf() {
        // ID's
        txfIDArea.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDAct.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDSub.setTextFormatter(Formato.getInstance().integerFormat());

        // Names
        txfNombreArea.setTextFormatter(Formato.getInstance().maxLengthFormat(100));
        txfNombreAct.setTextFormatter(Formato.getInstance().maxLengthFormat(100));
        txfNombreSub.setTextFormatter(Formato.getInstance().maxLengthFormat(100));
    }

    @FXML
    void onActionBntSave(ActionEvent event) {
        if (tptAreas.isSelected()) {
            saveArea();
        } else if (tptActividades.isSelected()) {
            saveActivity();
        } else if (tptSubactividades.isSelected()) {
            saveSubactivity();
        }

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

        if (tptAreas.isSelected()) {
            if (new Mensaje().showConfirmation("Eliminar Area", getStage(), "Desea eliminar el area?")) {
                deleteArea();
            }
        } else if (tptActividades.isSelected()) {
            if (new Mensaje().showConfirmation("Eliminar Actividad", getStage(), "Desea eliminar la actividad?")) {
                deleteActivity();
            }
        } else if (tptSubactividades.isSelected()) {
            if (new Mensaje().showConfirmation("Eliminar Subactividad", getStage(),
                    "Desea eliminar la subactividad?")) {
                deleteSubactivity();
            }
        }

    }

    @FXML
    void onActionBtnExit(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("PrincipalView");
        this.getStage().close();

    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (tptAreas.isSelected()) {
            if (new Mensaje().showConfirmation("Nueva Area", getStage(), "Desea crear una nueva area?")) {
                newArea();
            }
        } else if (tptActividades.isSelected()) {
            if (new Mensaje().showConfirmation("Nueva Actividad", getStage(), "Desea crear una nueva actividad?")) {
                newActivity();
            }
        } else if (tptSubactividades.isSelected()) {
            if (new Mensaje().showConfirmation("Nueva Subactividad", getStage(),
                    "Desea crear una nueva subactividad?")) {
                newSubactivity();
            }
        }
    }

    @FXML
    void onKeyPressedTxfIDArea(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER && !txfIDArea.getText().isBlank()) {
            chargeArea(Long.valueOf(txfIDArea.getText()));
        }

    }

    @FXML
    void onKeyPressedTxfIdAct(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER && !txfIDAct.getText().isBlank()) {
            chargeActivity(Long.valueOf(txfIDAct.getText()));
        }

    }

    @FXML
    void onKeyPressedTxfIdSub(KeyEvent event) {

    }

    @FXML
    void onSelectionChangedTptSubactividades(Event event) {
        if (tptSubactividades.isSelected()) {
            if (this.activity.getId() == null) {
                new Mensaje().showModal(AlertType.ERROR, "Seleccionar Actividad", getStage(),
                        "Debe seleccionar una actividad.");
                tpbAreas.getSelectionModel().select(tptActividades);
            }
        }

    }

    @FXML
    void onSelectionChangedTptActividades(Event event) {
        if (tptActividades.isSelected()) {
            if (this.area.getId() == null) {
                new Mensaje().showModal(AlertType.ERROR, "Seleccionar Area", getStage(), "Debe seleccionar un area.");
                tpbAreas.getSelectionModel().select(tptAreas);
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // format textfields
        formatTxf();

        // initialize clasess
        area = new AreasDto();
        activity = new ActivitiesDto();
        subactivity = new SubactivitiesDto();

        // load tables
        loadTableAreas();
        loadTableActivities();
        loadTableSubactivities();

        // news

        newArea();
        newActivity();
        newSubactivity();

        // listeners

        listernerAreas();
        listenerActivities();
        listenerSubactivities();

    }

}
