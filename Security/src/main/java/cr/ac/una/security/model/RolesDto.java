package cr.ac.una.security.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.io.Serializable;
import java.util.List;

public class RolesDto implements Serializable {

    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public SimpleStringProperty version;
    private SystemsDto system;
    public SimpleBooleanProperty modificado;
    public List<UsersDto> usuariosDto;

    public RolesDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.version = new SimpleStringProperty("1");
        this.system = new SystemsDto();
        this.modificado = new SimpleBooleanProperty(false);
        this.usuariosDto = FXCollections.observableArrayList();
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

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public SystemsDto getSystem() {
        return system;
    }

    public void setSystem(SystemsDto system) {
        this.system = system;
    }

    public Boolean isModified() {
        return modificado.get();
    }

    public void setModified(Boolean modificado) {
        this.modificado.set(modificado);
    }

    public List<UsersDto> getUsuariosDto() {
        return usuariosDto;
    }

    public void setUsuariosDto(List<UsersDto> usuariosDto) {
        this.usuariosDto = usuariosDto;
    }

    @Override
    public String toString() {
        return "RolesDto{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", version=" + version.get() +
                ", system=" + system +
                '}';
    }
}
