package cr.ac.una.mails.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
import java.util.List;

public class GestionsDto implements Serializable {

    private SimpleStringProperty id;
    private SimpleObjectProperty<java.util.Date> creationDate;
    private SimpleObjectProperty<java.util.Date> solutionDate;
    private SimpleStringProperty subject;
    private SimpleStringProperty description;
    private SimpleStringProperty state;
    private Serializable archive;
    private SimpleStringProperty version;
    private SubactivitiesDto subactivities;
    private UsersDto requester;
    private UsersDto assigned;
    private List<FollowsDto> follows;
    private List<ApprovalsDto> approvals;

    public GestionsDto() {
        this.id = new SimpleStringProperty("");
        this.creationDate = new SimpleObjectProperty<>(new java.util.Date());
        this.solutionDate = new SimpleObjectProperty<>(new java.util.Date());
        this.subject = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.version = new SimpleStringProperty("1");
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

    public java.util.Date getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate.set(creationDate);
    }

    public java.util.Date getSolutionDate() {
        return solutionDate.get();
    }

    public void setSolutionDate(java.util.Date solutionDate) {
        this.solutionDate.set(solutionDate);
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public Serializable getArchive() {
        return archive;
    }

    public void setArchive(Serializable archive) {
        this.archive = archive;
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public SubactivitiesDto getSubactivities() {
        return subactivities;
    }

    public void setSubactivities(SubactivitiesDto subactivities) {
        this.subactivities = subactivities;
    }

    public UsersDto getRequester() {
        return requester;
    }

    public void setRequester(UsersDto requester) {
        this.requester = requester;
    }

    public UsersDto getAssigned() {
        return assigned;
    }

    public void setAssigned(UsersDto assigned) {
        this.assigned = assigned;
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

    @Override
    public String toString() {
        return "GestionsDto{" +
                "id=" + id.get() +
                ", creationDate=" + creationDate.get() +
                ", solutionDate=" + solutionDate.get() +
                ", subject='" + subject.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", state='" + state.get() + '\'' +
                ", version=" + version.get() +
                ", subactivities=" + subactivities +
                ", requester=" + requester +
                ", assigned=" + assigned +
                '}';
    }
}
