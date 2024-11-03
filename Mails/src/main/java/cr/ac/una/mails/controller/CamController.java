package cr.ac.una.mails.controller;

import java.net.URL;
import java.util.ResourceBundle;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.WebCam;
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
        this.rb = resources;
        // TODO Auto-generated method stub

    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

}
