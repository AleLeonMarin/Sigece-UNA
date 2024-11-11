/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.admin.controller;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import cr.ac.una.admin.model.AreasDto;
import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.AreasService;
import cr.ac.una.admin.service.ReportsService;
import cr.ac.una.admin.service.UsersService;
import cr.ac.una.admin.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Kendall Fonseca
 */
public class ReportsController extends Controller implements Initializable {

    @FXML
    private MFXButton btnGestionesUsuarioByFechas;

    @FXML
    private MFXButton btnReporteAllAreas;

    @FXML
    private MFXButton btnReporteArea;

    @FXML
    private MFXButton btnUserRendimiento;

    @FXML
    private MFXDatePicker dateDesde;

    @FXML
    private MFXDatePicker dateHasta;

    @FXML
    private TableColumn<AreasDto, String> tbcAreas;

    @FXML
    private TableColumn<UsersDto, String> tbcEmpleados;

    @FXML
    private TableColumn<UsersDto, String> tbcEmpleados2;

    @FXML
    private TableView<AreasDto> tbvAreas;

    @FXML
    private TableView<UsersDto> tbvEmpleados;

    @FXML
    private TableView<UsersDto> tbvEmpleados2;

    private AreasService areasService;
    private UsersService usersService;
    private ReportsService reportsService;

    @FXML
    void onActionBtnGestionesUsuarioByFechas(ActionEvent event) {
        UsersDto selectedUser = tbvEmpleados.getSelectionModel().getSelectedItem();
        LocalDate startDate = dateDesde.getValue();
        LocalDate endDate = dateHasta.getValue();

        if (selectedUser != null && startDate != null && endDate != null) {
            Respuesta respuesta = reportsService.generateGestionesReport(
                    selectedUser.getId(),
                    startDate.toString(),
                    endDate.toString()
            );

            if (respuesta.getEstado()) {
                byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
                File tempFile;
                try {
                    tempFile = File.createTempFile("ReporteGestionesUsuario_" + selectedUser.getId(), ".pdf");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(pdfReport);
                    }
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempFile);
                    } else {
                        System.out.println("La funcionalidad para abrir PDF no es compatible en esta plataforma.");
                    }
                } catch (IOException e) {
                    System.err.println("Error al guardar o abrir el archivo PDF: " + e.getMessage());
                }
            } else {
                System.err.println("Error generando el reporte: " + respuesta.getMensaje());
            }
        } else {
            System.out.println("Debe seleccionar un empleado y establecer las fechas de inicio y fin.");
        }
    }


    @FXML
    void onActionBtnReporteAllAreas(ActionEvent event) {
        Respuesta respuesta = reportsService.generateGestionesPerformanceReport(null); // null indica todas las áreas
        if (respuesta.getEstado()) {
            byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
            File tempFile;
            try {
                tempFile = File.createTempFile("ReporteTodasLasAreas_", ".pdf");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(pdfReport);
                }
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(tempFile);
                } else {
                    System.out.println("La funcionalidad para abrir PDF no es compatible en esta plataforma.");
                }
            } catch (IOException e) {
                System.err.println("Error al guardar o abrir el archivo PDF: " + e.getMessage());
            }
        } else {
            System.err.println("Error generando el reporte: " + respuesta.getMensaje());
        }
    }

    @FXML
    void onActionBtnReporteArea(ActionEvent event) {
        AreasDto selectedArea = tbvAreas.getSelectionModel().getSelectedItem();
        if (selectedArea != null) {
            Respuesta respuesta = reportsService.generateGestionesPerformanceReport(selectedArea.getId());
            if (respuesta.getEstado()) {
                byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
                File tempFile;
                try {
                    tempFile = File.createTempFile("ReporteArea_" + selectedArea.getId(), ".pdf");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(pdfReport);
                    }
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempFile);
                    } else {
                        System.out.println("La funcionalidad para abrir PDF no es compatible en esta plataforma.");
                    }
                } catch (IOException e) {
                    System.err.println("Error al guardar o abrir el archivo PDF: " + e.getMessage());
                }
            } else {
                System.err.println("Error generando el reporte: " + respuesta.getMensaje());
            }
        } else {
            System.out.println("Debe seleccionar un área.");
        }
    }


    @FXML
    void onActionBtnUserRendimiento(ActionEvent event) {
        UsersDto selectedUser = tbvEmpleados.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Respuesta respuesta = reportsService.generateGestionesAsignadasReport(selectedUser.getId());
            if (respuesta.getEstado()) {
                byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
                File tempFile;
                try {
                    tempFile = File.createTempFile("ReporteGestionesAsignadas_", ".pdf");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(pdfReport);
                    }
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempFile);
                    } else {
                        System.out.println("La funcionalidad para abrir PDF no es compatible en esta plataforma.");
                    }
                } catch (IOException e) {
                    System.err.println("Error al guardar o abrir el archivo PDF: " + e.getMessage());
                }
            } else {
                System.err.println("Error generando el reporte: " + respuesta.getMensaje());
            }
        } else {
            System.out.println("Debe seleccionar un empleado.");
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        areasService = new AreasService();
        usersService = new UsersService();
        reportsService = new ReportsService();

        tbcAreas.setCellValueFactory(new PropertyValueFactory<>("name")); // Nombre del área
        tbcEmpleados.setCellValueFactory(new PropertyValueFactory<>("name")); // Nombre del empleado
        tbcEmpleados2.setCellValueFactory(new PropertyValueFactory<>("name")); // Nombre del empleado

        loadAreas();
        loadEmpleados();
    }

    private void loadAreas() {
        Respuesta respuesta = areasService.getAreas();
        if (respuesta.getEstado()) {
            List<AreasDto> areas = (List<AreasDto>) respuesta.getResultado("Areas");
            ObservableList<AreasDto> areasList = FXCollections.observableArrayList(areas);
            tbvAreas.setItems(areasList);
        } else {
            System.err.println("Error al cargar áreas: " + respuesta.getMensaje());
        }
    }


    private void loadEmpleados() {
        Respuesta respuesta = usersService.getUsers();
        if (respuesta.getEstado()) {
            List<UsersDto> empleados = (List<UsersDto>) respuesta.getResultado("Usuarios");
            ObservableList<UsersDto> empleadosList = FXCollections.observableArrayList(empleados);
            tbvEmpleados.setItems(empleadosList);
            tbvEmpleados2.setItems(empleadosList);
        } else {
            System.err.println("Error al cargar empleados: " + respuesta.getMensaje());
        }
    }

    @Override
    public void initialize() {
    }
    
}
