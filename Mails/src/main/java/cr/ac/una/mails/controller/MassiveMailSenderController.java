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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        // Validar que se haya seleccionado una notificación antes de continuar
        if (notificacionSeleccionada == null) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una notificación antes de cargar el archivo Excel.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar archivo Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
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
                        mensaje.show(Alert.AlertType.WARNING, "Advertencia", "La fila " + (row.getRowNum() + 1) + " no tiene un correo destinatario válido. Revisa tu Excel.");
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

                    correoDto.setResult(contenidoHTML);
                    correoDto.setState("P");
                    correosGenerados.add(correoDto);
                }

                tbvCorreoGenerados.setItems(correosGenerados);
                mensaje.show(Alert.AlertType.INFORMATION, "Carga exitosa", "El archivo Excel se ha procesado correctamente.");

            } catch (IOException e) {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Error al procesar el archivo Excel: " + e.getMessage());
            }
        }
    }




    @FXML
    void onActionBtnSend(ActionEvent event) {
        correosGenerados.forEach(correo -> {
            Respuesta respuesta = correosService.guardarCorreo(correo);
            if (!respuesta.getEstado()) {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Error al persistir correo de: " + correo.getDestinatary());
            }
        });

        tbvCorreoGenerados.refresh();
        mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "Correos enviados a la base de datos, serán enviados automáticamente.");
    }

    private String generarContenidoConVariables(Row row, Row headerRow, List<byte[]> adjuntos, List<String> contentIds) {
        String plantillaHTML = notificacionSeleccionada.getHtml();
        String plantillaHTMLFinal = plantillaHTML;
        List<VariablesDto> variables = notificacionSeleccionada.getVariables();

        for (int i = 1; i < row.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            if (headerCell != null) {
                String columnName = headerCell.getStringCellValue();
                String variable = "[" + columnName + "]";
                String valor = "";

                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
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

                boolean isCondicional = esVariableCondicional(columnName, variables);
                if (isCondicional && (valor == null || valor.isEmpty())) {
                    mensaje.show(Alert.AlertType.WARNING, "Advertencia", "La variable condicional '" + columnName + "' está vacía en la fila " + (row.getRowNum() + 1) + ". Revisa la estructura de tu Excel.");
                    System.out.println("Variable condicional vacía: " + columnName + " en fila: " + (row.getRowNum() + 1));
                    return null;
                }

                if (!isCondicional && (valor == null || valor.isEmpty())) {
                    valor = buscarValorPorDefecto(columnName, variables);
                    if (valor == null || valor.isEmpty()) {
                        continue;
                    }
                }

                plantillaHTMLFinal = plantillaHTMLFinal.replace(variable, valor);
            }
        }

        for (VariablesDto variable : variables) {
            String variableName = "[" + variable.getName() + "]";
            if ("Multimedia".equals(variable.getType()) && plantillaHTMLFinal.contains(variableName)) {
                Respuesta respuesta = multimediaService.obtenerImagen(variable.getId());
                if (respuesta.getEstado()) {
                    byte[] multimediaData = (byte[]) respuesta.getResultado("ImagenData");
                    String contentId = variable.getName() + "@mails.com";

                    adjuntos.add(multimediaData); // Agregar a la lista de adjuntos
                    contentIds.add(contentId);

                    String multimediaHtml = "<img src='cid:" + contentId + "' alt='Multimedia'>";
                    plantillaHTMLFinal = plantillaHTMLFinal.replace(variableName, multimediaHtml);
                } else {
                    plantillaHTMLFinal = plantillaHTMLFinal.replace(variableName, "Recurso multimedia no disponible");
                }
            } else if (plantillaHTMLFinal.contains(variableName)) {
                String defaultValue = variable.getValue() != null ? variable.getValue() : "Valor no encontrado";
                plantillaHTMLFinal = plantillaHTMLFinal.replace(variableName, defaultValue);
            }
        }

        return plantillaHTMLFinal;
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
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una notificación.");
            return;
        }

        List<VariablesDto> variables = notificacionSeleccionada.getVariables();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Plantilla Notificación");

        Row headerRow = sheet.createRow(0);
        int colIndex = 0;

        Cell emailCell = headerRow.createCell(colIndex++);
        emailCell.setCellValue("Correo Destino");

        // Excluir variables multimedia del Excel
        for (VariablesDto variable : variables) {
            if (!"Multimedia".equals(variable.getType())) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(variable.getName());
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Plantilla Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                workbook.close();
                mensaje.show(Alert.AlertType.INFORMATION, "Plantilla Excel guardada", "La plantilla Excel se ha guardado correctamente.");
            } catch (IOException e) {
                mensaje.show(Alert.AlertType.ERROR, "Error al guardar Excel", "Hubo un error al escribir el archivo Excel: " + e.getMessage());
            }
        }
    }



    @FXML
    void onActionBtnMaximize(ActionEvent event) {
        if (notificacionSeleccionada != null) {
            String htmlContent = notificacionSeleccionada.getHtml();
            AppContext.getInstance().set("htmlContent", htmlContent);
            AppContext.getInstance().set("variables", notificacionSeleccionada.getVariables());
            AppContext.getInstance().set("isPreviewMode", true); // Establece la bandera de modo vista previa
            FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una notificación.");
        }
    }




    @FXML
    void onActionBtnMaximizeMail(ActionEvent event) {
        if (tbvCorreoGenerados.getSelectionModel().getSelectedItem() != null) {
            String htmlContent = tbvCorreoGenerados.getSelectionModel().getSelectedItem().getResult();
            AppContext.getInstance().set("htmlContent", htmlContent);
            FlowController.getInstance().goViewInWindowModal("MaxViewHTML", this.getStage(), Boolean.TRUE);
        } else {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una correo.");
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
