package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

@JsonbPropertyOrder({
        "id",
        "subject",
        "destinatary",
        "result",
        "state",
        "date",
        "version"
})

@Schema(description = "Esta clase contiene la informacion de los correos")
public class MailsDto implements Serializable {

    @Schema(description = "ID del correo", example = "1")
    private Long id;

    @Schema(description = "Asunto del correo", example = "Asunto de prueba")
    private String subject;

    @Schema(description = "Destinatario del correo", example = "aleleonmarin01@gmail.com")
    private String destinatary;

    @Schema(description = "Resultado del correo", example = "Enviado")
    private String result;

    @Schema(description = "Estado del correo", example = "Activo")
    private String state;

    @Schema(description = "Fecha del correo", example = "2021-09-01")
    private Date date;


    @Schema(description = "Notificacion a la que pertenece el correo")
    private Notifications notification;

    @Schema(description = "Version del correo", example = "1")
    private Long version;
    
    private List<byte[]> attachments = new ArrayList<>();

    public MailsDto() {
    }

    public MailsDto(Mails mails) {
        this.id = mails.getId();
        this.subject = mails.getSubject();
        this.destinatary = mails.getDestinatary();
        this.result = mails.getResult();
        this.state = mails.getState();
        this.date = mails.getDate();
        this.notification = mails.getNotification();
        this.version = mails.getVersion();
        this.attachments = mails.getAttachments();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDestinatary() {
        return destinatary;
    }

    public void setDestinatary(String destinatary) {
        this.destinatary = destinatary;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Notifications getNotification() {
        return notification;
    }

    public void setNotification(Notifications notification) {
        this.notification = notification;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    public List<byte[]> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<byte[]> attachments) {
        this.attachments = attachments;
    }

}
