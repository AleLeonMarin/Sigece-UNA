package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.MailsDto;
import cr.ac.una.mails.model.NotificationsDto;
import cr.ac.una.mails.model.VariablesDto;
import cr.ac.una.mails.service.CorreosService;
import cr.ac.una.mails.service.MultimediaService;
import cr.ac.una.mails.service.NotificacionService;
import cr.ac.una.mails.service.VariablesService;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.FlowController;
import cr.ac.una.mails.util.Mensaje;
import cr.ac.una.mails.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MassiveMailSenderController extends Controller implements Initializable {

    @FXML
    private Button btnUploadExcel, btnSendMails, btnEnviarCorreosFinales, btnDowloadExcel, btnMaximize;

    @FXML
    private WebView webViewPlantillaFinal, htmlPreview;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<MailsDto> tbvCorreoGenerados;

    @FXML
    private MFXTextField txtAsunto;

    @FXML
    private Button btnMaximizeMail;

    @FXML
    private TableColumn<MailsDto, String> tbcEstado, tbcDestinatario, tbcPlantilla;

    @FXML
    private TableView<NotificationsDto> tbvNotificationProcess;

    @FXML
    private TableColumn<NotificationsDto, String> tbcNotifications;

    private NotificacionService notificacionService = new NotificacionService();
    private CorreosService correosService = new CorreosService();
    private MultimediaService multimediaService = new MultimediaService();
    private VariablesService variablesService = new VariablesService();
    private ObservableList<MailsDto> correosGenerados = FXCollections.observableArrayList();
    private ObservableList<NotificationsDto> notificaciones = FXCollections.observableArrayList();
    private NotificationsDto notificacionSeleccionada;
    private MailsDto correoSeleccionado;

    private Mensaje mensaje = new Mensaje();

    private ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.rb = rb;

        tbcNotifications.setCellValueFactory(new PropertyValueFactory<>("name"));


        configurarSeleccionNotificacion();
        configurarSeleccionCorreo();

        cargarNotificaciones();


        tbcDestinatario.setCellValueFactory(new PropertyValueFactory<>("destinatary"));
        tbcEstado.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbcPlantilla.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    @Override
    public void initialize() {
        cargarNotificaciones();
        correoSeleccionado = new MailsDto();

    }

    private void cargarNotificaciones() {
        Respuesta respuesta = notificacionService.obtenerNotificaciones();
        if (respuesta.getEstado()) {
            List<NotificationsDto> listaNotificaciones = (List<NotificationsDto>) respuesta.getResultado("Notificaciones");
            notificaciones.setAll(listaNotificaciones);
            tbvNotificationProcess.setItems(notificaciones);
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "Error al cargar las notificaciones: " + respuesta.getMensaje());
        }
    }

    private void configurarSeleccionNotificacion() {
        tbvNotificationProcess.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                notificacionSeleccionada = newValue;
                cargarPlantillaHTML(newValue);
                btnDowloadExcel.setDisable(false);
                btnUploadExcel.setDisable(false);
            }
        });
    }

    //NO funciona correctamente
    private void configurarSeleccionCorreo() {
        tbvCorreoGenerados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                correoSeleccionado = newValue;
//                cargarPlantillaFinal(newValue);
            }
        });
    }


    private void cargarPlantillaHTML(NotificationsDto notificacion) {
        if (notificacion != null && notificacion.getHtml() != null) {
            String htmlContent = notificacion.getHtml();
            List<VariablesDto> variables = notificacion.getVariables();
            String finalContent = replaceVariables(htmlContent, variables);
            htmlPreview.getEngine().loadContent(finalContent);
        }
    }



    //NO funciona correctamente
