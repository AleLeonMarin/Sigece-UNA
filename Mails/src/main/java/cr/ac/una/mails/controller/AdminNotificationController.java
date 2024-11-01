package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.NotificationsDto;
import cr.ac.una.mails.model.VariablesDto;
import cr.ac.una.mails.service.MultimediaService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Base64;
public class AdminNotificationController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button btnAttachImage;

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

    private File currentTempFile;


    byte[] multimediaSeleccionada;

    MultimediaService multimediaService = new MultimediaService();

    @FXML
    private ToggleButton btnMultimediaActive;

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
    private ImageView imageView;

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;  // Declarar mediaPlayer a nivel de clase


    @FXML
    private WebView plantillaPreviewFinal;

    private NotificacionService notificacionService;
    private VariablesService variablesService;
    private ObservableList<NotificationsDto> notificaciones;
    private NotificationsDto notificacionSeleccionada;
    private VariablesDto variableSeleccionada;
    private Mensaje mensaje;

    private ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.rb = rb;
        notificacionService = new NotificacionService();
        variablesService = new VariablesService();
        mensaje = new Mensaje();

        btnAttachImage.setDisable(true);

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

        txtVarTipo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnAttachImage.setDisable(!"Multimedia".equals(newValue));
        });


        ObservableList<String> opciones = FXCollections.observableArrayList("Por defecto", "Condicional", "Multimedia", null);
        txtVarTipo.setItems(opciones);

        plantillaCode.setOnKeyReleased(event -> updatePreview());



        btnAttachImage.setDisable(true);
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
        List<VariablesDto> variables = new ArrayList<>(tbvVariables.getItems());

        System.out.println("Numero de variables en updatePreview: " + variables.size());

        String finalContent = replaceVariables(htmlCode, variables);
        plantillaPreviewFinal.getEngine().loadContent(finalContent);
    }


    private String replaceVariables(String htmlContent, List<VariablesDto> variables) {

        String baseUrl = (String) AppContext.getInstance().get("resturl");

        for (VariablesDto variable : variables) {
            String placeholder = "[" + variable.getName() + "]";
            String value = "";

            if ("Condicional".equals(variable.getType())) {
                value = "___________";
            } else if ("Por defecto".equals(variable.getType())) {
                value = variable.getValue() != null ? variable.getValue() : "";
            } else if ("Multimedia".equals(variable.getType()) && variable.getVarMultimedia() != null) {
                Respuesta respuesta = multimediaService.obtenerImagen(variable.getId());
                if (respuesta.getEstado()) {
                    String multimediaUrl = (String) respuesta.getResultado("ImagenUrl");
                    value = variable.getValue() != null && variable.getValue().contains(".mp4")
                            ? "[previsualizador no soporta videos]"
                            : "<img src='" + baseUrl+"multimedia/imagen/" + variable.getId() + "' alt='Multimedia'>";
                } else {
                    value = "Recurso multimedia no disponible";
                }
            } else {
                value = variable.getValue() != null ? variable.getValue() : "";
            }

            // Realiza la sustitución del placeholder en el contenido HTML
            htmlContent = htmlContent.replace(placeholder, value);
        }
        return htmlContent;
    }



    private void cargarPlantilla(NotificationsDto notificacion) {
        plantillaCode.setText(notificacion.getHtml());
        txtNombre.setText(notificacion.getName());
        cargarVariables();
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
                tbvVariables.refresh();
                tbvVariables2.refresh();
            } else {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Error al cargar las variables: " + respuesta.getMensaje());
            }
        }
    }


    private void cargarVariableSeleccionada() {
        limpiarVisualizadores();

        if (variableSeleccionada != null) {
            txtVarNombre.setText(variableSeleccionada.getName());
            txtVarValor.setText(variableSeleccionada.getValue());
            txtVarTipo.setText(variableSeleccionada.getType());
        }

        if (variableSeleccionada != null && "Multimedia".equals(variableSeleccionada.getType()) && variableSeleccionada.getVarMultimedia() != null) {
            try {
                String fileExtension = ".jpg"; // Asumimos imagen por defecto

                // Determinamos la extensión del archivo basándonos en el tipo o el contenido de la variable
                if (variableSeleccionada.getValue() != null && variableSeleccionada.getValue().contains("mp4")) {
                    fileExtension = ".mp4";
                } else if (variableSeleccionada.getValue() != null && variableSeleccionada.getValue().contains("png")) {
                    fileExtension = ".png";
                }

                btnAttachImage.setDisable(false);
                mostrarMultimedia(variableSeleccionada.getVarMultimedia(), fileExtension);
            } catch (Exception e) {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Error al cargar el contenido multimedia desde la base de datos: " + e.getMessage());
            }
        }else {
            btnAttachImage.setDisable(true);
        }
    }

    private void limpiarVisualizadores() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        mediaView.setVisible(false);


        imageView.setImage(null);
        imageView.setVisible(false);


        if (currentTempFile != null && currentTempFile.exists()) {
            if (currentTempFile.delete()) {
                System.out.println("Archivo temporal eliminado: " + currentTempFile.getAbsolutePath());
            } else {
                System.err.println("Error al eliminar el archivo temporal: " + currentTempFile.getAbsolutePath());
            }
            currentTempFile = null;
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
            boolean confirm = mensaje.showConfirmation(
                    rb.getString("confirmDeleteNotificationTitle"),
                    root.getScene().getWindow(),
                    rb.getString("confirmDeleteNotificationMessage")
            );
            if (confirm) {
                Respuesta respuesta = notificacionService.eliminarNotificacion(notificacionSeleccionada.getId());
                if (respuesta.getEstado()) {
                    mensaje.show(Alert.AlertType.INFORMATION, rb.getString("successTitle"), rb.getString("successDeleteNotification"));
                    cargarNotificaciones();
                    limpiarFormulario();
                } else {
                    mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorDeleteNotification") + respuesta.getMensaje());
                }
            }
        } else {
            limpiarFormulario();
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningEmptyNotificationForm"));
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

        limpiarVisualizadores();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || plantillaCode.getText().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningCompleteFields"));
            return;
        }
        String htmlContent = plantillaCode.getText();
        boolean allVariablesPresent = true;
        for (VariablesDto variable : tbvVariables.getItems()) {
            String variablePattern = "[" + variable.getName() + "]";
            if (!htmlContent.contains(variablePattern)) {
                mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningMissingVariable") + variable.getName() + "'.");
                allVariablesPresent = false;
            }
        }
        if (!allVariablesPresent) {
            return;
        }

        NotificationsDto notificacion = notificacionSeleccionada != null ? notificacionSeleccionada : new NotificationsDto();
        notificacion.setName(txtNombre.getText());
        notificacion.setHtml(htmlContent);

        List<VariablesDto> listaActualizada = new ArrayList<>(tbvVariables.getItems());
        for (VariablesDto variable : listaActualizada) {
            variable.setNotification(notificacion);
        }
        notificacion.setVariables(listaActualizada);

        Respuesta respuesta = notificacionService.guardarNotificacion(notificacion);

        if (respuesta.getEstado()) {
            mensaje.show(Alert.AlertType.INFORMATION, rb.getString("successTitle"),
                    notificacionSeleccionada != null ? rb.getString("successUpdateNotification") : rb.getString("successSaveNotification"));
            cargarNotificaciones();
            limpiarFormulario();
            variablesTemporales.clear();
        } else {
            mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorSaveNotification") + respuesta.getMensaje());
        }
    }


    @FXML
    void onActionBtnSaveVar(ActionEvent event) {
        if (txtVarNombre.getText().isEmpty() || txtVarTipo.getValue() == null || txtVarTipo.getValue().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningCompleteVarFields"));
            return;
        }

        if ("Condicional".equals(txtVarTipo.getValue()) && !txtVarValor.getText().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningConditionalVarNoContent"));
            return;
        }

        if ("Por defecto".equals(txtVarTipo.getValue()) && txtVarValor.getText().isEmpty()) {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningDefaultVarContent"));
            return;
        }

        if (variableSeleccionada != null) {
            variableSeleccionada.setName(txtVarNombre.getText());
            variableSeleccionada.setType(txtVarTipo.getValue());

            if ("Multimedia".equals(txtVarTipo.getValue())) {
                variableSeleccionada.setVarMultimedia(multimediaSeleccionada);
                variableSeleccionada.setValue(txtVarValor.getText());
            } else {
                variableSeleccionada.setValue(txtVarValor.getText());
                variableSeleccionada.setVarMultimedia(null);
            }

            tbvVariables.refresh();
            tbvVariables2.refresh();
        } else {
            VariablesDto nuevaVariable = new VariablesDto();
            nuevaVariable.setName(txtVarNombre.getText());
            nuevaVariable.setType(txtVarTipo.getValue());

            if ("Multimedia".equals(txtVarTipo.getValue())) {
                nuevaVariable.setVarMultimedia(multimediaSeleccionada);
                nuevaVariable.setValue(txtVarValor.getText());
            } else {
                nuevaVariable.setValue(txtVarValor.getText());
                nuevaVariable.setVarMultimedia(null);
            }

            boolean variableYaExiste = tbvVariables.getItems().stream()
                    .anyMatch(var -> var.getName().equalsIgnoreCase(nuevaVariable.getName()));

            if (variableYaExiste) {
                mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningVariableExists"));
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
        limpiarVisualizadores();
    }


    @FXML
    void onActionBtnDeleteVar(ActionEvent event) {
        VariablesDto variableToDelete = tbvVariables.getSelectionModel().getSelectedItem();
        if (variableToDelete != null) {
            boolean confirm = mensaje.showConfirmation(
                    rb.getString("confirmDeleteVariableTitle"),
                    root.getScene().getWindow(),
                    rb.getString("confirmDeleteVariableMessage")
            );

            if (confirm) {
                tbvVariables.getItems().remove(variableToDelete);
                tbvVariables2.setItems(FXCollections.observableArrayList(tbvVariables.getItems()));
                variablesService.eliminarVariable(variableToDelete.getId());
                limpiarFormularioVar();
            }
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectVariable"));
        }
    }


    @FXML
    void onActionBtnNewVar(ActionEvent event) {
        txtVarNombre.requestFocus();
        tbvVariables.getSelectionModel().clearSelection();
        limpiarFormularioVar();
        limpiarVisualizadores();
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

        String updatedHTML = currentHTML.substring(0, cursorPosition) + "[" + variable + "]" + currentHTML.substring(cursorPosition);

        plantillaCode.setText(updatedHTML);
        plantillaCode.positionCaret(cursorPosition + variable.length());
        updatePreview();
    }

    @FXML
    void onActionBtnMax(ActionEvent event) {
        String htmlContent = plantillaCode.getText();
        AppContext.getInstance().set("htmlContent", htmlContent);
        AppContext.getInstance().set("variables", new ArrayList<>(tbvVariables.getItems())); // Guarda las variables

        FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
    }

    @FXML
    void onActionBtnInfo(ActionEvent event) {
        if (notificacionSeleccionada != null) {
            AppContext.getInstance().set("notificacionSeleccionada", notificacionSeleccionada);
            FlowController.getInstance().goViewInWindowModal("InfoNotificationView", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectNotificationForInfo"));
        }
    }


    private byte[] obtenerBytesDeArchivo(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return fileInputStream.readAllBytes();
        }
    }




    /**
     * Obtiene la extensión del archivo.
     */
    private String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
    }



    @FXML
    void clickBtnAttachImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("fileChooserTitle"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(rb.getString("fileChooserExtension"), "*.png", "*.mp4"));

        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                byte[] fileBytes = obtenerBytesDeArchivo(selectedFile);
                String extension = getFileExtension(selectedFile);

                // Mostrar multimedia en el componente adecuado
                mostrarMultimedia(fileBytes, extension);

                // Si es un video, setea el valor del tipo como ".mp4"
                if (extension.equals(".mp4")) {
                    txtVarTipo.setValue("Multimedia");
                    txtVarValor.setText(".mp4");
                } else {
                    txtVarTipo.setValue("Multimedia");
                    txtVarValor.setText(".png");
                }

                // Asigna el byte[] a la variable
                multimediaSeleccionada = fileBytes;
                txtVarTipo.setValue("Multimedia");

            } catch (IOException e) {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorFileLoad"));
            }
        }
    }

    /**
     * Obtiene la extensión del archivo.
     */
    private void mostrarMultimedia(byte[] fileBytes, String fileExtension) {
        // Limpiar cualquier contenido previo
        limpiarVisualizadores();

        try {
            // Crear archivo temporal a partir de byte[]
            currentTempFile = File.createTempFile("tempMedia", fileExtension);
            try (FileOutputStream fos = new FileOutputStream(currentTempFile)) {
                fos.write(fileBytes);
            }

            // Mostrar multimedia en el componente adecuado
            if (fileExtension.equalsIgnoreCase(".mp4")) {
                imageView.setVisible(false);
                mediaView.setVisible(true);

                Media media = new Media(currentTempFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.setAutoPlay(true);

                mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.dispose());

            } else {
                mediaView.setVisible(false);
                imageView.setVisible(true);

                Image image = new Image(currentTempFile.toURI().toString());
                imageView.setImage(image);
            }

        } catch (IOException e) {
            mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorMultimediaLoad") + e.getMessage());
        }
    }







}