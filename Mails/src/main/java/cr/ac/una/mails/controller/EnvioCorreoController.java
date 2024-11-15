package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.MailsDto;
import cr.ac.una.mails.model.NotificationsDto;
import cr.ac.una.mails.service.CorreosService;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.Respuesta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EnvioCorreoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnCancelar;

    @FXML
    private Button btnAttach;

    @FXML
    private MFXButton btnEnviar;

    @FXML
    private MFXTextField txtCorreoAsunto;

    @FXML
    private TextArea txtCorreoContenido;

    @FXML
    private MFXTextField txtDireccionCorreo;

    private CorreosService correosService;
    private Mensaje mensaje;

    private MailsDto correo = new MailsDto();
    private ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        correosService = new CorreosService();
        mensaje = new Mensaje();
        this.rb = rb;
    }

    @Override
    public void initialize() {
        limpiarCampos();
        correo = new MailsDto();
    }

    @FXML
    void onActionBtnCancelar(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    void onActionBtnEnviar(ActionEvent event) {
        if (validarCampos()) {
            correo.setDestinatary(txtDireccionCorreo.getText());
            correo.setSubject(txtCorreoAsunto.getText());
            correo.setResult(txtCorreoContenido.getText());
            NotificationsDto notificacion = new NotificationsDto();
            notificacion.setId(1L);
            correo.setNotification(notificacion);
            correo.setContentIds(null);

            Respuesta respuesta = correosService.enviarCorreoAhora(correo);

            if (respuesta.getEstado()) {
                mensaje.show(Alert.AlertType.INFORMATION, rb.getString("successTitle"), rb.getString("successSendEmail"));
                limpiarCampos();
            } else {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorSendEmail") + respuesta.getMensaje());
            }
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningFillAllFields"));
        }
        correo = new MailsDto();
    }

    private boolean validarCampos() {
        return !txtDireccionCorreo.getText().isEmpty()
                && !txtCorreoAsunto.getText().isEmpty()
                && !txtCorreoContenido.getText().isEmpty();
    }

    private void limpiarCampos() {
        txtCorreoAsunto.clear();
        txtCorreoContenido.clear();
        txtDireccionCorreo.clear();
    }

    @FXML
    void clickBtnAttach(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("fileChooserTitleAttach"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] fileData = new byte[(int) file.length()];
                    fis.read(fileData);

                    List<byte[]> attachments = correo.getAttachments();

                    if (attachments == null) {
                        attachments = List.of(fileData);
                    } else {
                        attachments.add(fileData);
                    }

                    correo.setAttachments(attachments);
                } catch (IOException e) {
                    mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorAttachFile") + file.getName());
                }
            }
            mensaje.show(Alert.AlertType.INFORMATION, rb.getString("infoTitleAttach"), rb.getString("infoFilesAttached") + selectedFiles.size());
        }
    }
}