//    private void cargarPlantillaFinal(CorreosDTO correo) {
//        if (correo != null && correo.getCorResultado() != null) {
//            System.out.println("Actualizando WebView con el contenido del correo: " + correo.getCorResultado());
//            webViewPlantillaFinal.getEngine().loadContent(correo.getCorResultado());
//        } else {
//            System.out.println("El correo seleccionado no tiene contenido o es nulo.");
//            webViewPlantillaFinal.getEngine().loadContent("No hay contenido disponible para este correo.</p>");
//        }
//    }

    @FXML
    private void onActionBtnUpload(ActionEvent event) {
        if (notificacionSeleccionada == null) {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectNotificationBeforeUpload"));
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("fileChooserTitleUploadExcel"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(rb.getString("fileChooserExcelFilter"), "*.xlsx"));
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Workbook workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheetAt(0);

                correosGenerados.clear();
                Row headerRow = sheet.getRow(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;

                    Cell correoCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (correoCell == null || correoCell.getCellType() == CellType.BLANK || correoCell.getStringCellValue().trim().isEmpty()) {
                        mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningInvalidEmailRow") + (row.getRowNum() + 1));
                        continue;
                    }

                    MailsDto correoDto = new MailsDto();
                    correoDto.setDestinatary(correoCell.getStringCellValue().trim());

                    if (txtAsunto.getText() != null && !txtAsunto.getText().trim().isEmpty()) {
                        correoDto.setSubject(txtAsunto.getText());
                    } else {
                        correoDto.setSubject(notificacionSeleccionada.getName());
                    }

                    correoDto.setNotification(notificacionSeleccionada);

                    List<byte[]> adjuntos = new ArrayList<>();
                    List<String> contentIds = new ArrayList<>();

                    String contenidoHTML = generarContenidoConVariables(row, headerRow, adjuntos, contentIds);
                    if (contenidoHTML == null) {
                        continue;
                    }

                    correoDto.setAttachments(new ArrayList<>(adjuntos));
                    correoDto.setContentIds(new ArrayList<>(contentIds));

                    correoDto.setResult(contenidoHTML);
                    correoDto.setState("P");
                    correosGenerados.add(correoDto);
                }

                tbvCorreoGenerados.setItems(correosGenerados);
                mensaje.show(Alert.AlertType.INFORMATION, rb.getString("infoTitleSuccess"), rb.getString("infoExcelProcessedSuccessfully"));

            } catch (IOException e) {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorProcessingExcel") + e.getMessage());
            }
        }
    }




    @FXML
    void onActionBtnSend(ActionEvent event) {
        correosGenerados.forEach(correo -> {
            Respuesta respuesta = correosService.guardarCorreo(correo);
            if (!respuesta.getEstado()) {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitle"), rb.getString("errorPersistingEmail") + correo.getDestinatary());
            }
        });

        tbvCorreoGenerados.refresh();
        mensaje.show(Alert.AlertType.INFORMATION, rb.getString("infoTitleSuccess"), rb.getString("infoEmailsSentToDatabase"));
    }

    private String generarContenidoConVariables(Row row, Row headerRow, List<byte[]> adjuntos, List<String> contentIds) {
        String plantillaHTML = notificacionSeleccionada.getHtml(); // HTML inicial de la notificación
        String plantillaHTMLFinal = plantillaHTML; // Se usará para hacer los reemplazos
        List<VariablesDto> variables = notificacionSeleccionada.getVariables(); // Variables asociadas a la notificación

        for (VariablesDto variable : variables) {
            String variableName = "[" + variable.getName() + "]";

            // Para variables multimedia
            if ("Multimedia".equals(variable.getType()) && plantillaHTMLFinal.contains(variableName)) {
                Respuesta respuesta = multimediaService.obtenerImagen(variable.getId());
                if (respuesta.getEstado()) {
                    byte[] multimediaData = (byte[]) respuesta.getResultado("ImagenData");
                    String contentId = UUID.randomUUID().toString() + "@mails.com";

                    adjuntos.add(multimediaData);
                    contentIds.add(contentId);

                    String multimediaHtml = "<img src='cid:" + contentId + "' alt='Multimedia'>";
                    plantillaHTMLFinal = plantillaHTMLFinal.replace(variableName, multimediaHtml);
                } else {
                    plantillaHTMLFinal = plantillaHTMLFinal.replace(variableName, "Recurso multimedia no disponible");
                }
            } else {
                // Para otras variables no multimedia
                String valorVariable = obtenerValorVariable(row, headerRow, variable);
                plantillaHTMLFinal = plantillaHTMLFinal.replace(variableName, valorVariable);
            }
        }
        return plantillaHTMLFinal;
    }

    private String obtenerValorVariable(Row row, Row headerRow, VariablesDto variable) {
        String valor = "";
        for (Cell headerCell : headerRow) {
            if (headerCell.getStringCellValue().equals(variable.getName())) {
                Cell cell = row.getCell(headerCell.getColumnIndex(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            valor = cell.getStringCellValue().trim();
                            break;
                        case NUMERIC:
                            valor = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            valor = String.valueOf(cell.getBooleanCellValue());
                            break;
                        default:
                            valor = "";
                    }
                }
                break;
            }
        }

        // Si la variable es condicional y está vacía, mostramos una advertencia y asignamos un valor predeterminado
        if ("Condicional".equals(variable.getType()) && (valor == null || valor.isEmpty())) {
            valor = "____________"; // Indicador visual de valor faltante para condicionales
        }
        return valor;
    }








    private String buscarValorPorDefecto(String nombreVariable, List<VariablesDto> variables) {
        for (VariablesDto variable : variables) {
            if (variable.getName().equals(nombreVariable)) {
                return variable.getValue() != null ? variable.getValue() : "Valor no encontrado";
            }
        }
        return "Valor no encontrado";
    }


    private boolean esVariableCondicional(String columnName, List<VariablesDto> variables) {
        for (VariablesDto variable : variables) {
            if (variable.getName().equals(columnName) && "Condicional".equals(variable.getType())) {
                return true;
            }
        }
        return false;
    }


    @FXML
    void onActionBtnDowload(ActionEvent event) {
        if (notificacionSeleccionada == null) {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectNotification"));
            return;
        }

        List<VariablesDto> variables = notificacionSeleccionada.getVariables();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(rb.getString("excelSheetTitle"));

        Row headerRow = sheet.createRow(0);
        int colIndex = 0;

        Cell emailCell = headerRow.createCell(colIndex++);
        emailCell.setCellValue(rb.getString("excelHeaderEmail"));

        for (VariablesDto variable : variables) {
            if (!"Multimedia".equals(variable.getType())) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(variable.getName());
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("fileChooserTitleSaveExcel"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(rb.getString("fileChooserExcelFilter"), "*.xlsx"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                workbook.close();
                mensaje.show(Alert.AlertType.INFORMATION, rb.getString("excelSaveTitleSuccess"), rb.getString("excelSaveMessageSuccess"));
            } catch (IOException e) {
                mensaje.show(Alert.AlertType.ERROR, rb.getString("errorTitleSaveExcel"), rb.getString("errorSaveExcel") + e.getMessage());
            }
        }
    }



    @FXML
    void onActionBtnMaximize(ActionEvent event) {
        if (notificacionSeleccionada != null) {
            String htmlContent = notificacionSeleccionada.getHtml();
            AppContext.getInstance().set("htmlContent", htmlContent);
            AppContext.getInstance().set("variables", notificacionSeleccionada.getVariables());
            AppContext.getInstance().set("isPreviewMode", true);
            FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectNotification"));
        }
    }




    @FXML
    void onActionBtnMaximizeMail(ActionEvent event) {
        if (tbvCorreoGenerados.getSelectionModel().getSelectedItem() != null) {
            String htmlContent = tbvCorreoGenerados.getSelectionModel().getSelectedItem().getResult();
            AppContext.getInstance().set("htmlContent", htmlContent);
            FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, rb.getString("warningTitle"), rb.getString("warningSelectMail"));
        }
    }

    private String replaceVariables(String htmlContent, List<VariablesDto> variables) {
        for (VariablesDto variable : variables) {
            String placeholder = "[" + variable.getName() + "]";
            String value = "";
            if ("Condicional".equals(variable.getType())) {
                value = "____________";
            } else if ("Multimedia".equals(variable.getType())) {
                // Obtener la URL de la imagen desde el MultimediaService
                Respuesta respuesta = multimediaService.obtenerImagen(variable.getId());
                if (respuesta.getEstado()) {
                    String multimediaUrl = (String) respuesta.getResultado("ImagenUrl");
                    // Detectar si es imagen o video según la extensión en el valor de la variable
                    if (variable.getValue() != null && variable.getValue().contains(".mp4")) {
                        value = "<video controls><source src='" + "visualizador no soporta viedos"+ "' type='video/mp4'></video>";
                    } else {
                        value = "<img src='" + multimediaUrl + "' alt='Multimedia'>";
                    }
                } else {
                    value = "Recurso multimedia no disponible";
                }
            } else {
                value = variable.getValue() != null ? variable.getValue() : "";
            }
            htmlContent = htmlContent.replace(placeholder, value);
        }
        return htmlContent;
    }
}
