package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({
        "id", "name", "version", "modified", "roles"
})

@Schema(description = "Clase que contiene la informacion de los sistemas")
public class SystemsDto implements Serializable {

    @Schema(description = "Id del sistema", example = "1")
    public Long id;

    @Schema(description = "Nombre del sistema", example = "Sistema de Gestion de la CEUNA")
    public String name;

    @Schema(description = "Sistema modificado", example = "false")
    public Boolean modified;

    @Schema(description = "Version del sistema", example = "1")
    public Long version;

    @Schema(description = "Roles del sistema", example = "Administrador")
    public List<RolesDto> roles;

    public SystemsDto() {
        this.modified = false;
        roles = new ArrayList<>();
    }

    public SystemsDto(Systems systems) {
        this();
        this.id = systems.getId();
        this.name = systems.getName();
        this.version = systems.getVersion();
    }

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
