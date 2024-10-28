package cr.ac.una.admin.model;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalDate;

public class GestionsDto implements Serializable {
    public SimpleStringProperty id;
    public ObjectProperty<LocalDate> creationDate;
    public ObjectProperty<LocalDate> solutionDate;
    public SimpleStringProperty subject;
    public SimpleStringProperty description;
    public SimpleBooleanProperty state;
    public ObjectProperty<byte[]> archive;
    public Long version;
    public SubactivitiesDto Subactivities;
    public UsersDto Requester;
    public UsersDto Assigned;
    List<FollowsDto> follows;
    List<ApprovalsDto> approvals;

    public GestionsDto() {

        this.id = new SimpleStringProperty("");
        this.creationDate = new SimpleObjectProperty<>(LocalDate.now());
        this.solutionDate = new SimpleObjectProperty<>();
        this.subject = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.state = new SimpleBooleanProperty();
        this.archive = new SimpleObjectProperty<>(new byte[0]);
        follows = new ArrayList<>();
        approvals = new ArrayList<>();

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

    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }

    public LocalDate getSolutionDate() {
        return solutionDate.get();
    }

    public void setSolutionDate(LocalDate solutionDate) {
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

    public Boolean getState() {
        return state.get();
    }

    public void setState(Boolean state) {
        this.state.set(state);
    }

    public byte[] getArchive() {
        return archive.get();
    }

    public void setArchive(byte[] archive) {
        this.archive.set(archive);
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

}
