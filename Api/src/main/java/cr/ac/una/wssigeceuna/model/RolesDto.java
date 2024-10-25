package cr.ac.una.wssigeceuna.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({
        "id", "name", "version", "system"
})

@Schema(description = "Clase que contiene la informacion de los roles")
public class RolesDto {

    @Schema(description = "Id del rol", example = "1")
    public Long id;

    @Schema(description = "Nombre del rol", example = "Administrador")
    public String name;

    @Schema(description = "Version del rol", example = "1")
    public Long version;

    @Schema(description = "Sistema del rol", example = "Sistema de Gestion de la CEUNA")
    public SystemsDto system;

    @JsonbTransient
    List<UsersDto> users;

    public RolesDto() {

        users = new ArrayList<>();
    }

    public RolesDto(Roles roles) {
        this();
        this.id = roles.getId();
        this.name = roles.getName();
        this.version = roles.getVersion();
        this.system = new SystemsDto(roles.getSystem());
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

    public SystemsDto getSystem() {
        return system;
    }

    public void setSystem(SystemsDto system) {
        this.system = system;
    }

    public List<UsersDto> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDto> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "RolesDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", system=" + system +
                '}';
    }

}
