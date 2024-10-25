package cr.ac.una.mails.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class NotificationsDto {

    public SimpleLongProperty id;
    public SimpleStringProperty name;
    public SimpleStringProperty html;
    public SimpleLongProperty version;
    public List<VariablesDto> variables;
    public List<MailsDto> mails;

    public NotificationsDto() {
        this.id = new SimpleLongProperty();
        this.name = new SimpleStringProperty("");
        this.html = new SimpleStringProperty("");
        this.version = new SimpleLongProperty(1L);
        this.variables = FXCollections.observableArrayList();
        this.mails = FXCollections.observableArrayList();
    }

    public NotificationsDto(Long id, String name, String html, Long version) {
        this();
        this.id.set(id);
        this.name.set(name);
        this.html.set(html);
        this.version.set(version);
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getHtml() {
        return html.get();
    }

    public void setHtml(String html) {
        this.html.set(html);
    }

    public Long getVersion() {
        return version.get();
    }

    public void setVersion(Long version) {
        this.version.set(version);
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

    @Override
    public String toString() {
        return "NotificationsDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", html='" + html.get() + '\'' +
                ", version=" + version.get() +
                ", variables=" + variables +
                ", mails=" + mails +
                '}';
    }
}
