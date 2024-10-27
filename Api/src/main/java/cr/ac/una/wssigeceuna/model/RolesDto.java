package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({ "id", "name", "version", "system" })
@Schema(description = "Clase que contiene la informacion de los roles")
public class RolesDto implements Serializable {

    @Schema(description = "Id del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "Administrador")
    private String name;

    @Schema(description = "Version del rol", example = "1")
    private Long version;

    @Schema(description = "Sistema del rol", example = "1")
    private SystemsDto system;

    List<UsersDto> users = new ArrayList<>();

    public RolesDto() {
    }

    public RolesDto(Roles roles) {
        this.id = roles.getId();
        this.name = roles.getName();
        this.version = roles.getVersion();
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
