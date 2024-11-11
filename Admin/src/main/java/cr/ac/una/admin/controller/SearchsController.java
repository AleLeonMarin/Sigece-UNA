package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.service.GestionService;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Desktop;

public class SearchsController extends Controller implements Initializable {

    @FXML
    private MFXButton btnClean;

    @FXML
    private MFXButton btnExit;

    @FXML
    private MFXButton btnExport;

    @FXML
    private MFXButton btnFIlter;

    @FXML
    private MFXComboBox<String> cmbCriterios;

    @FXML
    private MFXComboBox<String> cmbOperandos;

    @FXML
    private MFXDatePicker dpDates;

    @FXML
    private TableView<GestionsDto> tbvResults;

    @FXML
    private MFXTextField txfValue;

    private ObservableList<GestionsDto> gestionsFilters;

    @FXML
    void onACtionBtnExport(ActionEvent event) {
        createExcel();
    }

    @FXML
    void onActionBtnClean(ActionEvent event) {
        if (new Mensaje().showConfirmation("Limpiar", this.getStage(), "¿Desea limpiar los campos?")) {
            clean();
        }
    }

    @FXML
    void onActionBtnExit(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("PrincipalView");
        this.getStage().close();
    }

    @FXML
    void onActionBtnFilter(ActionEvent event) {
        search();
    }

    private void implementsCriteria() {
        cmbCriterios.getItems().add("Asunto");
        cmbCriterios.getItems().add("Descripcion");
        cmbCriterios.getItems().add("Estado");
        cmbCriterios.getItems().add("Usuario Asignado");
        cmbCriterios.getItems().add("Fecha de creacion");
        cmbCriterios.getItems().add("Fecha de solucion");
        cmbCriterios.getItems().add("Aprobadores");
    }

    private void implementsOperands() {
        cmbOperandos.getItems().add("Igual");
        cmbOperandos.getItems().add("Contiene");
        cmbOperandos.getItems().add("Mayor");
        cmbOperandos.getItems().add("Menor");
        cmbOperandos.getItems().add("Entre");
        cmbOperandos.getItems().add("Y");
        cmbOperandos.getItems().add("O");
    }

    private void enableFields() {
        if ("Fecha de creacion".equals(cmbCriterios.getSelectionModel().getSelectedItem()) ||
                "Fecha de solucion".equals(cmbCriterios.getSelectionModel().getSelectedItem())) {
            dpDates.setDisable(false);
            txfValue.setDisable(true);
        } else {
            dpDates.setDisable(true);
            txfValue.setDisable(false);
        }
    }

