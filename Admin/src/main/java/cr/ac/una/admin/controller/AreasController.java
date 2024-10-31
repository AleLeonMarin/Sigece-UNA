package cr.ac.una.admin.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.admin.model.ActivitiesDto;
import cr.ac.una.admin.model.AreasDto;
import cr.ac.una.admin.model.SubactivitiesDto;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

public class AreasController extends Controller implements Initializable {

    @FXML
    private MFXButton bntExit;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXCheckbox chkState;

    @FXML
    private TableColumn<ActivitiesDto, String> tbcIDAct;

    @FXML
    private TableColumn<AreasDto, String> tbcIDArea;

    @FXML
    private TableColumn<SubactivitiesDto, String> tbcIDSub;

    @FXML
    private TableColumn<ActivitiesDto, String> tbcNombreAct;

    @FXML
    private TableColumn<AreasDto, String> tbcNombreArea;

    @FXML
    private TableColumn<SubactivitiesDto, String> tbcNombreSub;

    @FXML
    private TableView<ActivitiesDto> tbvActividades;

    @FXML
    private TableView<AreasDto> tbvAreas;

    @FXML
    private TableView<SubactivitiesDto> tbvSubactividades;

    @FXML
    private MFXTextField txfIDAct;

    @FXML
    private MFXTextField txfIDArea;

    @FXML
    private MFXTextField txfIDSub;

    @FXML
    private MFXTextField txfNombreAct;

    @FXML
    private MFXTextField txfNombreArea;

    @FXML
    private MFXTextField txfNombreSub;

    @FXML
    private Tab tptActividades;

    @FXML
    private Tab tptAreas;

    @FXML
    private Tab tptSubactividades;

    AreasDto area;

    ActivitiesDto activity;

    SubactivitiesDto subactivity;

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    // Table load methods

    private void loadTableAreas() {
        // TODO Auto-generated method stub

    }

    private void loadTableActivities() {
        // TODO Auto-generated method stub

    }

    private void loadTableSubactivities() {
        // TODO Auto-generated method stub

    }

    // bind methods

    private void bindAreas() {
        // TODO Auto-generated method stub

    }

    private void bindActivities() {
        // TODO Auto-generated method stub

    }

    private void bindSubactivities() {
        // TODO Auto-generated method stub

    }

    // unbind methods

    private void unbindAreas() {
        // TODO Auto-generated method stub

    }

    private void unbindActivities() {
        // TODO Auto-generated method stub

    }

    private void unbindSubactivities() {
        // TODO Auto-generated method stub

    }

    // clear methods

    private void newArea() {
        // TODO Auto-generated method stub

    }

    private void newActivity() {
        // TODO Auto-generated method stub

    }

    private void newSubactivity() {
        // TODO Auto-generated method stub

    }

    // charging methods

    private void chargeArea(Long id) {
        // TODO Auto-generated method stub

    }

    private void chargeActivity(Long id) {
        // TODO Auto-generated method stub

    }

    private void chargeSubactivity(Long id) {
        // TODO Auto-generated method stub

    }

    @FXML
    void onActionBntSave(ActionEvent event) {

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

    }

    @FXML
    void onActionBtnExit(ActionEvent event) {

    }

    @FXML
    void onActionBtnNew(ActionEvent event) {

    }

    @FXML
    void onKeyPressedTxfIDArea(KeyEvent event) {

    }

    @FXML
    void onKeyPressedTxfIdAct(KeyEvent event) {

    }

    @FXML
    void onKeyPressedTxfIdSub(KeyEvent event) {

    }

    @FXML
    void onSelectionChangedTptSubactividades(ActionEvent event) {

    }

    @FXML
    void onSelectionChangedTptActividades(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize clasess
        area = new AreasDto();
        activity = new ActivitiesDto();
        subactivity = new SubactivitiesDto();

    }

}
