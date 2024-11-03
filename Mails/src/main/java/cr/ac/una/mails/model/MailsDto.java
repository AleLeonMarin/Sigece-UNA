package cr.ac.una.mails.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailsDto {

    public SimpleLongProperty id;
    public SimpleStringProperty subject;
    public SimpleStringProperty destinatary;
    public SimpleStringProperty result;
    public SimpleStringProperty state;
    public SimpleObjectProperty<Date> date;
    public SimpleLongProperty version;
    public NotificationsDto notification;
    private List <byte[]> attachments;
    private List<String> contentIds;

    public MailsDto() {
        this.id = new SimpleLongProperty();
        this.subject = new SimpleStringProperty("");
        this.destinatary = new SimpleStringProperty("");
        this.result = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("P");
        this.date = new SimpleObjectProperty<>(new Date());
        this.notification = new NotificationsDto();
        this.version = new SimpleLongProperty(0L);
        this.notification = new NotificationsDto();
        this.attachments = new ArrayList<>();
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getDestinatary() {
        return destinatary.get();
    }

    public void setDestinatary(String destinatary) {
        this.destinatary.set(destinatary);
    }

    public String getResult() {
        return result.get();
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public Long getVersion() {
        return version.get();
    }

    public void setVersion(Long version) {
        this.version.set(version);
    }

    public NotificationsDto getNotification() {
        return notification;
    }

    public void setNotification(NotificationsDto notification) {
        this.notification = notification;
    }


    public List <byte[]> getAttachments() {
        return attachments;
    }

    public void setAttachments(List <byte[]> attachments) {
        this.attachments = attachments;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public void setContentIds(List<String> contentIds) {
        this.contentIds = contentIds;
    }

    @Override
    public String toString() {
        return "MailsDto{" +
                "id=" + id.get() +
                ", subject='" + subject.get() + '\'' +
                ", destinatary='" + destinatary.get() + '\'' +
                ", result='" + result.get() + '\'' +
                ", state='" + state.get() + '\'' +
                ", date=" + date.get() +
                ", version=" + version.get() +
                ", notification=" + notification +
                '}';
    }

    public boolean hasAttachments() {
        return attachments != null && !attachments.isEmpty();
    }
}
