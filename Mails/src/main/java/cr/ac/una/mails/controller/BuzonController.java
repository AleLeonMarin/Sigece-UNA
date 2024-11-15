package cr.ac.una.mails.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import org.apache.tika.Tika;
import java.util.stream.IntStream;

import cr.ac.una.mails.model.MailsDto;
import cr.ac.una.mails.model.ParamethersDto;
import cr.ac.una.mails.service.CorreosService;
import cr.ac.una.mails.service.ParametrosService;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.FlowController;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.tika.mime.MimeTypes;

public class BuzonController extends Controller implements Initializable {

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnSendAgain;

    private Tika tika = new Tika();

    @FXML
    private MFXButton btnSendNow;

    @FXML
    private MFXButton btnShowContent;

    @FXML
    private MFXButton btnBuscar;


    @FXML
    private Button btnNewMail;

    @FXML
    private MFXFilterComboBox<String> cmbEstado;

    @FXML
    private MFXTextField txtBusqueda;


    @FXML
    private TableColumn<MailsDto, String> tbcDestinatario;

    @FXML
    private TableColumn<MailsDto, String> tbcEstado;

    @FXML
    private TableColumn<MailsDto, String> tbcFecha;

    @FXML
    private TableColumn<MailsDto, Long> tbcId;

    @FXML
    private TableView<MailsDto> tbvMails;

    @FXML
    private TableColumn<MailsDto, Integer> tbcAdjuntos;


    private CorreosService correosService;
    private ObservableList<MailsDto> correosList;

    private Mensaje mensaje;

    @FXML
    private Button btnSaveMailXHora;

    @FXML
    private MFXSpinner<Integer> spinnerCorreosXhora;


    private ParametrosService parametrosService;
    private ParamethersDto parametros;

    private ResourceBundle rb;

    @FXML
    private MFXButton btnDownloadAttachment;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        parametrosService = new ParametrosService();
        correosService = new CorreosService();
        mensaje = new Mensaje();
        correosList = FXCollections.observableArrayList();
        configurarTabla();

        IntegerSpinnerModel spinnerModel = new IntegerSpinnerModel(); // Rango: 1 a 1000, valor inicial: 100
        spinnerCorreosXhora.setSpinnerModel(spinnerModel);


        cargarParametros();

        actualizarCorreos();

        cmbEstado.getItems().addAll(rb.getString("enviados"), rb.getString("porEnviar"), rb.getString("todos"));


    }

    private void cargarParametros() {
        Respuesta respuesta = parametrosService.getParametros();
        if (respuesta.getEstado()) {
            parametros = (ParamethersDto) respuesta.getResultado("Parametros");
            if (parametros != null) {
                spinnerCorreosXhora.setValue(parametros.getTimeout().intValue());
            }
        } else {
            mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorLoadParams") + respuesta.getMensaje());
        }
    }



    private void configurarTabla() {
        tbcDestinatario.setCellValueFactory(new PropertyValueFactory<>("destinatary"));
        tbcEstado.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbcFecha.setCellValueFactory(new PropertyValueFactory<>("date"));
        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));

        tbcAdjuntos.setCellValueFactory(mail -> {
            int attachmentCount = mail.getValue().getAttachments() != null ? mail.getValue().getAttachments().size() : 0;
            return new SimpleObjectProperty<>(attachmentCount);
        });

        tbvMails.setItems(correosList);

        ParametrosService parametrosService = new ParametrosService();

        Respuesta respuesta = parametrosService.getParametros();


    }


    private void actualizarCorreos() {
        Respuesta respuesta = correosService.obtenerTodosLosCorreos();
        if (respuesta.getEstado()) {
            List<MailsDto> correos = (List<MailsDto>) respuesta.getResultado("Correos");
            correosList.setAll(correos);
            tbvMails.refresh();
        }
    }

    @Override
    public void initialize() {
        actualizarCorreos();
    }


    @FXML
    void onActionBtnDelete(ActionEvent event) {
        MailsDto correoSeleccionado = tbvMails.getSelectionModel().getSelectedItem();
        if (correoSeleccionado != null) {
            Respuesta respuesta = correosService.eliminarCorreo(correoSeleccionado.getId());
            if (!respuesta.getEstado()) {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorDeleteMail") + respuesta.getMensaje());
            } else {
                tbvMails.getItems().remove(correoSeleccionado);
                mensaje.show(Alert.AlertType.INFORMATION, rb.getString("successTitle"), rb.getString("successDeleteMail"));
            }
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectMailToDelete"));
        }
    }


