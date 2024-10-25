package cr.ac.una.mails.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class MultimediaVariablesDto {

    public SimpleLongProperty id;
    public SimpleStringProperty url;
    public SimpleStringProperty type;
    public SimpleLongProperty version;
    public VariablesDto variable;

    public MultimediaVariablesDto() {
        this.id = new SimpleLongProperty();
        this.url = new SimpleStringProperty("");
        this.type = new SimpleStringProperty("");
        this.version = new SimpleLongProperty(1L);
        this.variable = new VariablesDto();
    }

  

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Long getVersion() {
        return version.get();
    }

    public void setVersion(Long version) {
        this.version.set(version);
    }

    public VariablesDto getVariable() {
        return variable;
    }

    public void setVariable(VariablesDto variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "MultimediaVariablesDto{" +
                "id=" + id.get() +
                ", url='" + url.get() + '\'' +
                ", type='" + type.get() + '\'' +
                ", version=" + version.get() +
                ", variable=" + variable +
                '}';
    }
}
