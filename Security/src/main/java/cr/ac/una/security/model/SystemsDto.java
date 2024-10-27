package cr.ac.una.security.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class SystemsDto implements Serializable {

    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public Boolean modified;
    public Long version;
    List<RolesDto> roles;

    public SystemsDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        roles = new ArrayList<>();
        this.modified = false;
    }

    public Long getId() {
        if (id != null && id.get() != null && !id.get().isEmpty()) {
            return Long.valueOf(id.get());
        } else {
            return null;
        }
    }

    public void setId(Long id) {
        this.id.set(id.toString());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public List<RolesDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesDto> roles) {
        this.roles = roles;
    }
}
