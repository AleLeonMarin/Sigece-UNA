package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.MailsDto;
import cr.ac.una.mails.model.NotificationsDto;
import cr.ac.una.mails.service.CorreosService;
import cr.ac.una.mails.service.NotificacionService;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.FlowController;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.Respuesta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InfoNotificationController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TableColumn<MailsDto, String> tbcDestinatario;

    @FXML
    private TableView<MailsDto> tbvMails;


    @FXML
    private Button btnMaximize;

    @FXML
    private Label txtPorEnviar;

    @FXML
    private Label txtVecesEnviado;

    @FXML
    private Label txtVecesUtilizado;

    private NotificationsDto notificacionSeleccionada;
    private CorreosService correosService;

    private Mensaje mensaje = new Mensaje();

    private NotificacionService notificacionService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initialize() {
        correosService = new CorreosService();
        cargarNotificacion();
        tbcDestinatario.setCellValueFactory(new PropertyValueFactory<>("corDestinatario"));
    }

    private void cargarNotificacion() {
        notificacionSeleccionada = (NotificationsDto) AppContext.getInstance().get("notificacionSeleccionada");

        if (notificacionSeleccionada != null) {
            txtVecesUtilizado.setText(String.valueOf(notificacionSeleccionada.getMails().size()));

            List<MailsDto> correosList = notificacionSeleccionada.getMails();
            ObservableList<MailsDto> correosObservableList = FXCollections.observableArrayList(correosList);
            tbvMails.setItems(correosObservableList);

            long porEnviar = correosList.stream().filter(correo -> correo.getState().equals("P")).count();
            long enviados = correosList.stream().filter(correo -> correo.getState().equals("E")).count();

            txtPorEnviar.setText(String.valueOf(porEnviar));
            txtVecesEnviado.setText(String.valueOf(enviados));
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "No se ha seleccionado ninguna notificación.");
        }
    }


    @FXML
    void onActionBtnMaximize(ActionEvent event) {

        String htmlContent = tbvMails.getSelectionModel().getSelectedItem().getResult();
        AppContext.getInstance().set("htmlContent", htmlContent);
        FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);

    }



}

