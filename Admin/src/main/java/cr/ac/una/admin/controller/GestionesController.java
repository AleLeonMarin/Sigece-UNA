package cr.ac.una.admin.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.admin.model.ApprovalsDto;
import cr.ac.una.admin.model.FollowsDto;
import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.util.AppContext;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Formato;
import javafx.fxml.Initializable;

public class GestionesController extends Controller implements Initializable {

    @FXML
    private MFXButton bntSearch;

    @FXML
    private MFXButton btnAdjuntarAprobacion;

    @FXML
    private MFXButton btnAdjuntarGestion;

    @FXML
    private MFXButton btnAdjuntarSeguimiento;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnExit;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXCheckbox chkActividad;

    @FXML
    private MFXCheckbox chkAprobacionGestion;

    @FXML
    private MFXCheckbox chkAprobadaAprobacion;

    @FXML
    private MFXCheckbox chkAprobadaGestion;

    @FXML
    private MFXCheckbox chkAprobadaSeguimiento;

    @FXML
    private MFXCheckbox chkAprobador1;

    @FXML
    private MFXCheckbox chkAprobador2;

    @FXML
    private MFXCheckbox chkAprobador4;

    @FXML
    private MFXCheckbox chkAprobador6;

    @FXML
    private MFXCheckbox chkArchivoSeguimiento;

    @FXML
    private MFXCheckbox chkCursoGestion;

    @FXML
    private MFXCheckbox chkCursoSeguimiento;

    @FXML
    private MFXCheckbox chkEsperaGestion;

    @FXML
    private MFXCheckbox chkRechazadaAprobacion;

    @FXML
    private MFXCheckbox chkRechazadaGestion;

    @FXML
    private MFXCheckbox chkRechazadaSeguimiento;

    @FXML
    private MFXCheckbox chkSubactividad;

    @FXML
    private MFXCheckbox chkTextoSeguimiento;

    @FXML
    private MFXComboBox<String> cmbActividades;

    @FXML
    private MFXComboBox<String> cmbAprobador1Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador2Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador3Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador4Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador5Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador6Gestion;

    @FXML
    private MFXComboBox<String> cmbAsiganadoGestion;

    @FXML
    private MFXComboBox<String> cmbSubactividades;

    @FXML
    private MFXDatePicker dpCreacionGestion;

    @FXML
    private MFXDatePicker dpFechaAprobacion;

    @FXML
    private MFXDatePicker dpFechaSeguimeinto;

    @FXML
    private MFXDatePicker dpSolucionGestion;

    @FXML
    private TabPane tbGestiones;

    @FXML
    private Tab tptAprobaciones;

    @FXML
    private Tab tptSeguimeinto;

    @FXML
    private Tab tptGestiones;

    @FXML
    private TextArea txaGestion;

    @FXML
    private TextArea txaSolucionAprobacion;

    @FXML
    private TextArea txaTextoSeguimiento;

    @FXML
    private MFXTextField txfAprobadorAprobacion;

    @FXML
    private MFXTextField txfAsuntoGestion;

    @FXML
    private MFXTextField txfComentarioAprobacion;

    @FXML
    private MFXTextField txfDescripcionAprobacion;

    @FXML
    private MFXTextField txfDescripcionSeguimiento;

    @FXML
    private MFXTextField txfIDAprobacion;

    @FXML
    private MFXTextField txfIDGestion;

    @FXML
    private MFXTextField txfIDSeguimiento;

    @FXML
    private MFXTextField txfSolicitanteGestion;

    @FXML
    private VBox vboxHistorial;

    UsersDto user;

    GestionsDto gestion;

    FollowsDto follow;

    ApprovalsDto approval;

    @Override
    public void initialize() {

        initValuesOfGestion();
    }

    // unique chkBox Selections

