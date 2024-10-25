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
import javafx.scene.control.TextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;

public class EnvioCorreoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnCancelar;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        correosService = new CorreosService();
        mensaje = new Mensaje();
    }

    @Override
    public void initialize() {
    }

    @FXML
    void onActionBtnCancelar(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    void onActionBtnEnviar(ActionEvent event) {
        if (validarCampos()) {
            MailsDto correo = new MailsDto();
            correo.setDestinatary(txtDireccionCorreo.getText());
            correo.setSubject(txtCorreoAsunto.getText());
            correo.setResult(txtCorreoContenido.getText());
            NotificationsDto notificacion = new NotificationsDto();
            notificacion.setId(1L);
            correo.setNotification(notificacion);

            Respuesta respuesta = correosService.enviarCorreoAhora(correo);

            if (respuesta.getEstado()) {
                mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "El correo fue enviado exitosamente.");
                limpiarCampos();
            } else {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Hubo un problema al enviar el correo: " + respuesta.getMensaje());
            }
        } else {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Por favor, rellena todos los campos.");
        }
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
}