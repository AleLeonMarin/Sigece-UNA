package cr.ac.una.wssigeceuna.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalDate;

@JsonbPropertyOrder({
        "id",
        "creationDate",
        "solutionDate",
        "subject",
        "description",
        "state",
        "archive",
        "version",
        "version",
        "Subactuvities",
        "Requester",
        "Assigned",

})

@Schema(description = "Esta clase contiene la informacion de las gestiones")
public class GestionsDto implements Serializable {

    @Schema(description = "Identificador de la gestion", example = "1")
    private Long id;

    @Schema(description = "Fecha de creacion de la gestion", example = "2021-09-01")
    private LocalDate creationDate;

    @Schema(description = "Fecha de solucion de la gestion", example = "2021-09-01")
    private LocalDate solutionDate;

    @Schema(description = "Asunto de la gestion", example = "Asunto de prueba")
    private String subject;

    @Schema(description = "Descripcion de la gestion", example = "Descripcion de prueba")
    private String description;

    @Schema(description = "Estado de la gestion", example = "Activo")
    private String state;

    @Schema(description = "Archivo de la gestion")
    private byte[] archive;

    @Schema(description = "Version de la gestion", example = "1")
    private Long version;

    @Schema(description = "Subactividades de la gestion")
    private SubactivitiesDto Subactivities;

    @Schema(description = "Actividad de la gestion")
    private ActivitiesDto Activity;

    @Schema(description = "Solicitante de la gestion")
    private UsersDto Requester;

    @Schema(description = "Asignado de la gestion")
    private UsersDto Assigned;

    @JsonbTransient
    List<FollowsDto> follows;

    @JsonbTransient
    List<ApprovalsDto> approvals;

    List<UsersDto> approvers;

    List<UsersDto> deletedApprovers;

    public GestionsDto() {

        follows = new ArrayList<>();
        approvals = new ArrayList<>();
        approvers = new ArrayList<>();
        deletedApprovers = new ArrayList<>();
        Activity = new ActivitiesDto();
        Requester = new UsersDto();
        Assigned = new UsersDto();
        Subactivities = new SubactivitiesDto();

    }

    public GestionsDto(Gestions gestions) {
        this();
        this.id = gestions.getId();
        this.creationDate = gestions.getCreationDate();
        this.solutionDate = gestions.getSolutionDate();
        this.subject = gestions.getSubject();
        this.description = gestions.getDescription();
        this.state = gestions.getState();
        this.archive = gestions.getArchive();
        this.version = gestions.getVersion();
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(LocalDate solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public SubactivitiesDto getSubactivities() {
        return Subactivities;
    }

    public void setSubactivities(SubactivitiesDto Subactivities) {
        this.Subactivities = Subactivities;
    }

    public ActivitiesDto getActivity() {
        return Activity;
    }

    public void setActivity(ActivitiesDto Activity) {
        this.Activity = Activity;
    }

    public UsersDto getRequester() {
        return Requester;
    }

    public void setRequester(UsersDto Requester) {
        this.Requester = Requester;
    }

    public UsersDto getAssigned() {
        return Assigned;
    }

    public void setAssigned(UsersDto Assigned) {
        this.Assigned = Assigned;
    }

    public List<FollowsDto> getFollows() {
        return follows;
    }

    public void setFollows(List<FollowsDto> follows) {
        this.follows = follows;
    }

    public List<ApprovalsDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalsDto> approvals) {
        this.approvals = approvals;
    }

    public List<UsersDto> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<UsersDto> approvers) {
        this.approvers = approvers;
    }

    public List<UsersDto> getDeletedApprovers() {
        return deletedApprovers;
    }

    public void setDeletedApprovers(List<UsersDto> deletedApprovers) {
        this.deletedApprovers = deletedApprovers;
    }

}
