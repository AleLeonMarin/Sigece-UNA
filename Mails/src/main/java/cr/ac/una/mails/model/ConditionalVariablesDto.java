package cr.ac.una.mails.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ConditionalVariablesDto {

    public SimpleLongProperty id;
    public SimpleStringProperty value;
    public SimpleStringProperty result;
    public SimpleLongProperty version;
    public VariablesDto variable;

    public ConditionalVariablesDto() {
        this.id = new SimpleLongProperty();
        this.value = new SimpleStringProperty("");
        this.result = new SimpleStringProperty("");
        this.version = new SimpleLongProperty(1L);
        this.variable = new VariablesDto();
    }

    public ConditionalVariablesDto(Long id, String value, String result, Long version, VariablesDto variable) {
        this();
        this.id.set(id);
        this.value.set(value);
        this.result.set(result);
        this.version.set(version);
        this.variable = variable;
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getResult() {
        return result.get();
    }

    public void setResult(String result) {
        this.result.set(result);
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
        return "ConditionalVariablesDto{" +
                "id=" + id.get() +
                ", value='" + value.get() + '\'' +
                ", result='" + result.get() + '\'' +
                ", version=" + version.get() +
                ", variable=" + variable +
                '}';
    }
}
