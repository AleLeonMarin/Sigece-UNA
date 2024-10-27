package cr.ac.una.mails.controller;

import cr.ac.una.mails.controller.Controller;
import cr.ac.una.mails.model.VariablesDto;
import cr.ac.una.mails.util.AppContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MaxViewHTMLController extends Controller implements Initializable {

    @FXML
    private WebView webHTML;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No se necesita inicialización aquí
    }

    @Override
    public void initialize() {
        // Obtenemos el contenido HTML y las variables
        String htmlContent = (String) AppContext.getInstance().get("htmlContent");
        List<VariablesDto> variables = (List<VariablesDto>) AppContext.getInstance().get("variables");

        if (htmlContent != null && !htmlContent.isEmpty() && variables != null) {
            // Realiza la sustitución de variables
            String finalContent = replaceVariables(htmlContent, variables);
            webHTML.getEngine().loadContent(finalContent);
        }
    }

    private String replaceVariables(String htmlContent, List<VariablesDto> variables) {
        // Sustituir variables por sus valores predeterminados
        for (VariablesDto variable : variables) {
            String placeholder = "[" + variable.getName() + "]";
            String value = variable.getValue() != null ? variable.getValue() : ""; // Usa el valor por defecto
            htmlContent = htmlContent.replace(placeholder, value);
        }
        return htmlContent;
    }
}