//    @FXML
//    void onActionBtnSendAgain(ActionEvent event) {
//        CorreosDTO correoSeleccionado = tbvMails.getSelectionModel().getSelectedItem();
//
//        if (correoSeleccionado != null) {
//            correoSeleccionado.setCorEstado("P");
//
//            Respuesta respuesta = correosService.guardarCorreo(correoSeleccionado);
//
//            if (respuesta.getEstado()) {
//                mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "El estado del correo ha sido cambiado a 'Por enviar'.");
//                tbvMails.refresh();
//            } else {
//                mensaje.show(Alert.AlertType.ERROR, "Error", "Hubo un error al actualizar el estado del correo: " + respuesta.getMensaje());
//            }
//        } else {
//            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un correo para reenviar.");
//        }
//    }


    @FXML
    void onActionBtnSendNow(ActionEvent event) {
       actualizarCorreos();
    }


    @FXML
    void onActionBtnShowContent(ActionEvent event) {
        MailsDto correoSeleccionado = tbvMails.getSelectionModel().getSelectedItem();
        if (correoSeleccionado != null && correoSeleccionado.getResult() != null) {
            String htmlContent = correoSeleccionado.getResult();
            AppContext.getInstance().set("htmlContent", htmlContent);

            FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectMailWithContent"));
        }
    }



    @FXML
    void onActionBtnBuscar(ActionEvent event) {
        String terminoBusqueda = txtBusqueda.getText().trim().toLowerCase();
        String estadoSeleccionado = cmbEstado.getSelectionModel().getSelectedItem();

        if (estadoSeleccionado != null && estadoSeleccionado.equalsIgnoreCase(rb.getString("enviados"))) {
            estadoSeleccionado = "E";
        } else if (estadoSeleccionado != null && estadoSeleccionado.equalsIgnoreCase(rb.getString("porEnviar"))) {
            estadoSeleccionado = "P";
        } else {
            estadoSeleccionado = null;
        }

        String finalEstadoSeleccionado = estadoSeleccionado;
        List<MailsDto> correosFiltrados = correosList.stream()
                .filter(correo -> (terminoBusqueda.isEmpty() || correo.getDestinatary().toLowerCase().contains(terminoBusqueda)
                        || correo.getState().toLowerCase().contains(terminoBusqueda)
                        || String.valueOf(correo.getId()).contains(terminoBusqueda)))
                .filter(correo -> finalEstadoSeleccionado == null || correo.getState().equalsIgnoreCase(finalEstadoSeleccionado))
                .collect(Collectors.toList());

        tbvMails.setItems(FXCollections.observableArrayList(correosFiltrados));
        tbvMails.refresh();
    }

    @FXML
    void onActionBtnNewMail(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("EnvioCorreoView");
    }


    @FXML
    void clickbtnSaveCMailXHora(ActionEvent event) {
        if (parametros != null) {
            parametros.setTimeout(spinnerCorreosXhora.getValue().longValue());
            Respuesta respuesta = parametrosService.guardarParametros(parametros);
            if (respuesta.getEstado()) {
                mensaje.show(Alert.AlertType.INFORMATION, rb.getString("successTitle"), rb.getString("successUpdateTimeout"));
            } else {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorUpdateTimeout") + respuesta.getMensaje());
            }
        } else {
            mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorNoParamsFound"));
        }
    }


    @FXML
    void onActionBtnDownloadAttachment(ActionEvent event) {
        MailsDto correoSeleccionado = tbvMails.getSelectionModel().getSelectedItem();
        if (correoSeleccionado != null && !correoSeleccionado.getAttachments().isEmpty()) {
            List<byte[]> attachments = correoSeleccionado.getAttachments();

            for (int i = 0; i < attachments.size(); i++) {
                byte[] fileContent = attachments.get(i);
                String fileName = "attachment_" + (i + 1);

                // Detectar la extensión correcta del archivo con Tika
                String extension = obtenerExtensionConTika(fileContent);
                fileName += extension;

                try {
                    Files.write(Paths.get(System.getProperty("user.home"), "Downloads", fileName), fileContent);
                    mensaje.show(Alert.AlertType.INFORMATION, rb.getString("successTitle"),
                            rb.getString("successDownloadAttachment") + " " + fileName);
                } catch (IOException e) {
                    mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"),
                            rb.getString("errorDownloadAttachment") + e.getMessage());
                }
            }
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"),
                    rb.getString("warningNoAttachment"));
        }
    }

    private String obtenerExtensionConTika(byte[] fileContent) {
        Path tempFile = null;
        try {
            // Crear archivo temporal para detección
            tempFile = Files.createTempFile("tempfile", null);
            Files.write(tempFile, fileContent);

            String mimeType = tika.detect(tempFile);
            System.out.println("Detected MIME type: " + mimeType);
            return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension(); // Obtener extensión
        } catch (Exception e) {
            return ".bin"; // Si falla, devolver binario
        } finally {
            if (tempFile != null) {
                try {
                    Files.delete(tempFile);
                } catch (IOException ignored) {
                }
            }
        }
    }




}
