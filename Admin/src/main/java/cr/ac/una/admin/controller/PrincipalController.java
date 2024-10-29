/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

import cr.ac.una.admin.util.FlowController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class PrincipalController extends Controller implements Initializable {

    @FXML
    private Button btnApprovals;

    @FXML
    private Button btnAreas;

    @FXML
    private Button btnFollows;

    @FXML
    private Button btnGestions;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnCalendar;

    @FXML
    private Label gestCompletadasLabel;

    @FXML
    private Label gestEnCursoLabel;

    @FXML
    private Label gestPendientesLabel;

    @FXML
    private ImageView imvTiempo;

    @FXML
    private Label labelTime;

    @FXML
    private TableColumn<?, ?> tbcGest;

    @FXML
    private TableView<?> tbvGestionesSimp;

    @FXML
    private Label userNameLabel;

    String username;

    @FXML
    private PieChart ChartGestiones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username = "Kendall";
        updateGreeting();

        int gestionesPendientes = 10; // Reemplazar con el valor real
        int gestionesEnCurso = 8;
        int gestionesCompletadas = 5;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Pendientes", gestionesPendientes),
                new PieChart.Data("En Curso", gestionesEnCurso),
                new PieChart.Data("Completadas", gestionesCompletadas));

        ChartGestiones.setData(pieChartData);
        ChartGestiones.setTitle("Distribución de Gestiones");

    }

    @Override
    public void initialize() {

    }

    private void updateGreeting() {

        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();

        if (hour >= 6 && hour < 12) {

            labelTime.setText(" Buenos Días, " + username);
            imvTiempo.setImage(
                    new Image(getClass().getResource("/cr/ac/una/admin/resources/dia-nublado.png").toExternalForm()));
        } else if (hour >= 12 && hour < 18) {
            labelTime.setText(" Buenas Tardes, " + username);
            imvTiempo
                    .setImage(new Image(getClass().getResource("/cr/ac/una/admin/resources/dom.png").toExternalForm()));
        } else {
            labelTime.setText(" Buenas Noches, " + username);
            imvTiempo.setImage(
                    new Image(getClass().getResource("/cr/ac/una/admin/resources/luna.png").toExternalForm()));
        }
    }

    @FXML
    void onAcntionBtnApprovals(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("ApprovalsView");

    }

    @FXML
    void onActionBtnAreas(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("AreasView");

    }

    @FXML
    void onActionBtnFollows(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("FollowsView");

    }

    @FXML
    void onActionBtnGestions(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("GestionsView");

    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("SearchView");

    }

    @FXML
    void onActionBtnReports(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("ReportsView");

    }

    @FXML
    void onActionBtnCalendar(ActionEvent event) {

        FlowController.getInstance().goViewInWindow("CalendarView");

    }

}