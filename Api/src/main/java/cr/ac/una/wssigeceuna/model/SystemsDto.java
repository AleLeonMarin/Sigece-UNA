package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({ "id", "name", "version", "modified", "roles" })
@Schema(description = "Clase que contiene la informacion de los sistemas")
public class SystemsDto implements Serializable {

    @Schema(description = "Id del sistema", example = "1")
    private Long id;

    @Schema(description = "Nombre del sistema", example = "Seguridad")
    private String name;

    @Schema(description = "Sistema modificado", example = "false")
    private Boolean modified;

    @Schema(description = "Version del sistema", example = "1")
    private Long version;

    @Schema(description = "Roles del sistema")
    List<RolesDto> roles;

    public SystemsDto() {

        roles = new ArrayList<>();
        this.modified = false;
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
