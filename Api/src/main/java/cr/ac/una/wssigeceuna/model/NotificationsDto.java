package cr.ac.una.wssigeceuna.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({ "id", "name", "html", "version" })

@Schema(description = "Esta clase contiene la informacion de las notificaciones")
public class NotificationsDto implements Serializable{

    @Schema(description = "ID de la notificacion", example = "1")
    private Long id;

    @Schema(description = "Nombre de la notificacion", example = "Notificacion de prueba")
    private String name;

    @Schema(description = "Contenido HTML de la notificacion", example = "<h1>Notificacion</h1>")
    private String html;

    @Schema(description = "Version de la notificacion", example = "1.0")
    private Long version;


    List<VariablesDto> variables;
    List<MailsDto> mails;

    public NotificationsDto() {
    }

    public NotificationsDto(Notifications notifications) {

        this();
        this.id = notifications.getId();
        this.name = notifications.getName();
        this.html = notifications.getHtml();
        this.version = notifications.getVersion();
        
           if (notifications.getVariables() != null) {
        this.variables = new ArrayList<>();
        for (Variables var : notifications.getVariables()) {
            this.variables.add(new VariablesDto(var));
        }
    }

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<VariablesDto> getVariables() {
        return variables;
    }

    public void setVariables(List<VariablesDto> variables) {
        this.variables = variables;
    }

    public List<MailsDto> getMails() {
        return mails;
    }

    public void setMails(List<MailsDto> mails) {
        this.mails = mails;
    }
    

}
