package cr.ac.una.wssigeceuna.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;
import java.time.LocalDate;

@JsonbPropertyOrder({
        "id",
        "date",
        "description",
        "archive",
        "version",
        "Gestions",
        "Users"
})

@Schema(description = "Esta clase contiene la informacion de los seguimientos")
public class FollowsDto implements Serializable {

    @Schema(description = "Identificador del seguimiento", example = "1")
    private Long id;

    @Schema(description = "Fecha del seguimiento", example = "2021-09-01")
    private LocalDate date;

    @Schema(description = "Descripcion del seguimiento", example = "Descripcion de prueba")
    private String description;

    @Schema(description = "Archivo del seguimiento")
    private byte[] archive;

    @Schema(description = "Estado del seguimiento", example = "Activo")
    private String state;

    @Schema(description = "Version del seguimiento", example = "1")
    private Long version;

    @Schema(description = "Gestion del seguimiento")
    private GestionsDto Gestions;

    @Schema(description = "Usuario del seguimiento")
    private UsersDto Users;

    public FollowsDto() {
    }

    public FollowsDto(Follows follows) {
        this.id = follows.getId();
        this.date = follows.getDate();
        this.description = follows.getDescription();
        this.archive = follows.getArchive();
        this.version = follows.getVersion();
        this.state = follows.getState();
        this.Gestions = new GestionsDto(follows.getGestion());
        this.Users = new UsersDto(follows.getUsers());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getArchive() {
        return archive;
    }

    public void setArchive(byte[] archive) {
        this.archive = archive;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public GestionsDto getGestions() {
        return Gestions;
    }

    public void setGestions(GestionsDto Gestions) {
        this.Gestions = Gestions;
    }

    public UsersDto getUsers() {
        return Users;
    }

    public void setUsers(UsersDto Users) {
        this.Users = Users;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