    private void selectionTypeOfGestion(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkActividad, chkSubactividad };
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }

    }

    private void selectType() {
        // Configura las acciones para cada CheckBox
        chkActividad.setOnAction(e -> {
            selectionTypeOfGestion(chkActividad); // Asegura que solo uno de los checkboxes esté seleccionado
            if (chkActividad.isSelected()) {
                cmbActividades.setDisable(false); // Activa el ComboBox de Actividades
                cmbActividades.setVisible(true); // Hace visible el ComboBox de Actividades
                cmbSubactividades.setDisable(true); // Desactiva el ComboBox de Subactividades
                cmbSubactividades.setVisible(false); // Oculta el ComboBox de Subactividades
            } else {
                cmbActividades.setDisable(true); // Desactiva y oculta el ComboBox de Actividades si se deselecciona
                cmbActividades.setVisible(false);
            }
        });

        chkSubactividad.setOnAction(e -> {
            selectionTypeOfGestion(chkSubactividad); // Asegura que solo uno de los checkboxes esté seleccionado
            if (chkSubactividad.isSelected()) {
                cmbSubactividades.setDisable(false); // Activa el ComboBox de Subactividades
                cmbSubactividades.setVisible(true); // Hace visible el ComboBox de Subactividades
                cmbActividades.setDisable(true); // Desactiva el ComboBox de Actividades
                cmbActividades.setVisible(false); // Oculta el ComboBox de Actividades
            } else {
                cmbSubactividades.setDisable(true); // Desactiva y oculta el ComboBox de Subactividades si se
                                                    // deselecciona
                cmbSubactividades.setVisible(false);
            }
        });
    }

    private void selectionTypeOfAprobacion(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkCursoGestion, chkEsperaGestion, chkAprobacionGestion, chkAprobadaGestion,
                chkRechazadaGestion };
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }
    }

    private void selectTypeOfAprobacion() {
        chkCursoGestion.setOnAction(e -> selectionTypeOfAprobacion(chkCursoGestion));
        chkEsperaGestion.setOnAction(e -> selectionTypeOfAprobacion(chkEsperaGestion));
        chkAprobacionGestion.setOnAction(e -> selectionTypeOfAprobacion(chkAprobacionGestion));
        chkAprobadaGestion.setOnAction(e -> selectionTypeOfAprobacion(chkAprobadaGestion));
        chkRechazadaGestion.setOnAction(e -> selectionTypeOfAprobacion(chkRechazadaGestion));
    }

    private void selectQuantityOfApprovers(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkAprobador1, chkAprobador2, chkAprobador4, chkAprobador6 };

        // Desmarcar todos los demás CheckBoxes
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }

        // Determinar el nivel seleccionado según el CheckBox marcado
        int selectedLevel = 0;
        if (chkAprobador1.isSelected()) {
            selectedLevel = 1;
        } else if (chkAprobador2.isSelected()) {
            selectedLevel = 2;
        } else if (chkAprobador4.isSelected()) {
            selectedLevel = 4;
        } else if (chkAprobador6.isSelected()) {
            selectedLevel = 6;
        }

        // Actualizar los ComboBox según el nivel seleccionado
        updateApproverComboBoxes(selectedLevel);
    }

    private void selectQuantityOfApprovers() {
        // Configura las acciones para cada CheckBox de aprobador
        chkAprobador1.setOnAction(e -> selectQuantityOfApprovers(chkAprobador1));
        chkAprobador2.setOnAction(e -> selectQuantityOfApprovers(chkAprobador2));
        chkAprobador4.setOnAction(e -> selectQuantityOfApprovers(chkAprobador4));
        chkAprobador6.setOnAction(e -> selectQuantityOfApprovers(chkAprobador6));

        // Llama a la función de actualización inicialmente para establecer el estado
        // correcto
        updateApproverComboBoxes(0); // 0 para deshabilitar todos inicialmente
    }

    private void updateApproverComboBoxes(int selectedLevel) {
        // Dependiendo del nivel seleccionado, habilita los ComboBox correspondientes
        cmbAprobador1Gestion.setDisable(selectedLevel < 1);
        cmbAprobador2Gestion.setDisable(selectedLevel < 2);
        cmbAprobador3Gestion.setDisable(selectedLevel < 3);
        cmbAprobador4Gestion.setDisable(selectedLevel < 4);
        cmbAprobador5Gestion.setDisable(selectedLevel < 5);
        cmbAprobador6Gestion.setDisable(selectedLevel < 6);

        // Controla la visibilidad de los ComboBox también
        cmbAprobador1Gestion.setVisible(selectedLevel >= 1);
        cmbAprobador2Gestion.setVisible(selectedLevel >= 2);
        cmbAprobador3Gestion.setVisible(selectedLevel >= 3);
        cmbAprobador4Gestion.setVisible(selectedLevel >= 4);
        cmbAprobador5Gestion.setVisible(selectedLevel >= 5);
        cmbAprobador6Gestion.setVisible(selectedLevel >= 6);

        // Si ningún CheckBox está seleccionado, deshabilita y oculta todos
        if (selectedLevel == 0) {
            cmbAprobador1Gestion.setDisable(true);
            cmbAprobador2Gestion.setDisable(true);
            cmbAprobador4Gestion.setDisable(true);
            cmbAprobador6Gestion.setDisable(true);

            cmbAprobador1Gestion.setVisible(false);
            cmbAprobador2Gestion.setVisible(false);
            cmbAprobador4Gestion.setVisible(false);
            cmbAprobador6Gestion.setVisible(false);
        }
    }

    private void initEntities() {
        // Inicialización de las entidades
        gestion = new GestionsDto();
        follow = new FollowsDto();
        approval = new ApprovalsDto();

    }

    private void initValuesOfGestion() {

        // Configuración inicial de los componentes

        selectType();
        selectTypeOfAprobacion();
        selectQuantityOfApprovers();

        // Configuración de los comboboxes
        cmbActividades.setVisible(false);
        cmbSubactividades.setVisible(false);

        cmbAprobador1Gestion.setVisible(false);
        cmbAprobador2Gestion.setVisible(false);
        cmbAprobador3Gestion.setVisible(false);
        cmbAprobador4Gestion.setVisible(false);
        cmbAprobador5Gestion.setVisible(false);
        cmbAprobador6Gestion.setVisible(false);

        // Configuración de los campos de texto

        user = (UsersDto) AppContext.getInstance().get("User");
        txfSolicitanteGestion.setText(user.getName() + " " + user.getLastNames());
        txfSolicitanteGestion.setEditable(false);

        txfAprobadorAprobacion.setText(user.getName() + " " + user.getLastNames());
        txfAprobadorAprobacion.setEditable(false);

    }

    // format method
    private void format() {
        // ID's
        txfIDGestion.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDAprobacion.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDSeguimiento.setTextFormatter(Formato.getInstance().integerFormat());

        // Descriptions
        txfDescripcionAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(200));
        txfDescripcionSeguimiento.setTextFormatter(Formato.getInstance().letrasFormat(200));
        txaGestion.setTextFormatter(Formato.getInstance().letrasFormat(1000));

        // Comments
        txfComentarioAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(100));

        // Subject
        txfAsuntoGestion.setTextFormatter(Formato.getInstance().letrasFormat(100));

        // Solution
        txaSolucionAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(1000));

    }

    // bindings

    private void bindGestion(Boolean isNew) {
        if (!isNew) {
            txfIDGestion.textProperty().bind(gestion.id);
        }
        txfAsuntoGestion.textProperty().bindBidirectional(gestion.subject);
        txaGestion.textProperty().bindBidirectional(gestion.description);
        dpCreacionGestion.valueProperty().bindBidirectional(gestion.creationDate);
        dpSolucionGestion.valueProperty().bindBidirectional(gestion.solutionDate);
        txfSolicitanteGestion.textProperty().bindBidirectional(gestion.Requester.name);
    }

    // unbinds

    private void unbindGestion() {
        txfIDGestion.textProperty().unbind();
        txfAsuntoGestion.textProperty().unbindBidirectional(gestion.subject);
        txaGestion.textProperty().unbindBidirectional(gestion.description);
        dpCreacionGestion.valueProperty().unbindBidirectional(gestion.creationDate);
        dpSolucionGestion.valueProperty().unbindBidirectional(gestion.solutionDate);
        txfSolicitanteGestion.textProperty().unbindBidirectional(gestion.Requester.name);
    }

    // new entity

    private void newGestion() {
        unbindGestion();
        gestion = new GestionsDto();
        bindGestion(true);
        txfIDGestion.clear();
        txfIDGestion.requestFocus();
    }

    @FXML
    void onActionBtnAdjuntarAprobacion(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        File file = fileChooser.showOpenDialog(this.getStage());

    }

    @FXML
    void onActionBtnAdjuntarGestion(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        File file = fileChooser.showOpenDialog(this.getStage());

    }

    @FXML
    void onActionBtnAdjuntarSeguimeinto(ActionEvent event) {

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

    }

    @FXML
    void onActionBtnExit(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("PrincipalView");
        this.getStage().close();
    }

    @FXML
    void onActionBtnNew(ActionEvent event) {

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {

    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {

    }

    @FXML
    void onKeyPressedTxfIDGestion(KeyEvent event) {

    }

    @FXML
    void onKeyPressedTxfIdSeguimiento(KeyEvent event) {

    }

    @FXML
    void onSelecitonChangedTptSeguimiento(Event event) {

    }

    @FXML
    void onSelectionChangedTptAprobaciones(Event event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        format();
        initEntities();

    }

}
