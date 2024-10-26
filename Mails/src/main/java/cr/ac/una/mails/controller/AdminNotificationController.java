package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.NotificationsDto;
import cr.ac.una.mails.model.VariablesDto;
import cr.ac.una.mails.service.NotificacionService;
import cr.ac.una.mails.service.VariablesService;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.FlowController;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminNotificationController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnSave;

    @FXML
    private TextArea plantillaCode;

    @FXML
    private WebView plantillaPreview;

    @FXML
    private MFXTextField txtNombre;

    @FXML
    private MFXTextField txtVarNombre;

    @FXML
    private MFXButton btnInfo;

    @FXML
    private MFXComboBox<String> txtVarTipo;

    @FXML
    private MFXTextField txtVarValor;

    @FXML
    private TableView<NotificationsDto> tbvProcesosNotificacion;

    @FXML
    private TableColumn<NotificationsDto, Long> tbcId;

    @FXML
    private TableColumn<NotificationsDto, String> tbcNombre;

    @FXML
    private TableView<VariablesDto> tbvVariables;

    @FXML
    private TableColumn<VariablesDto, String> tbcVarName;

    @FXML
    private TableColumn<VariablesDto, String> tbcContent;

    @FXML
    private TableColumn<VariablesDto, String> tbcType;

    @FXML
    private TableView<VariablesDto> tbvVariables2;

    @FXML
    private TableColumn<VariablesDto, String> tbcVariables2;

    @FXML
    private TableColumn<VariablesDto, String> tbcVariablesTipo2;

    @FXML
    private Button btnSaveBar;

    @FXML
    private Button btnNewVar;

    @FXML
    private Button btnDeleteVar;

    @FXML
    private Button btnMaximazeView;

    @FXML
    private Tab tabConfigHTML;

    @FXML
    private Tab tabConfigVariables;

    private ObservableList<VariablesDto> variablesTemporales = FXCollections.observableArrayList();


    @FXML
    private WebView plantillaPreviewFinal;

    private NotificacionService notificacionService;
    private VariablesService variablesService;
    private ObservableList<NotificationsDto> notificaciones;
    private NotificationsDto notificacionSeleccionada;
    private VariablesDto variableSeleccionada;
    private Mensaje mensaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notificacionService = new NotificacionService();
        variablesService = new VariablesService();
        mensaje = new Mensaje();

        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcVarName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcContent.setCellValueFactory(new PropertyValueFactory<>("value"));
        tbcType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tbcVariables2.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcVariablesTipo2.setCellValueFactory(new PropertyValueFactory<>("type"));

        setupDoubleClickForVariables();

        cargarNotificaciones();


        btnSave.setDisable(true);
        tabConfigHTML.setDisable(true);
        tabConfigVariables.setDisable(true);
        txtNombre.setDisable(true);


        tbvProcesosNotificacion.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                limpiarFormularioVar();
                notificacionSeleccionada = newValue;

                tabConfigHTML.setDisable(false);
                tabConfigVariables.setDisable(false);
                txtNombre.setDisable(false);
                cargarPlantilla(newValue);
                cargarVariables();


                btnSave.setDisable(false);
            }
        });

        tbvVariables.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                limpiarFormularioVar();
                variableSeleccionada = newValue;
                cargarVariableSeleccionada();
            }
        });

        ObservableList<String> opciones = FXCollections.observableArrayList("Por defecto", "Condicional", null);
        txtVarTipo.setItems(opciones);

        plantillaCode.setOnKeyReleased(event -> updatePreview());
    }


    @Override
    public void initialize() {
        cargarNotificaciones();

        tbvVariables.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                limpiarFormularioVar();
                variableSeleccionada = newValue;
                cargarVariableSeleccionada();
            }
        });

    }

    private void updatePreview() {
        String htmlCode = plantillaCode.getText();
        plantillaPreviewFinal.getEngine().loadContent(htmlCode);
    }

    private void cargarPlantilla(NotificationsDto notificacion) {
        plantillaCode.setText(notificacion.getHtml());
        txtNombre.setText(notificacion.getName());
        updatePreview();
    }

    private void cargarVariables() {
        if (notificacionSeleccionada != null) {
            Respuesta respuesta = variablesService.obtenerVariablesPorNotificacion(notificacionSeleccionada.getId());

            if (respuesta.getEstado()) {
                List<VariablesDto> variablesList = (List<VariablesDto>) respuesta.getResultado("Variables");
                ObservableList<VariablesDto> variablesObservableList = FXCollections.observableArrayList(variablesList);
                tbvVariables.setItems(variablesObservableList);
                tbvVariables2.setItems(FXCollections.observableArrayList(variablesList));
                tbvVariables.refresh(); // Forzar actualización visual
                tbvVariables2.refresh();
            } else {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Error al cargar las variables: " + respuesta.getMensaje());
            }
        }
    }



    private void cargarVariableSeleccionada() {
        if (variableSeleccionada != null) {
            txtVarNombre.setText(variableSeleccionada.getName());
            txtVarValor.setText(variableSeleccionada.getValue());
            txtVarTipo.setValue(variableSeleccionada.getType() != null ? variableSeleccionada.getType() : "");
        }
    }

    private void cargarNotificaciones() {
        Respuesta respuesta = notificacionService.obtenerNotificaciones();
        if (respuesta.getEstado()) {
            List<NotificationsDto> notificaciones = (List<NotificationsDto>) respuesta.getResultado("Notificaciones");
            tbvProcesosNotificacion.getItems().clear();
            tbvProcesosNotificacion.getItems().addAll(notificaciones);
            tbvProcesosNotificacion.refresh();
            tbvVariables.getItems().clear();
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "Error al cargar las notificaciones: " + respuesta.getMensaje());
        }
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {
        btnSave.setDisable(true);
        tabConfigHTML.setDisable(true);
        tabConfigVariables.setDisable(true);
        if (notificacionSeleccionada != null) {
            boolean confirm = mensaje.showConfirmation("Eliminar Notificación", root.getScene().getWindow(), "¿Está seguro de eliminar esta notificación?");
            if (confirm) {
                Respuesta respuesta = notificacionService.eliminarNotificacion(notificacionSeleccionada.getId());

                if (respuesta.getEstado()) {
                    mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "Notificación eliminada correctamente.");
                    cargarNotificaciones();
                    limpiarFormulario();
                } else {
                    mensaje.show(Alert.AlertType.ERROR, "Error", "Error al eliminar la notificación: " + respuesta.getMensaje());
                }
            }
        } else {
            limpiarFormulario();
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Su formulario para la creación de un proceso de notificación " +
                    "ha sido limpiado. " +
                    " Si desea eliminar una  existente notificación debe seleccionarla.");
        }

    }

    @FXML
    void onActionBtnNuevo(ActionEvent event) {
        limpiarFormulario();
        limpiarFormularioVar();
        tbvVariables2.getItems().clear();
        tabConfigHTML.setDisable(false);
        tabConfigVariables.setDisable(false);
        txtNombre.setDisable(false);
        txtNombre.requestFocus();
        tbvVariables.getItems().clear();
        tbvProcesosNotificacion.getSelectionModel().clearSelection();
        btnSave.setDisable(false);
    }


    @FXML
    void onActionBtnSave(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || plantillaCode.getText().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe completar todos los campos.");
            return;
        }

        String htmlContent = plantillaCode.getText();
        boolean allVariablesPresent = true;//Bandera

        for (VariablesDto variable : tbvVariables.getItems()) {
            String variablePattern = "[" + variable.getName() + "]";
            if (!htmlContent.contains(variablePattern)) {
                mensaje.show(Alert.AlertType.WARNING, "Advertencia", "La variable '" + variable.getName() + "' no está implementada en la plantilla HTML.");
                allVariablesPresent = false;
            }
        }

        if (!allVariablesPresent) {
            return;
        }

        NotificationsDto notificacion = notificacionSeleccionada != null ? notificacionSeleccionada : new NotificationsDto();
        notificacion.setName(txtNombre.getText());
        notificacion.setHtml(htmlContent);

        if (notificacionSeleccionada == null) {
            ArrayList<VariablesDto> variables = new ArrayList<>(tbvVariables.getItems());
            for (VariablesDto variable : variables) {
                if (variable.getType().equals("Por defecto")) {
                    variable.setType("P");
                } else if (variable.getType().equals("Condicional")) {
                    variable.setType("C");
                }
            }
            notificacion.setVariables(variables);
        } else {
            List<VariablesDto> listaActualizada = new ArrayList<>(tbvVariables.getItems());
            notificacion.setVariables(listaActualizada);
        }

        Respuesta respuesta = notificacionService.guardarNotificacion(notificacion);

        if (respuesta.getEstado()) {
            mensaje.show(Alert.AlertType.INFORMATION, "Éxito", notificacionSeleccionada != null ? "Notificación actualizada correctamente." : "Notificación guardada correctamente.");
            cargarNotificaciones();
            limpiarFormulario();
            variablesTemporales.clear();
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "Error al guardar la notificación: " + respuesta.getMensaje());
        }
    }




    @FXML
    void onActionBtnSaveVar(ActionEvent event) {
        if (txtVarNombre.getText().isEmpty() || txtVarTipo.getValue() == null || txtVarTipo.getValue().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe completar los campos de nombre y tipo de la variable.");
            return;
        }

        if (txtVarTipo.getValue().equals("Condicional") && !txtVarValor.getText().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Las variables condicionales no deben tener contenido en el campo de valor.");
            return;
        }

        if (txtVarTipo.getValue().equals("Por defecto") && txtVarValor.getText().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Las variables por defecto deben tener contenido en el campo de valor.");
            return;
        }

        if (variableSeleccionada != null) {
            variableSeleccionada.setName(txtVarNombre.getText());
            variableSeleccionada.setType(txtVarTipo.getValue());
            variableSeleccionada.setValue(txtVarTipo.getValue().equals("Condicional") ? "" : txtVarValor.getText());
            tbvVariables.refresh();
            tbvVariables2.refresh();
        } else {
            VariablesDto nuevaVariable = new VariablesDto();
            nuevaVariable.setName(txtVarNombre.getText());
            nuevaVariable.setValue(txtVarTipo.getValue().equals("Condicional") ? "" : txtVarValor.getText());

            boolean variableYaExiste = tbvVariables.getItems().stream()
                    .anyMatch(var -> var.getName().equalsIgnoreCase(nuevaVariable.getName()));

            if (variableYaExiste) {
                mensaje.show(Alert.AlertType.WARNING, "Advertencia", "La variable ya existe en la lista.");
                return;
            }

            tbvVariables.getItems().add(nuevaVariable);
        }

        tbvVariables2.setItems(FXCollections.observableArrayList(tbvVariables.getItems()));
        tbvVariables2.refresh();

        tbvVariables.getSelectionModel().clearSelection();
        tbvVariables2.getSelectionModel().clearSelection();
        variableSeleccionada = null;
        limpiarFormularioVar();
    }


    @FXML
    void onActionBtnDeleteVar(ActionEvent event) {
        VariablesDto variableToDelete = tbvVariables.getSelectionModel().getSelectedItem();

        if (variableToDelete != null) {
            boolean confirm = mensaje.showConfirmation("Eliminar Variable", root.getScene().getWindow(), "¿Está seguro de eliminar esta variable?");

            if (confirm) {
                tbvVariables.getItems().remove(variableToDelete);

                tbvVariables2.setItems(FXCollections.observableArrayList(tbvVariables.getItems()));
                limpiarFormularioVar();
            }
        } else {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una variable para eliminar.");
        }
    }




    @FXML
    void onActionBtnNewVar(ActionEvent event) {
        txtVarNombre.requestFocus();
        tbvVariables.getSelectionModel().clearSelection();
        limpiarFormularioVar();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        plantillaCode.clear();
        notificacionSeleccionada = null;
        updatePreview();
    }

    private void limpiarFormularioVar() {
        txtVarNombre.clear();
        txtVarValor.clear();
        txtVarTipo.clear();
        variableSeleccionada = null;

        tbvVariables.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                limpiarFormularioVar();
                variableSeleccionada = newValue;
                cargarVariableSeleccionada();
            }
        });
    }
    private void setupDoubleClickForVariables() {
        tbvVariables2.setRowFactory(tv -> {
            TableRow<VariablesDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    VariablesDto selectedVariable = row.getItem();
                    insertarVariableEnHTML(selectedVariable.getName());
                }
            });
            return row;
        });
    }

    private void insertarVariableEnHTML(String variable) {
        String currentHTML = plantillaCode.getText();
        int cursorPosition = plantillaCode.getCaretPosition();

        String updatedHTML = currentHTML.substring(0, cursorPosition) + "["+variable+"]" + currentHTML.substring(cursorPosition);

        plantillaCode.setText(updatedHTML);
        plantillaCode.positionCaret(cursorPosition + variable.length());
        updatePreview();
    }

    @FXML
    void onActionBtnMax(ActionEvent event) {
        String htmlContent = plantillaCode.getText();
        AppContext.getInstance().set("htmlContent", htmlContent);
        FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
    }

    @FXML
    void onActionBtnInfo(ActionEvent event) {
        if (notificacionSeleccionada != null) {
            AppContext.getInstance().set("notificacionSeleccionada", notificacionSeleccionada);
            FlowController.getInstance().goViewInWindowModal("InfoNotificationView", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una notificación antes de ver la información.");
        }
    }




}
