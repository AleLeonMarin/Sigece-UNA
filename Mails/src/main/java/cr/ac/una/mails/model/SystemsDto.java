package cr.ac.una.mails.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SystemsDto {

    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public SimpleBooleanProperty modified;
    public SimpleStringProperty version;
    public ObservableList<RolesDto> rolesDto;

    public SystemsDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.modified = new SimpleBooleanProperty(false);
        this.version = new SimpleStringProperty("1");
        this.rolesDto = FXCollections.observableArrayList();
    }

    public Long getId() {
        if (this.id.get() != null && !this.id.get().isEmpty()) {
            return Long.valueOf(this.id.get());
        } else {
            return null;
        }
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Boolean getModified() {
        return modified.get();
    }

    public void setModified(Boolean modified) {
        this.modified.set(modified);
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public ObservableList<RolesDto> getRolesDto() {
        return rolesDto;
    }

    public void setRolesDto(ObservableList<RolesDto> rolesDto) {
        this.rolesDto = rolesDto;
    }

    @Override
    public String toString() {
        return "SystemsDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", modified=" + modified.get() +
                ", version=" + version.get() +
                '}';
    }
}
