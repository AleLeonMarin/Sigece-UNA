package cr.ac.una.admin.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import cr.ac.una.admin.model.ActivitiesDto;
import cr.ac.una.admin.model.ApprovalsDto;
import cr.ac.una.admin.model.FollowsDto;
import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.model.SubactivitiesDto;
import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.ActivitiesService;
import cr.ac.una.admin.service.GestionService;
import cr.ac.una.admin.service.SubactivitiesService;
import cr.ac.una.admin.service.UsersService;
import cr.ac.una.admin.util.AppContext;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Formato;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class GestionesController extends Controller implements Initializable {

    @FXML
    private MFXButton bntSearch;

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
    private MFXTextField txfUsuarioSeguimiento;

    @FXML
    private VBox vboxHistorial;

    @FXML
    private VBox vboxAprobaciones;

    UsersDto user;
    UsersDto actualUser;

    GestionsDto gestion;

    FollowsDto follow;

    ApprovalsDto approval;

    GestionsDto calendarGestion;

    private Map<String, UsersDto> usersMap = new HashMap<>();

    private Map<String, ActivitiesDto> activityMap = new HashMap<>();

    private Map<String, SubactivitiesDto> subactivityMap = new HashMap<>();

    private Map<String, UsersDto> assigned = new HashMap<>();

    List<UsersDto> currentUsers;

    List<UsersDto> deletedUsers;

    List<Node> requiered = new ArrayList<>();

    @Override
    public void initialize() {

        actualUser = (UsersDto) AppContext.getInstance().get("User");
        if (actualUser.getRoles().stream().anyMatch(r -> r.getName().equals("Solicitante"))) {
            txfIDAprobacion.setVisible(false);
            txfIDSeguimiento.setVisible(false);
            txfIDGestion.setVisible(false);
            tptAprobaciones.setDisable(true);
        } else {
            tptAprobaciones.setDisable(false);
            txfIDAprobacion.setVisible(true);
            txfIDSeguimiento.setVisible(true);
            txfIDGestion.setVisible(true);
        }
        initValuesOfGestion();

        calendarGestion = (GestionsDto) AppContext.getInstance().get("Gestion");
        if (calendarGestion != null) {
            chargeGestion(calendarGestion.getId());
        } else {
            AppContext.getInstance().set("Gestion", new GestionsDto());
            calendarGestion = (GestionsDto) AppContext.getInstance().get("Gestion");
        }

    }

    // requiered methods

    public String validarRequeridos() {
        Boolean validos = true;
        String invalidos = "";
        for (Node node : requiered) {
            if (node instanceof MFXTextField
                    && (((MFXTextField) node).getText() == null || ((MFXTextField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXTextField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXTextField) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXPasswordField
                    && (((MFXPasswordField) node).getText() == null || ((MFXPasswordField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXPasswordField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXPasswordField) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
                if (validos) {
                    invalidos += ((MFXDatePicker) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXDatePicker) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXComboBox && ((MFXComboBox) node).getSelectionModel().getSelectedIndex() < 0) {
                if (validos) {
                    invalidos += ((MFXComboBox) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXComboBox) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof TextArea
                    && (((TextArea) node).getText() == null || ((TextArea) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((TextArea) node).getAccessibleText();
                } else {
                    invalidos += "," + ((TextArea) node).getAccessibleText();
                }
                validos = false;
            }
        }
        if (validos) {
            return "";
        } else {
            return "Campos requierdos o con problemas de formato: " + invalidos;
        }
    }

    private void gestionRequiered() {
        requiered.clear();
        requiered.addAll(Arrays.asList(txfAsuntoGestion, txaGestion, txfSolicitanteGestion, cmbAsiganadoGestion,
                dpCreacionGestion, dpSolucionGestion));
    }

    private void followRequiered() {
        requiered.clear();
        requiered.addAll(Arrays.asList(txfDescripcionSeguimiento, dpFechaSeguimeinto, txfUsuarioSeguimiento));
    }

    private void approvalRequiered() {
        requiered.clear();
        requiered.addAll(Arrays.asList(txfDescripcionAprobacion, txaSolucionAprobacion,
                dpFechaAprobacion, txfAprobadorAprobacion));
    }

    private void allRequieredFields() {

        if (tptGestiones.isSelected()) {
            requiered.clear();
            gestionRequiered();
        } else if (tptAprobaciones.isSelected()) {
            requiered.clear();
            approvalRequiered();
        } else if (tptSeguimeinto.isSelected()) {
            requiered.clear();
            followRequiered();
        }

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

    private void selectionStateFollows(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkCursoSeguimiento, chkAprobadaSeguimiento, chkRechazadaSeguimiento };
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }
    }

    private void selectStateFollows() {
        chkCursoSeguimiento.setOnAction(e -> selectionStateFollows(chkCursoSeguimiento));
        chkAprobadaSeguimiento.setOnAction(e -> selectionStateFollows(chkAprobadaSeguimiento));
        chkRechazadaSeguimiento.setOnAction(e -> selectionStateFollows(chkRechazadaSeguimiento));
    }

    private void selectTypeOfFollows(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkTextoSeguimiento, chkArchivoSeguimiento };
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }
    }

    private void selectTypeOfFollows() {
        chkTextoSeguimiento.setOnAction(e -> {
            selectTypeOfFollows(chkTextoSeguimiento);
            if (chkTextoSeguimiento.isSelected()) {
                txaTextoSeguimiento.setDisable(false);
                txaTextoSeguimiento.setVisible(true);
                btnAdjuntarSeguimiento.setDisable(true);
                btnAdjuntarSeguimiento.setVisible(false);
            } else {
                txaTextoSeguimiento.setDisable(true);
                txaTextoSeguimiento.setVisible(false);
            }
        });
        chkArchivoSeguimiento.setOnAction(e -> {
            selectTypeOfFollows(chkArchivoSeguimiento);
            if (chkArchivoSeguimiento.isSelected()) {
                txaTextoSeguimiento.setDisable(true);
                txaTextoSeguimiento.setVisible(false);
                btnAdjuntarSeguimiento.setDisable(false);
                btnAdjuntarSeguimiento.setVisible(true);
            } else {
                btnAdjuntarSeguimiento.setDisable(true);
                btnAdjuntarSeguimiento.setVisible(false);
            }
        });
    }

    private void selectType() {

        chkActividad.setOnAction(e -> {
            selectionTypeOfGestion(chkActividad);
            if (chkActividad.isSelected()) {
                cmbActividades.setDisable(false);
                cmbActividades.setVisible(true);
                cmbSubactividades.setDisable(true);
                cmbSubactividades.setVisible(false);
            } else {
                cmbActividades.setDisable(true);
                cmbActividades.setVisible(false);
            }
        });

        chkSubactividad.setOnAction(e -> {
            selectionTypeOfGestion(chkSubactividad);
            if (chkSubactividad.isSelected()) {
                cmbSubactividades.setDisable(false);
                cmbSubactividades.setVisible(true);
                cmbActividades.setDisable(true);
                cmbActividades.setVisible(false);
            } else {
                cmbSubactividades.setDisable(true);
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

    private void selectApproval(CheckBox checkBox) {
        CheckBox[] checkBoxs = { chkAprobadaAprobacion, chkRechazadaAprobacion };
        for (CheckBox chk : checkBoxs) {
            if (chk != checkBox) {
                chk.setSelected(false);
            }
        }
    }

    private void selectApproval() {
        chkAprobadaAprobacion.setOnAction(e -> selectApproval(chkAprobadaAprobacion));
        chkRechazadaAprobacion.setOnAction(e -> selectApproval(chkRechazadaAprobacion));
    }

    private void selectQuantityOfApprovers(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkAprobador1, chkAprobador2, chkAprobador4, chkAprobador6 };

        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }

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

        updateApproverComboBoxes(selectedLevel);
    }

    private void selectQuantityOfApprovers() {

        chkAprobador1.setOnAction(e -> selectQuantityOfApprovers(chkAprobador1));
        chkAprobador2.setOnAction(e -> selectQuantityOfApprovers(chkAprobador2));
        chkAprobador4.setOnAction(e -> selectQuantityOfApprovers(chkAprobador4));
        chkAprobador6.setOnAction(e -> selectQuantityOfApprovers(chkAprobador6));

        updateApproverComboBoxes(0);
    }

    private void updateApproverComboBoxes(int selectedLevel) {

        cmbAprobador1Gestion.setDisable(selectedLevel < 1);
        cmbAprobador2Gestion.setDisable(selectedLevel < 2);
        cmbAprobador3Gestion.setDisable(selectedLevel < 3);
        cmbAprobador4Gestion.setDisable(selectedLevel < 4);
        cmbAprobador5Gestion.setDisable(selectedLevel < 5);
        cmbAprobador6Gestion.setDisable(selectedLevel < 6);

        cmbAprobador1Gestion.setVisible(selectedLevel >= 1);
        cmbAprobador2Gestion.setVisible(selectedLevel >= 2);
        cmbAprobador3Gestion.setVisible(selectedLevel >= 3);
        cmbAprobador4Gestion.setVisible(selectedLevel >= 4);
        cmbAprobador5Gestion.setVisible(selectedLevel >= 5);
        cmbAprobador6Gestion.setVisible(selectedLevel >= 6);

        if (selectedLevel < 1) {
            cmbAprobador1Gestion.getSelectionModel().clearSelection();
        }
        if (selectedLevel < 2) {
            cmbAprobador2Gestion.getSelectionModel().clearSelection();
        }
        if (selectedLevel < 3) {
            cmbAprobador3Gestion.getSelectionModel().clearSelection();
        }
        if (selectedLevel < 4) {
            cmbAprobador4Gestion.getSelectionModel().clearSelection();
        }
        if (selectedLevel < 5) {
            cmbAprobador5Gestion.getSelectionModel().clearSelection();
        }
        if (selectedLevel < 6) {
            cmbAprobador6Gestion.getSelectionModel().clearSelection();
        }

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
        selectStateFollows();
        selectTypeOfFollows();
        selectApproval();

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

        chargeUsers();
        chargeActivity();
        chargeSubactivity();

        newGestion();

    }

    // format method
    private void format() {
        // ID's
        txfIDGestion.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDAprobacion.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDSeguimiento.setTextFormatter(Formato.getInstance().integerFormat());

        // Descriptions
        txfDescripcionAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(200));
        txfDescripcionSeguimiento.setTextFormatter(Formato.getInstance().anyCharacterFormatWithMaxLength(200));
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
    }

    private void bindFollow(Boolean isNew) {
        if (!isNew) {
            txfIDSeguimiento.textProperty().bind(follow.id);
        }
        txfDescripcionSeguimiento.textProperty().bindBidirectional(follow.description);
        dpFechaSeguimeinto.valueProperty().bindBidirectional(follow.date);
    }

    private void bindApproval(Boolean isNew) {
        if (!isNew) {
            txfIDAprobacion.textProperty().bind(approval.id);
        }
        txfDescripcionAprobacion.textProperty().bindBidirectional(approval.description);
        txfComentarioAprobacion.textProperty().bindBidirectional(approval.comment);
        txaSolucionAprobacion.textProperty().bindBidirectional(approval.solution);
        dpFechaAprobacion.valueProperty().bindBidirectional(approval.date);
    }

    // unbinds

    private void unbindGestion() {
        txfIDGestion.textProperty().unbind();
        txfAsuntoGestion.textProperty().unbindBidirectional(gestion.subject);
        txaGestion.textProperty().unbindBidirectional(gestion.description);
        dpCreacionGestion.valueProperty().unbindBidirectional(gestion.creationDate);
        dpSolucionGestion.valueProperty().unbindBidirectional(gestion.solutionDate);
    }

    private void unbindFollow() {
        txfIDSeguimiento.textProperty().unbind();
        txfDescripcionSeguimiento.textProperty().unbindBidirectional(follow.description);
        dpFechaSeguimeinto.valueProperty().unbindBidirectional(follow.date);
    }

    private void unbindApproval() {
        txfIDAprobacion.textProperty().unbind();
        txfDescripcionAprobacion.textProperty().unbindBidirectional(approval.description);
        txfComentarioAprobacion.textProperty().unbindBidirectional(approval.comment);
        txaSolucionAprobacion.textProperty().unbindBidirectional(approval.solution);
        dpFechaAprobacion.valueProperty().unbindBidirectional(approval.date);
    }

    private void cleanGestion() {
        chkActividad.setSelected(false);
        chkSubactividad.setSelected(false);
        cmbActividades.setDisable(true);
        cmbActividades.setVisible(false);
        cmbActividades.getSelectionModel().clearSelection();
        cmbSubactividades.setDisable(true);
        cmbSubactividades.setVisible(false);
        cmbSubactividades.getSelectionModel().clearSelection();
        cmbAprobador1Gestion.getSelectionModel().clearSelection();
        cmbAprobador1Gestion.getItems().clear();
        cmbAprobador1Gestion.setDisable(true);
        cmbAprobador1Gestion.setVisible(false);
        cmbAprobador2Gestion.getSelectionModel().clearSelection();
        cmbAprobador2Gestion.getItems().clear();
        cmbAprobador2Gestion.setDisable(true);
        cmbAprobador2Gestion.setVisible(false);
        cmbAprobador3Gestion.getSelectionModel().clearSelection();
        cmbAprobador3Gestion.getItems().clear();
        cmbAprobador3Gestion.setDisable(true);
        cmbAprobador3Gestion.setVisible(false);
        cmbAprobador4Gestion.getSelectionModel().clearSelection();
        cmbAprobador4Gestion.getItems().clear();
        cmbAprobador4Gestion.setDisable(true);
        cmbAprobador4Gestion.setVisible(false);
        cmbAprobador5Gestion.getSelectionModel().clearSelection();
        cmbAprobador5Gestion.getItems().clear();
        cmbAprobador5Gestion.setDisable(true);
        cmbAprobador5Gestion.setVisible(false);
        cmbAprobador6Gestion.getSelectionModel().clearSelection();
        cmbAprobador6Gestion.getItems().clear();
        cmbAprobador6Gestion.setDisable(true);
        cmbAprobador6Gestion.setVisible(false);
        cmbAsiganadoGestion.getSelectionModel().clearSelection();
        cmbAsiganadoGestion.getItems().clear();
        cmbAsiganadoGestion.setDisable(false);
        cmbAsiganadoGestion.setVisible(true);
        chkAprobador1.setSelected(false);
        chkAprobador2.setSelected(false);
        chkAprobador4.setSelected(false);
        chkAprobador6.setSelected(false);
        chkCursoGestion.setSelected(false);
        chkEsperaGestion.setSelected(false);
        chkAprobacionGestion.setSelected(false);
        chkAprobadaGestion.setSelected(false);
        chkRechazadaGestion.setSelected(false);

    }

    // new entity

    private void newGestion() {
        unbindGestion();
        cleanGestion();
        gestion = new GestionsDto();
        bindGestion(true);
        txfIDGestion.clear();
        txfIDGestion.requestFocus();
        chkEsperaGestion.setSelected(true);
        vboxHistorial.getChildren().clear();
        chargeUsers();

    }

    private void newFollow() {
        unbindFollow();
        follow = new FollowsDto();
        bindFollow(true);
        restoreFollow();
        txfIDSeguimiento.clear();
        txfIDSeguimiento.requestFocus();
        chkCursoSeguimiento.setSelected(true);
        chkTextoSeguimiento.setSelected(true);
        txaTextoSeguimiento.clear();
        txaTextoSeguimiento.setDisable(false);
        txaTextoSeguimiento.setVisible(true);
        btnAdjuntarSeguimiento.setDisable(true);
        btnAdjuntarSeguimiento.setVisible(false);
    }

    private void restoreFollow() {
        txfIDSeguimiento.setEditable(true);
        txfDescripcionSeguimiento.setEditable(true);
        dpFechaSeguimeinto.setEditable(true);
        dpFechaSeguimeinto.setDisable(false);
        txfUsuarioSeguimiento.setText(user.getName() + " " + user.getLastNames());
        txfUsuarioSeguimiento.setEditable(false);
        chkCursoSeguimiento.setDisable(false);
        chkAprobadaSeguimiento.setDisable(false);
        chkRechazadaSeguimiento.setDisable(false);
        chkTextoSeguimiento.setDisable(false);
        chkArchivoSeguimiento.setDisable(false);
    }

    private void newApproval() {
        unbindApproval();
        approval = new ApprovalsDto();
        bindApproval(true);
        restoreApproval();
        txfIDAprobacion.clear();
        txfIDAprobacion.requestFocus();
        txfComentarioAprobacion.clear();
        txaSolucionAprobacion.clear();
    }

    private void restoreApproval() {
        txfIDAprobacion.setEditable(true);
        txfDescripcionAprobacion.setEditable(true);
        txfComentarioAprobacion.setEditable(true);
        txaSolucionAprobacion.setEditable(true);
        dpFechaAprobacion.setEditable(true);
        dpFechaAprobacion.setDisable(false);
        txfAprobadorAprobacion.setText(user.getName() + " " + user.getLastNames());
        txfAprobadorAprobacion.setEditable(false);
        chkAprobadaAprobacion.setDisable(false);
        chkRechazadaAprobacion.setDisable(false);

    }

    // selection checkBoxes methods

    private void setState() {
        if (chkCursoGestion.isSelected()) {
            gestion.state.set("C");
        } else if (chkEsperaGestion.isSelected()) {
            gestion.state.set("E");
        } else if (chkAprobacionGestion.isSelected()) {
            gestion.state.set("S");
        } else if (chkAprobadaGestion.isSelected()) {
            gestion.state.set("A");
        } else if (chkRechazadaGestion.isSelected()) {
            gestion.state.set("R");
        }
    }

    // set approvers method

    private List<UsersDto> setApprovers() {
        List<UsersDto> selectedApprovers = new ArrayList<>();

        if (chkAprobador1.isSelected()) {
            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
        }

        if (chkAprobador2.isSelected()) {

            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            String selectedUser2 = cmbAprobador2Gestion.getSelectionModel().getSelectedItem();

            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
            if (selectedUser2 != null && usersMap.containsKey(selectedUser2)) {
                selectedApprovers.add(usersMap.get(selectedUser2));
            }
        }

        if (chkAprobador4.isSelected()) {

            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            String selectedUser2 = cmbAprobador2Gestion.getSelectionModel().getSelectedItem();
            String selectedUser3 = cmbAprobador3Gestion.getSelectionModel().getSelectedItem();
            String selectedUser4 = cmbAprobador4Gestion.getSelectionModel().getSelectedItem();

            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
            if (selectedUser2 != null && usersMap.containsKey(selectedUser2)) {
                selectedApprovers.add(usersMap.get(selectedUser2));
            }
            if (selectedUser3 != null && usersMap.containsKey(selectedUser3)) {
                selectedApprovers.add(usersMap.get(selectedUser3));
            }
            if (selectedUser4 != null && usersMap.containsKey(selectedUser4)) {
                selectedApprovers.add(usersMap.get(selectedUser4));
            }
        }

        if (chkAprobador6.isSelected()) {

            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            String selectedUser2 = cmbAprobador2Gestion.getSelectionModel().getSelectedItem();
            String selectedUser3 = cmbAprobador3Gestion.getSelectionModel().getSelectedItem();
            String selectedUser4 = cmbAprobador4Gestion.getSelectionModel().getSelectedItem();
            String selectedUser5 = cmbAprobador5Gestion.getSelectionModel().getSelectedItem();
            String selectedUser6 = cmbAprobador6Gestion.getSelectionModel().getSelectedItem();

            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
            if (selectedUser2 != null && usersMap.containsKey(selectedUser2)) {
                selectedApprovers.add(usersMap.get(selectedUser2));
            }
            if (selectedUser3 != null && usersMap.containsKey(selectedUser3)) {
                selectedApprovers.add(usersMap.get(selectedUser3));
            }
            if (selectedUser4 != null && usersMap.containsKey(selectedUser4)) {
                selectedApprovers.add(usersMap.get(selectedUser4));
            }
            if (selectedUser5 != null && usersMap.containsKey(selectedUser5)) {
                selectedApprovers.add(usersMap.get(selectedUser5));
            }
            if (selectedUser6 != null && usersMap.containsKey(selectedUser6)) {
                selectedApprovers.add(usersMap.get(selectedUser6));
            }
        }

        return selectedApprovers;
    }

    private void setApproversGestion() {

        List<UsersDto> selectedApprovers = setApprovers();

        List<UsersDto> deletedUsers = new ArrayList<>();

        for (UsersDto user : new ArrayList<>(gestion.getApprovers())) {

            if (selectedApprovers.stream().noneMatch(u -> u.getId().equals(user.getId()))) {

                deletedUsers.add(user);
            }
        }

        gestion.setApprovers(new ArrayList<>(selectedApprovers));
        gestion.setDeletedApprovers(deletedUsers);

        System.out.println("Selected Approvers (IDs): "
                + selectedApprovers.stream().map(UsersDto::getId).collect(Collectors.toList()));
        System.out.println(
                "Deleted Users (IDs): " + deletedUsers.stream().map(UsersDto::getId).collect(Collectors.toList()));
    }

    // selection Activity or subactivity

    private void setActivity() {
        if (chkActividad.isSelected()) {
            String selectedActivity = cmbActividades.getSelectionModel().getSelectedItem();
            System.out.println(selectedActivity);
            if (selectedActivity != null && activityMap.containsKey(selectedActivity)) {
                gestion.setActivity(activityMap.get(selectedActivity));
                System.out.println(gestion.getActivity().getId());
            }
        } else if (chkSubactividad.isSelected()) {
            String selectedSubactivity = cmbSubactividades.getSelectionModel().getSelectedItem();
            System.out.println(selectedSubactivity);
            if (selectedSubactivity != null && subactivityMap.containsKey(selectedSubactivity)) {
                gestion.setSubactivities(subactivityMap.get(selectedSubactivity));
                System.out.println("Subactividad: " + gestion.getSubactivities().getId());
            }
        }
    }

    // assigned method

    private void setAssigned() {
        String selectedAssigned = cmbAsiganadoGestion.getSelectionModel().getSelectedItem();
        if (selectedAssigned != null && assigned.containsKey(selectedAssigned)) {

            List<UsersDto> aprobadores = gestion.getApprovers();

            if (aprobadores == null) {
                aprobadores = new ArrayList<>(assigned.values());
                gestion.setApprovers(aprobadores);
            } else {

                UsersDto usuarioAsignado = assigned.get(selectedAssigned);
                if (!aprobadores.contains(usuarioAsignado)) {
                    aprobadores.add(usuarioAsignado);
                }
            }

            gestion.setAssigned(assigned.get(selectedAssigned));
        }
    }

    // charging methods

    private void chargeUsers() {
        try {
            UsersService service = new UsersService();
            Respuesta respuesta = service.getUsers();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Usuarios", getStage(), respuesta.getMensaje());
            } else {

                UsersDto currentUser = (UsersDto) AppContext.getInstance().get("User");
                List<UsersDto> users = (List<UsersDto>) respuesta.getResultado("Usuarios");

                usersMap.clear();
                assigned.clear();

                for (UsersDto user : users) {
                    String userName = user.getName() + " " + user.getLastNames();
                    usersMap.put(userName, user);
                    assigned.put(userName, user);
                }

                cmbAsiganadoGestion.getItems().clear();
                cmbAprobador1Gestion.getItems().clear();
                cmbAprobador2Gestion.getItems().clear();
                cmbAprobador3Gestion.getItems().clear();
                cmbAprobador4Gestion.getItems().clear();
                cmbAprobador5Gestion.getItems().clear();
                cmbAprobador6Gestion.getItems().clear();

                for (UsersDto user : users) {
                    String userName = user.getName() + " " + user.getLastNames();
                    if (!user.getId().equals(currentUser.getId())) {
                        cmbAsiganadoGestion.getItems().add(userName);
                    }
                }

                for (UsersDto user : users) {
                    String userName = user.getName() + " " + user.getLastNames();
                    if (!user.getId().equals(currentUser.getId())) {
                        cmbAprobador1Gestion.getItems().add(userName);
                        cmbAprobador2Gestion.getItems().add(userName);
                        cmbAprobador3Gestion.getItems().add(userName);
                        cmbAprobador4Gestion.getItems().add(userName);
                        cmbAprobador5Gestion.getItems().add(userName);
                        cmbAprobador6Gestion.getItems().add(userName);
                    }
                }

                cmbAsiganadoGestion.getSelectionModel().selectedItemProperty()
                        .addListener((observable, oldValue, newValue) -> {
                            if (newValue != null) {
                                // Limpiar todos los ComboBox de aprobadores
                                cmbAprobador1Gestion.getItems().clear();
                                cmbAprobador2Gestion.getItems().clear();
                                cmbAprobador3Gestion.getItems().clear();
                                cmbAprobador4Gestion.getItems().clear();
                                cmbAprobador5Gestion.getItems().clear();
                                cmbAprobador6Gestion.getItems().clear();

                                for (UsersDto user : users) {
                                    String userName = user.getName() + " " + user.getLastNames();
                                    if (!userName.equals(newValue) && !user.getId().equals(currentUser.getId())) {
                                        cmbAprobador1Gestion.getItems().add(userName);
                                        cmbAprobador2Gestion.getItems().add(userName);
                                        cmbAprobador3Gestion.getItems().add(userName);
                                        cmbAprobador4Gestion.getItems().add(userName);
                                        cmbAprobador5Gestion.getItems().add(userName);
                                        cmbAprobador6Gestion.getItems().add(userName);
                                    }
                                }
                            }
                        });
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando los usuarios", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Usuarios", getStage(), "Error cargando los usuarios.");
        }
    }

    private void chargeActivity() {
        try {
            ActivitiesService service = new ActivitiesService();
            Respuesta respuesta = service.getActivities();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Actividades", getStage(), respuesta.getMensaje());
            } else {
                List<ActivitiesDto> activities = (List<ActivitiesDto>) respuesta.getResultado("Activity");

                activityMap.clear();
                for (ActivitiesDto activity : activities) {
                    activityMap.put(activity.getName(), activity);
                }

                cmbActividades.getItems().clear();

                for (ActivitiesDto activity : activities) {
                    String activityName = activity.getName();
                    cmbActividades.getItems().add(activityName);

                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando las actividades",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Actividades", getStage(),
                    "Error cargando las actividades.");
        }
    }

    private void chargeSubactivity() {
        try {
            SubactivitiesService service = new SubactivitiesService();
            Respuesta respuesta = service.getSubactivities();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Subactividades", getStage(), respuesta.getMensaje());
            } else {
                List<SubactivitiesDto> subactivities = (List<SubactivitiesDto>) respuesta
                        .getResultado("Subactivity");

                subactivityMap.clear();

                for (SubactivitiesDto subactivity : subactivities) {
                    subactivityMap.put(subactivity.getName(), subactivity);
                }

                cmbSubactividades.getItems().clear();

                for (SubactivitiesDto subactivity : subactivities) {
                    String subactivityName = subactivity.getName();
                    cmbSubactividades.getItems().add(subactivityName);

                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando las subactividades",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Subactividades", getStage(),
                    "Error cargando las subactividades.");
        }
    }

    private void chargeGestion(Long id) {
        try {
            vboxHistorial.getChildren().clear();
            GestionService service = new GestionService();
            Respuesta respuesta = service.getGestion(id);

            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestiones", getStage(), respuesta.getMensaje());
                return;
            }

            unbindGestion();
            gestion = (GestionsDto) respuesta.getResultado("Gestion");

            txfSolicitanteGestion.setEditable(true);
            txfSolicitanteGestion
                    .setText(gestion.getRequester().getName() + " " + gestion.getRequester().getLastNames());
            txfSolicitanteGestion.setEditable(false);

            if (gestion.getState().equals("C")) {
                chkRechazadaGestion.setSelected(false);
                chkAprobadaGestion.setSelected(false);
                chkAprobacionGestion.setSelected(false);
                chkEsperaGestion.setSelected(false);
                chkCursoGestion.setSelected(true);
            } else if (gestion.getState().equals("E")) {
                chkRechazadaGestion.setSelected(false);
                chkAprobadaGestion.setSelected(false);
                chkAprobacionGestion.setSelected(false);
                chkCursoGestion.setSelected(false);
                chkEsperaGestion.setSelected(true);
            } else if (gestion.getState().equals("A")) {
                chkRechazadaGestion.setSelected(false);
                chkAprobadaGestion.setSelected(false);
                chkEsperaGestion.setSelected(false);
                chkCursoGestion.setSelected(false);
                chkAprobacionGestion.setSelected(true);
            } else if (gestion.getState().equals("S")) {
                chkRechazadaGestion.setSelected(false);
                chkAprobacionGestion.setSelected(true);
                chkEsperaGestion.setSelected(false);
                chkCursoGestion.setSelected(false);
                chkAprobadaGestion.setSelected(false);
            } else if (gestion.getState().equals("R")) {
                chkAprobadaGestion.setSelected(false);
                chkAprobacionGestion.setSelected(false);
                chkEsperaGestion.setSelected(false);
                chkCursoGestion.setSelected(false);
                chkRechazadaGestion.setSelected(true);
            }

            if (gestion.getActivity() != null) {
                chkActividad.setSelected(true);
                if (cmbActividades.getItems().contains(gestion.getActivity().getName())) {
                    cmbActividades.setVisible(true);
                    cmbActividades.setDisable(false);
                    cmbActividades.getSelectionModel().selectItem(gestion.getActivity().getName());
                } else {
                    System.out.println(
                            "La actividad " + gestion.getActivity().getName() + " no está en la lista de opciones.");
                }
            } else if (gestion.getSubactivities() != null) {
                chkSubactividad.setSelected(true);
                if (cmbSubactividades.getItems().contains(gestion.getSubactivities().getName())) {
                    cmbSubactividades.setVisible(true);
                    cmbSubactividades.setDisable(false);
                    cmbSubactividades.getSelectionModel().selectItem(gestion.getSubactivities().getName());
                } else {
                    System.out.println("La subactividad " + gestion.getSubactivities().getName()
                            + " no está en la lista de opciones.");
                }
            }

            if (gestion.getAssigned() != null) {
                String assignedFullName = gestion.getAssigned().getName() + " " + gestion.getAssigned().getLastNames();
                if (!cmbAsiganadoGestion.getItems().contains(assignedFullName)) {
                    // Añadir el usuario asignado al ComboBox si no está ya en la lista
                    cmbAsiganadoGestion.getItems().add(assignedFullName);
                }
                cmbAsiganadoGestion.getSelectionModel().selectItem(assignedFullName);
            }

            List<MFXComboBox<String>> approverComboBoxes = List.of(
                    cmbAprobador1Gestion, cmbAprobador2Gestion, cmbAprobador3Gestion,
                    cmbAprobador4Gestion, cmbAprobador5Gestion, cmbAprobador6Gestion);

            // Selección de aprobadores
            currentUsers = gestion.getApprovers();
            int approversCount = gestion.getApprovers().size();
            for (int i = 0; i < approversCount && i < approverComboBoxes.size(); i++) {
                UsersDto approver = gestion.getApprovers().get(i);
                String approverFullName = approver.getName() + " " + approver.getLastNames();
                MFXComboBox<String> approverComboBox = approverComboBoxes.get(i);

                if (!approverComboBox.getItems().contains(approverFullName)) {
                    approverComboBox.getItems().add(approverFullName);
                }

                approverComboBox.setVisible(true);
                approverComboBox.setDisable(false);
                approverComboBox.getSelectionModel().selectItem(approverFullName);

                // Marcar el CheckBox correspondiente como seleccionado
                switch (i) {
                    case 0:
                        chkAprobador1.setSelected(true);
                        chkAprobador2.setSelected(false);
                        chkAprobador4.setSelected(false);
                        chkAprobador6.setSelected(false);
                        break;
                    case 1:
                        chkAprobador1.setSelected(false);
                        chkAprobador2.setSelected(true);
                        chkAprobador4.setSelected(false);
                        chkAprobador6.setSelected(false);
                        break;
                    case 2:
                        chkAprobador1.setSelected(false);
                        chkAprobador2.setSelected(false);
                        chkAprobador4.setSelected(true);
                        chkAprobador6.setSelected(false);
                        break;
                    case 3:
                        chkAprobador1.setSelected(false);
                        chkAprobador2.setSelected(false);
                        chkAprobador4.setSelected(false);
                        chkAprobador6.setSelected(true);
                        break;
                }
            }

            for (FollowsDto follow : gestion.getFollows()) {

                FollowsDto currentFollow = follow;

                vboxHistorial.setAlignment(Pos.TOP_CENTER);
                vboxHistorial.setSpacing(5);

                AnchorPane pane = new AnchorPane();
                pane.setPrefHeight(50);
                pane.setPrefWidth(100);
                pane.getStyleClass().add("stack-historial-pane");
                VBox.setMargin(pane, new Insets(20));

                VBox vbox = new VBox();
                vbox.getStyleClass().add("stack-personal-pane");
                vbox.setAlignment(Pos.CENTER);

                // Título de la gestión
                Label title = new Label(follow.getDescription());
                title.getStyleClass().add("stackpane-label-black");
                title.setAlignment(Pos.CENTER);
                title.setMaxWidth(Double.MAX_VALUE);

                // Contenido de la gestión con TextFlow
                TextFlow contentFlow = new TextFlow(new Text(new String(follow.getArchive(), StandardCharsets.UTF_8)));
                contentFlow.setTextAlignment(TextAlignment.CENTER);
                contentFlow.setMaxWidth(250);
                contentFlow.setPrefWidth(250);

                // Fecha de la gestión
                Label date = new Label(follow.getDate().toString());
                date.setAlignment(Pos.CENTER);
                date.setMaxWidth(Double.MAX_VALUE);

                // Añadiendo los elementos al VBox
                vbox.getChildren().addAll(title, contentFlow, date);
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(5);
                vbox.setPadding(new Insets(10));
                vbox.setPrefHeight(50);
                vbox.setPrefWidth(180);

                AnchorPane.setTopAnchor(vbox, 10.0);
                AnchorPane.setBottomAnchor(vbox, 10.0);
                AnchorPane.setLeftAnchor(vbox, 10.0);
                AnchorPane.setRightAnchor(vbox, 10.0);

                pane.setOnMouseClicked(event -> {
                    txfUsuarioSeguimiento.setEditable(true);
                    // Depuración: imprime los valores de follow en la consola
                    System.out.println("ID: " + currentFollow.getId());
                    System.out.println("Descripción: " + currentFollow.getDescription());
                    System.out.println("Contenido: " + new String(currentFollow.getArchive(), StandardCharsets.UTF_8));
                    System.out.println("Fecha: " + currentFollow.getDate());
                    System.out.println(
                            "Usuario: " + currentFollow.getUsers().getName() + " "
                                    + currentFollow.getUsers().getLastNames());
                    System.out.println("Estado: " + currentFollow.getState());

                    // Mostrar el contenido del seguimiento
                    txfIDSeguimiento.setText(currentFollow.getId().toString());
                    txfDescripcionSeguimiento.setText(currentFollow.getDescription());
                    txfUsuarioSeguimiento.setText(currentFollow.getUsers().getName() + " "
                            + currentFollow.getUsers().getLastNames());
                    txfUsuarioSeguimiento.setEditable(false);
                    txaTextoSeguimiento.setText(new String(currentFollow.getArchive(), StandardCharsets.UTF_8));
                    dpFechaSeguimeinto.setValue(LocalDate.parse(follow.getDate().toString()));

                    if (currentFollow.getState().equals("A")) {
                        chkAprobadaSeguimiento.setSelected(true);
                    } else if (currentFollow.getState().equals("R")) {
                        chkRechazadaSeguimiento.setSelected(true);
                    } else {
                        chkCursoSeguimiento.setSelected(true);
                    }

                    if (currentFollow.getUsers().getId() != ((UsersDto) AppContext.getInstance().get("User"))
                            .getId()) {
                        new Mensaje().showModal(AlertType.INFORMATION, "Seguimiento", getStage(),
                                "No puedes editar un seguimiento que no te pertenece.");
                        txfIDSeguimiento.setEditable(false);
                        txfDescripcionSeguimiento.setEditable(false);
                        txaTextoSeguimiento.setEditable(false);
                        chkTextoSeguimiento.setDisable(false);
                        chkArchivoSeguimiento.setDisable(false);
                        dpFechaSeguimeinto.setDisable(true);
                        chkAprobadaSeguimiento.setDisable(true);
                        chkRechazadaSeguimiento.setDisable(true);
                        chkCursoSeguimiento.setDisable(true);
                        txfUsuarioSeguimiento.setEditable(false);
                    } else {
                        txfIDSeguimiento.setEditable(true);
                        txfDescripcionSeguimiento.setEditable(true);
                        txaTextoSeguimiento.setEditable(true);
                        chkTextoSeguimiento.setDisable(false);
                        chkArchivoSeguimiento.setDisable(false);
                        dpFechaSeguimeinto.setDisable(false);
                        chkAprobadaSeguimiento.setDisable(false);
                        chkRechazadaSeguimiento.setDisable(false);
                        chkCursoSeguimiento.setDisable(false);
                        txfUsuarioSeguimiento.setEditable(false);
                        return;
                    }

                });

                pane.getChildren().add(vbox);
                vboxHistorial.getChildren().add(pane);
            }

            for (ApprovalsDto approval : gestion.getApprovals()) {
                ApprovalsDto currentApproval = approval;

                vboxAprobaciones.setAlignment(Pos.TOP_CENTER);
                vboxAprobaciones.setSpacing(5);

                AnchorPane pane = new AnchorPane();
                pane.setPrefHeight(50);
                pane.setPrefWidth(100);
                pane.getStyleClass().add("stack-historial-pane");
                VBox.setMargin(pane, new Insets(20));

                VBox vbox = new VBox();
                vbox.getStyleClass().add("stack-personal-pane");
                vbox.setAlignment(Pos.CENTER);

                // Título de la gestión
                Label title = new Label(approval.getDescription());
                title.getStyleClass().add("stackpane-label-black");
                title.setAlignment(Pos.CENTER);
                title.setMaxWidth(Double.MAX_VALUE);

                // Contenido de la gestión con TextFlow
                Label approver = new Label(approval.getUsers().getName() + " " + approval.getUsers().getLastNames());
                approver.setTextAlignment(TextAlignment.CENTER);
                approver.setMaxWidth(250);
                approver.setPrefWidth(250);

                // Fecha de la gestión
                Label date = new Label(approval.getDate().toString());
                date.setAlignment(Pos.CENTER);
                date.setMaxWidth(Double.MAX_VALUE);

                // Añadiendo los elementos al VBox
                vbox.getChildren().addAll(title, approver, date);
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(5);
                vbox.setPadding(new Insets(10));
                vbox.setPrefHeight(50);
                vbox.setPrefWidth(180);

                AnchorPane.setTopAnchor(vbox, 10.0);
                AnchorPane.setBottomAnchor(vbox, 10.0);
                AnchorPane.setLeftAnchor(vbox, 10.0);
                AnchorPane.setRightAnchor(vbox, 10.0);

                pane.setOnMouseClicked(event -> {

                    txfAprobadorAprobacion.setEditable(true);
                    // Depuración: imprime los valores de follow en la consola
                    System.out.println("ID: " + currentApproval.getId());
                    System.out.println("Descripción: " + currentApproval.getDescription());
                    System.out.println("Comentario: " + currentApproval.getComment());
                    System.out.println("Solución: " + currentApproval.getSolution());
                    System.out.println("Fecha: " + currentApproval.getDate());
                    System.out.println(
                            "Usuario: " + currentApproval.getUsers().getName() + " "
                                    + currentApproval.getUsers().getLastNames());
                    System.out.println("Estado: " + currentApproval.getState());

                    // Mostrar el contenido del seguimiento

                    txfIDAprobacion.setText(currentApproval.getId().toString());
                    txfDescripcionAprobacion.setText(currentApproval.getDescription());
                    txfAprobadorAprobacion.setText(currentApproval.getUsers().getName() + " "
                            + currentApproval.getUsers().getLastNames());
                    txfAprobadorAprobacion.setEditable(false);
                    txfComentarioAprobacion.setText(currentApproval.getComment());
                    txaSolucionAprobacion.setText(currentApproval.getSolution());
                    dpFechaAprobacion.setValue(LocalDate.parse(approval.getDate().toString()));

                    if (currentApproval.getState().equals("A")) {
                        chkRechazadaAprobacion.setSelected(false);
                        chkAprobadaAprobacion.setSelected(true);
                    } else if (currentApproval.getState().equals("R")) {
                        chkAprobadaAprobacion.setSelected(false);
                        chkRechazadaAprobacion.setSelected(true);
                    }

                    if (currentApproval.getUsers().getId() != ((UsersDto) AppContext.getInstance().get("User"))
                            .getId()) {
                        new Mensaje().showModal(AlertType.INFORMATION, "Aprobación", getStage(),
                                "No puedes editar una aprobación que no te pertenece.");
                        txfIDAprobacion.setEditable(false);
                        txfDescripcionAprobacion.setEditable(false);
                        txfComentarioAprobacion.setEditable(false);
                        txaSolucionAprobacion.setEditable(false);
                        dpFechaAprobacion.setDisable(true);
                        chkAprobadaAprobacion.setDisable(true);
                        chkRechazadaAprobacion.setDisable(true);
                        txfAprobadorAprobacion.setEditable(false);
                    } else {
                        txfIDAprobacion.setEditable(true);
                        txfDescripcionAprobacion.setEditable(true);
                        txfComentarioAprobacion.setEditable(true);
                        txaSolucionAprobacion.setEditable(true);
                        dpFechaAprobacion.setDisable(false);
                        chkAprobadaAprobacion.setDisable(false);
                        chkRechazadaAprobacion.setDisable(false);
                        txfAprobadorAprobacion.setEditable(false);
                        return;
                    }

                });

                pane.getChildren().add(vbox);

                vboxAprobaciones.getChildren().add(pane);

            }

            bindGestion(false);

        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando la gestion", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Gestion", getStage(), "Error cargando la gestion.");
        }
    }

    // save methods

    private void saveGestion() {
        try {
            String invalid = validarRequeridos();
            if (!invalid.isEmpty()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), invalid);
            } else {
                setAssigned();
                setState();
                setActivity();
                setApproversGestion();
                UsersDto user = (UsersDto) AppContext.getInstance().get("User");
                this.gestion.setRequester(user);
                System.out.println("Solicitante: " + gestion.getRequester().getId());
                if (gestion.getActivity() != null) {
                    System.out.println("Actividad: " + gestion.getActivity().getId());
                } else if (gestion.getSubactivities() != null) {
                    System.out.println("Subactividad: " + gestion.getSubactivities().getId());
                }
                System.out.println(gestion.getState());
                this.gestion.setDeletedApprovers(deletedUsers);
                GestionService service = new GestionService();
                Respuesta respuesta = service.createGestion(gestion);
                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), respuesta.getMensaje());
                } else {
                    new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), respuesta.getMensaje());
                    unbindGestion();
                    this.gestion = (GestionsDto) respuesta.getResultado("Gestion");
                    bindGestion(false);
                    new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), "Gestión guardada con éxito");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error guardando la gestion", e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Gestion", getStage(), "Error guardando la gestion.");
        }

    }

    private void saveFollow() {
        try {
            if (txfUsuarioSeguimiento.getText().isEmpty() || txfUsuarioSeguimiento.getText().isBlank()
                    || dpFechaSeguimeinto.getValue() == null || txaTextoSeguimiento.getText().isEmpty()
                    || txaTextoSeguimiento.getText().isBlank() || txfDescripcionSeguimiento.getText().isEmpty()
                    || txfDescripcionSeguimiento.getText().isBlank()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Seguimiento", getStage(),
                        "Debe completar todos los campos requeridos");
            } else {
                this.follow.setGestions(this.gestion);
                this.follow.setUsers((UsersDto) AppContext.getInstance().get("User"));
                this.follow.setArchive(txaTextoSeguimiento.getText().getBytes(StandardCharsets.UTF_8));
                GestionService service = new GestionService();
                Respuesta respuesta = service.createFollow(follow);
                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Seguimiento", getStage(), respuesta.getMensaje());
                } else {
                    unbindFollow();
                    this.follow = (FollowsDto) respuesta.getResultado("Follow");
                    bindFollow(false);
                    vboxHistorial.getChildren().clear();
                    chargeGestion(Long.valueOf(txfIDGestion.getText()));
                    new Mensaje().showModal(AlertType.INFORMATION, "Seguimiento", getStage(), respuesta.getMensaje());

                }
            }

        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error guardando el seguimiento",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Seguimiento", getStage(),
                    "Error guardando el seguimiento.");
        }
    }

    private void saveApproval() {
        try {

            if (txfAprobadorAprobacion.getText().isEmpty() || txfAprobadorAprobacion.getText().isBlank()
                    || dpFechaAprobacion.getValue() == null || txaSolucionAprobacion.getText().isEmpty()
                    || txaSolucionAprobacion.getText().isBlank() || txfDescripcionAprobacion.getText().isEmpty()
                    || txfDescripcionAprobacion.getText().isBlank()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Aprobación", getStage(),
                        "Debe completar todos los campos requeridos");
            } else {
                this.approval.setGestion(this.gestion);
                this.approval.setUsers((UsersDto) AppContext.getInstance().get("User"));
                if (chkAprobadaAprobacion.isSelected()) {
                    this.approval.setState("A");
                } else if (chkRechazadaAprobacion.isSelected()) {
                    this.approval.setState("R");
                }
                this.approval.setDescription(txfDescripcionAprobacion.getText());
                this.approval.setComment(txfComentarioAprobacion.getText());
                this.approval.setSolution(txaSolucionAprobacion.getText());
                GestionService service = new GestionService();
                Respuesta respuesta = service.createApproval(approval);
                if (!respuesta.getEstado()) {
                    new Mensaje().showModal(AlertType.INFORMATION, "Aprobación", getStage(), respuesta.getMensaje());
                } else {
                    unbindApproval();
                    this.approval = (ApprovalsDto) respuesta.getResultado("Approval");
                    bindApproval(false);
                    new Mensaje().showModal(AlertType.INFORMATION, "Aprobación", getStage(), respuesta.getMensaje());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error guardando la aprobación",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Aprobación", getStage(),
                    "Error guardando la aprobación.");
        }
    }

    // delete methods

    private void deleteGestion(Long id) {
        try {
            GestionService service = new GestionService();
            Respuesta respuesta = service.deleteGestion(id);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestiones", getStage(), respuesta.getMensaje());
            } else {
                new Mensaje().showModal(AlertType.CONFIRMATION, "Gestiones", getStage(),
                        "La gestion ha sido correctamente eliminada");
                newGestion();
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error eliminando la gestión", e);
            new Mensaje().showModal(AlertType.ERROR, "Eliminar Gestion", getStage(), "Error eliminando la gestión.");
        }
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        File file = fileChooser.showOpenDialog(this.getStage());

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {
        if (tptGestiones.isSelected()) {
            deleteGestion(Long.valueOf(txfIDGestion.getText()));
        } else if (tptSeguimeinto.isSelected()) {
            new Mensaje().showModal(AlertType.INFORMATION, "Seguimiento", getStage(),
                    "No se puede eliminar un seguimiento ya que es parte del historial de la gestión");
        }

    }

    @FXML
    void onActionBtnExit(ActionEvent event) {
        AppContext.getInstance().set("Gestion", null);
        FlowController.getInstance().goViewInWindow("PrincipalView");
        this.getStage().close();
    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (tptGestiones.isSelected()) {
            if (new Mensaje().showConfirmation("Nueva Gestion", getStage(), "Desea crear una nueva gestion?")) {
                newGestion();
            }
        } else if (tptSeguimeinto.isSelected()) {
            if (new Mensaje().showConfirmation("Nuevo Seguimiento", getStage(),
                    "Desea crear un nuevo seguimiento?")) {
                newFollow();
            }
        } else if (tptAprobaciones.isSelected()) {
            if (new Mensaje().showConfirmation("Nueva Aprobación", getStage(),
                    "Desea crear una nueva aprobación?")) {
                newApproval();
            }
        }

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        if (tptGestiones.isSelected()) {
            saveGestion();
        } else if (tptSeguimeinto.isSelected()) {
            saveFollow();
        } else if (tptAprobaciones.isSelected()) {
            saveApproval();
        }

    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {
        if (tptSeguimeinto.isSelected()) {
            tbGestiones.getSelectionModel().select(tptGestiones);
        }

        SpecificSearchViewController controller = (SpecificSearchViewController) FlowController.getInstance()
                .getController("SpecificSearch");

        FlowController.getInstance().goViewInWindowModal("SpecificSearch", getStage(), false);

        Object result = controller.getResult();

        GestionsDto gestion = (GestionsDto) result;
        if (gestion != null) {

            txfIDGestion.textProperty().unbind();
            txfIDGestion.setText(gestion.getId().toString());

            chargeGestion(Long.valueOf(txfIDGestion.getText()));
        }
    }

    @FXML
    void onKeyPressedTxfIDGestion(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfIDGestion.getText().isBlank()) {
            chargeGestion(Long.valueOf(txfIDGestion.getText()));
        }

    }

    @FXML
    void onKeyPressedTxfIdSeguimiento(KeyEvent event) {

    }

    @FXML
    void onSelecitonChangedTptSeguimiento(Event event) {
        if (tptSeguimeinto.isSelected()) {
            if (this.gestion.getId() == null) {
                new Mensaje().showModal(AlertType.INFORMATION, "Seguimiento", getStage(),
                        "Debe de seleccionar una gestion");
                tbGestiones.getSelectionModel().select(tptGestiones);
            }
            txfUsuarioSeguimiento.setText(((UsersDto) AppContext.getInstance().get("User")).getName() + " "
                    + ((UsersDto) AppContext.getInstance().get("User")).getLastNames());
            txfUsuarioSeguimiento.setEditable(false);
            newFollow();
        }

    }

    @FXML
    void onSelectionChangedTptAprobaciones(Event event) {
        if (tptAprobaciones.isSelected()) {
            if (this.gestion == null || this.gestion.getId() == null) {
                // Mostrar mensaje si no se ha seleccionado una gestión
                new Mensaje().showModal(AlertType.INFORMATION, "Aprobaciones", getStage(),
                        "Debe de seleccionar una gestión");
                tbGestiones.getSelectionModel().select(tptGestiones);
            } else {
                // Obtener el usuario actual desde AppContext
                UsersDto currentUser = (UsersDto) AppContext.getInstance().get("User");

                // Verificar si el usuario actual es un aprobador de la gestión
                boolean isApprover = gestion.getApprovers().stream()
                        .anyMatch(user -> user.getId().equals(currentUser.getId()));

                if (!isApprover) {
                    // Mostrar mensaje si el usuario no es un aprobador de esta gestión
                    new Mensaje().showModal(AlertType.INFORMATION, "Aprobaciones", getStage(),
                            "No eres aprobadador de esta gestión");
                    tbGestiones.getSelectionModel().select(tptGestiones);
                }
            }

            txfAprobadorAprobacion.setText(((UsersDto) AppContext.getInstance().get("User")).getName() + " "
                    + ((UsersDto) AppContext.getInstance().get("User")).getLastNames());
            txfAprobadorAprobacion.setEditable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        format();
        initEntities();
        newGestion();
        allRequieredFields();

    }

}
