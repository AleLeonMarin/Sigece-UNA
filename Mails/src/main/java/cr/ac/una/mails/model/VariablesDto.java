package cr.ac.una.mails.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class VariablesDto {

    public SimpleLongProperty id;
    public SimpleStringProperty name;
    public SimpleStringProperty type;
    public SimpleStringProperty value;
    public SimpleLongProperty version;
    public NotificationsDto notification;
    public List<ConditionalVariablesDto> conditionalVariables;
    public List<MultimediaVariablesDto> multimediaVariables;

    public VariablesDto() {
        this.id = new SimpleLongProperty(0L);
        this.name = new SimpleStringProperty("");
        this.type = new SimpleStringProperty("");
        this.value = new SimpleStringProperty("");
        this.version = new SimpleLongProperty(1L);
        this.notification = new NotificationsDto();
        this.conditionalVariables = FXCollections.observableArrayList();
        this.multimediaVariables = FXCollections.observableArrayList();
    }

    public Long getId() {
        if (this.id != null && !this.id.equals(0L)) {
            return this.id.get();
        } else {
            return null;
        }
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

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
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

    public List<ConditionalVariablesDto> getConditionalVariables() {
        return conditionalVariables;
    }

    public void setConditionalVariables(List<ConditionalVariablesDto> conditionalVariables) {
        this.conditionalVariables = conditionalVariables;
    }

    public List<MultimediaVariablesDto> getMultimediaVariables() {
        return multimediaVariables;
    }

    public void setMultimediaVariables(List<MultimediaVariablesDto> multimediaVariables) {
        this.multimediaVariables = multimediaVariables;
    }

    @Override
    public String toString() {
        return "VariablesDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", type='" + type.get() + '\'' +
                ", value='" + value.get() + '\'' +
                ", version=" + version.get() +
                ", notification=" + notification +
                '}';
    }
}
