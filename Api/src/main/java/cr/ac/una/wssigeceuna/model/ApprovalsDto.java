package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({
        "id",
        "state",
        "description",
        "comment",
        "solution",
        "date",
        "archive",
        "version",
        "users",
        "gestion"
})

@Schema(description = "Esta clase contiene la informacion de las aprobaciones")
public class ApprovalsDto implements Serializable {

    @Schema(description = "Identificador de la aprobacion", example = "1")
    private Long id;

    @Schema(description = "Estado de la aprobacion", example = "Activo")
    private String state;

    @Schema(description = "Descripcion de la aprobacion", example = "Descripcion de prueba")
    private String description;

    @Schema(description = "Comentario de la aprobacion", example = "Comentario de prueba")
    private String comment;

    @Schema(description = "Solucion de la aprobacion", example = "Solucion de prueba")
    private String solution;

    @Schema(description = "Fecha de la aprobacion", example = "2021-09-01")
    private LocalDate date;

    @Schema(description = "Archivo de la aprobacion")
    private byte[] archive;

    @Schema(description = "Version de la aprobacion", example = "1")
    private Long version;

    @Schema(description = "Usuarios de la aprobacion")
    private UsersDto users;

    @Schema(description = "Gestion de la aprobacion")
    private GestionsDto gestion;

    public ApprovalsDto() {
    }

    public ApprovalsDto(Approvals approvals) {
        this.id = approvals.getId();
        this.state = approvals.getState();
        this.description = approvals.getDescription();
        this.comment = approvals.getComentary();
        this.solution = approvals.getSolution();
        this.date = approvals.getDate();
        this.archive = approvals.getArchive();
        this.version = approvals.getVersion();
        this.users = new UsersDto(approvals.getUser());
        this.gestion = new GestionsDto(approvals.getGestion());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public UsersDto getUsers() {
        return users;
    }

    public void setUsers(UsersDto users) {
        this.users = users;
    }

    public GestionsDto getGestion() {
        return gestion;
    }

    public void setGestion(GestionsDto gestion) {
        this.gestion = gestion;
    }

}
