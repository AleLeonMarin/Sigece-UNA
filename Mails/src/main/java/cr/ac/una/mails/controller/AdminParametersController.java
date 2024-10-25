package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.ParamethersDto;
import cr.ac.una.mails.service.ParametrosService;
import cr.ac.una.mails.util.FlowController;
import cr.ac.una.mails.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AdminParametersController extends Controller implements Initializable {

    @FXML
    private Button btnInfo;
    @FXML
    private MFXButton btnSave;
    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtClave;
    @FXML
    private TextField txtPuerto;
    @FXML
    private TextField txtServer;

    @FXML
    private TextField txtTimeout;

    private ParametrosService parametrosService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        parametrosService = new ParametrosService();
        cargarParametros();
    }

    @Override
    public void initialize() {
        cargarParametros();
    }

    @FXML
    void onActionBtnInfo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("InfoView", this.getStage(), Boolean.FALSE);
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        ParamethersDto parametrosDTO = new ParamethersDto();
        parametrosDTO.setMail(txtCorreo.getText());
        parametrosDTO.setPassword(txtClave.getText());
        parametrosDTO.setPort(Long.parseLong(txtPuerto.getText()));
        parametrosDTO.setServer(txtServer.getText());
        parametrosDTO.setTimeout(Long.parseLong(txtTimeout.getText()));

        Respuesta respuesta = parametrosService.guardarParametros(parametrosDTO);
        if (respuesta.getEstado()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Parámetros guardados correctamente.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error guardando los parámetros: " + respuesta.getMensajeInterno());
            alert.showAndWait();
        }
    }

    private void cargarParametros() {
        Respuesta respuesta = parametrosService.getParametros();
        if (respuesta.getEstado()) {
            ParamethersDto parametrosDTO = (ParamethersDto) respuesta.getResultado("Parametros");

            if (parametrosDTO == null) {
                parametrosDTO = new ParamethersDto();
            }

            txtCorreo.setText(parametrosDTO.getMail());
            txtClave.setText(parametrosDTO.getPassword());
            txtPuerto.setText(String.valueOf(parametrosDTO.getPort()));
            txtServer.setText(parametrosDTO.getServer());
            txtTimeout.setText(String.valueOf(parametrosDTO.getTimeout()));
        } else {
            Logger.getLogger(AdminParametersController.class.getName()).log(Level.SEVERE, "Error cargando los parámetros: " + respuesta.getMensajeInterno());
        }
    }
}