    private void search() {
        try {
            GestionService service = new GestionService();
            Respuesta res = service.getGestiones();

            if (res.getEstado()) {
                List<GestionsDto> gestiones = (List<GestionsDto>) res.getResultado("Gestiones");

                // Aplicar filtros en la lista de gestiones
                List<GestionsDto> filteredGestiones = gestiones.stream()
                        .filter(this::applyFilters)
                        .collect(Collectors.toList());

                // Actualizar la tabla con los resultados filtrados
                gestionsFilters.setAll(filteredGestiones);
                tbvResults.setItems(gestionsFilters);
            }
        } catch (Exception e) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(),
                    "Se presento un error al buscar los datos");
        }
    }

    private boolean applyFilters(GestionsDto gestion) {
        String criterio = cmbCriterios.getSelectionModel().getSelectedItem();
        String operando = cmbOperandos.getSelectionModel().getSelectedItem();
        String valor = txfValue.getText();

        // Validar el criterio seleccionado
        if (criterio == null || operando == null) {
            return true; // Si no hay criterio, no aplica filtros adicionales
        }

        switch (criterio) {
            case "Asunto":
                return applyStringFilter(gestion.getSubject(), operando, valor);
            case "Descripcion":
                return applyStringFilter(gestion.getDescription(), operando, valor);
            case "Estado":
                return applyStringFilter(gestion.getState(), operando, valor);
            case "Usuario Asignado":
                return applyStringFilter(gestion.getAssigned() != null ? gestion.getAssigned().getName() : "", operando,
                        valor);
            case "Fecha de creacion":
                return applyDateFilter(gestion.getCreationDate(), operando);
            case "Fecha de solucion":
                return applyDateFilter(gestion.getSolutionDate(), operando);
            case "Aprobadores":
                return applyStringFilter(gestion.getApprovers() != null ? gestion.getApprovers().toString() : "",
                        operando, valor);
            default:
                return true;
        }
    }

    private boolean applyStringFilter(String fieldValue, String operando, String valor) {
        if (fieldValue == null)
            return false;

        switch (operando) {
            case "Igual":
                return fieldValue.equalsIgnoreCase(valor);
            case "Contiene":
                return fieldValue.toLowerCase().contains(valor.toLowerCase());
            default:
                return true;
        }
    }

    private boolean applyDateFilter(LocalDate date, String operando) {
        if (date == null || dpDates.getValue() == null)
            return false;

        switch (operando) {
            case "Mayor":
                return date.isAfter(dpDates.getValue());
            case "Menor":
                return date.isBefore(dpDates.getValue());
            case "Igual":
                return date.isEqual(dpDates.getValue());
            case "Entre":
                return date.isAfter(dpDates.getValue()) && date.isBefore(dpDates.getValue());
            case "Y":
                return date.isAfter(dpDates.getValue()) || date.isBefore(dpDates.getValue());
            case "O":
                return date.isAfter(dpDates.getValue()) || date.isBefore(dpDates.getValue());
            case "Contiene":
                return date.toString().contains(dpDates.getValue().toString());
            default:
                return true;
        }
    }

    private void clean() {
        cmbCriterios.getSelectionModel().clearSelection();
        cmbOperandos.getSelectionModel().clearSelection();
        dpDates.setValue(null);
        txfValue.clear();
        tbvResults.getItems().clear();
    }

    private void populateTable() {
        TableColumn<GestionsDto, String> tbcAsunto = new TableColumn<>("Asunto");
        tbcAsunto.setCellValueFactory(cd -> cd.getValue().subject);

        TableColumn<GestionsDto, String> tbcDescripcion = new TableColumn<>("Descripcion");
        tbcDescripcion.setCellValueFactory(cd -> cd.getValue().description);

        TableColumn<GestionsDto, String> tbcEstado = new TableColumn<>("Estado");
        tbcEstado.setCellValueFactory(cd -> cd.getValue().state);

        TableColumn<GestionsDto, String> tbcAsignado = new TableColumn<>("Asignado");
        tbcAsignado.setCellValueFactory(cd -> cd.getValue().Assigned.name);

        TableColumn<GestionsDto, String> tbcFechaCreacion = new TableColumn<>("Fecha de creacion");
        tbcFechaCreacion
                .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCreationDate().toString()));

        TableColumn<GestionsDto, String> tbcFechaSolucion = new TableColumn<>("Fecha de solucion");
        tbcFechaSolucion
                .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSolutionDate().toString()));

        TableColumn<GestionsDto, String> tbcAprobadores = new TableColumn<>("Aprobadores");
        tbcAprobadores.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getApprovers().toString()));

        tbvResults.getColumns()
                .addAll(Arrays.asList(tbcAsunto, tbcDescripcion, tbcEstado, tbcAsignado, tbcFechaCreacion,
                        tbcFechaSolucion, tbcAprobadores));
    }

    private void createExcel() {
        // Crear un libro de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Gestiones");

        // Crear estilo de encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Crear encabezados de la tabla
        Row headerRow = sheet.createRow(0);
        String[] headers = { "Asunto", "Descripcion", "Estado", "Asignado", "Fecha de creacion", "Fecha de solucion",
                "Aprobadores" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar los datos de la tabla
        ObservableList<GestionsDto> data = tbvResults.getItems();
        int rowNum = 1;
        for (GestionsDto gestion : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(gestion.getSubject());
            row.createCell(1).setCellValue(gestion.getDescription());
            row.createCell(2).setCellValue(gestion.getState());
            row.createCell(3).setCellValue(gestion.getAssigned() != null ? gestion.getAssigned().getName() : "");
            row.createCell(4)
                    .setCellValue(gestion.getCreationDate() != null ? gestion.getCreationDate().toString() : "");
            row.createCell(5)
                    .setCellValue(gestion.getSolutionDate() != null ? gestion.getSolutionDate().toString() : "");
            row.createCell(6).setCellValue(gestion.getApprovers() != null ? gestion.getApprovers().toString() : "");
        }

        // Ajustar el ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Guardar el archivo y abrirlo
        File file = new File("Gestiones.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Exportar a Excel", getStage(),
                    "Archivo Excel creado exitosamente.");

            // Intentar abrir el archivo automáticamente
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Abrir archivo", getStage(),
                        "El sistema no soporta abrir archivos automáticamente.");
            }
        } catch (IOException e) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error al exportar", getStage(),
                    "No se pudo crear el archivo Excel.");
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txfValue.setDisable(true);
        dpDates.setDisable(true);
        cmbCriterios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> enableFields());
        implementsCriteria();
        implementsOperands();
        gestionsFilters = FXCollections.observableArrayList();
        tbvResults.setItems(gestionsFilters);
        populateTable();
    }

    @Override
    public void initialize() {
        // Deja esto vacío si no necesitas usar este método en particular
    }
}
