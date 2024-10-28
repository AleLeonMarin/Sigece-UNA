package cr.ac.una.mails.controller;

import cr.ac.una.mails.model.VariablesDto;
import cr.ac.una.mails.service.MultimediaService;
import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.Respuesta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MaxViewHTMLController extends Controller implements Initializable {

    @FXML
    private WebView webHTML;

    private MultimediaService multimediaService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        multimediaService = new MultimediaService();
    }

    @Override
    public void initialize() {
        String htmlContent = (String) AppContext.getInstance().get("htmlContent");
        List<VariablesDto> variables = (List<VariablesDto>) AppContext.getInstance().get("variables");

        if (htmlContent != null && !htmlContent.isEmpty() && variables != null) {
            String finalContent = replaceVariables(htmlContent, variables);
            webHTML.getEngine().loadContent(finalContent);
        }
    }

    private String replaceVariables(String htmlContent, List<VariablesDto> variables) {
        for (VariablesDto variable : variables) {
            String placeholder = "[" + variable.getName() + "]";
            String value = "";

            if ("Condicional".equals(variable.getType())) {
                value = "____________";
            } else if ("Multimedia".equals(variable.getType()) && variable.getVarMultimedia() != null) {
                // Obtener la URL de la imagen desde el MultimediaService
                Respuesta respuesta = multimediaService.obtenerImagen(variable.getId());
                if (respuesta.getEstado()) {
                    String multimediaUrl = (String) respuesta.getResultado("ImagenUrl");

                    // Detectar si es imagen o video según la extensión en el valor de la variable
                    if (variable.getValue() != null && variable.getValue().contains(".mp4")) {
                        value = "<video controls><source src='" + multimediaUrl + "' type='video/mp4'></video>";
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
