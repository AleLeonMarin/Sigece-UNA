package cr.ac.una.chats.controller;

import java.net.URL;
import java.util.ResourceBundle;
import cr.ac.una.chats.util.AppContext;
import cr.ac.una.chats.util.Mensaje;
import cr.ac.una.chats.util.WebCam;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class CamController extends Controller implements Initializable {

    @FXML
    private MFXButton btnActivate;

    @FXML
    private MFXButton btnStop;

    @FXML
    private MFXButton btnTake;

    @FXML
    private MFXButton btnExit;

    @FXML
    private ImageView imgvCam;

    WebCam webCam;

    Boolean taken = false;

    ResourceBundle rb;

    @FXML
    void onActionBtnActivate(ActionEvent event) {
        webCam = new WebCam(imgvCam);

        webCam.start();

        webCam.updateImageView();

        this.rb = rb;
    }

    @FXML
    void onActionBtnStop(ActionEvent event) {

        webCam.stop();

    }

    @FXML
    void onActionBtnTake(ActionEvent event) {

        webCam.takePhoto();
        taken = true;



        AppContext.getInstance().set("Taken", taken);

    }

    @FXML
    void onActionBtnExit(ActionEvent event) {


        if (new Mensaje().showConfirmation(rb.getString("exitTitle"), getStage(), rb.getString("exitConfirmation"))) {
            webCam.stop();
            this.getStage().close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

}
