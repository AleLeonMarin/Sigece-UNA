package cr.ac.una.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleObjectProperty;

public class UsersDto {

    public SimpleStringProperty id;

    public SimpleStringProperty name;

    public SimpleStringProperty lastNames;

    public SimpleStringProperty idCard;

    public SimpleStringProperty email;

    public SimpleStringProperty phone;

    public SimpleStringProperty cellPhone;

    public SimpleStringProperty language;

    public ObjectProperty<byte[]> photo;

    public SimpleStringProperty user;

    public SimpleStringProperty password;

    public SimpleStringProperty state;

    public SimpleStringProperty status;

    public Boolean modified;

    public Long version;

    public List<RolesDto> roles;

    public AreasDto areas;

    public List<FollowsDto> follows;

    public List<GestionsDto> gestions;

    public List<ApprovalsDto> approvals;

    private String token;

    public UsersDto() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.lastNames = new SimpleStringProperty("");
        this.idCard = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("");
        this.cellPhone = new SimpleStringProperty("");
        this.language = new SimpleStringProperty("");
        this.photo = new SimpleObjectProperty<>(new byte[0]);
        this.user = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.status = new SimpleStringProperty("");
        this.modified = false;
        this.roles = FXCollections.observableArrayList();
        this.follows = FXCollections.observableArrayList();
        this.gestions = FXCollections.observableArrayList();
        this.approvals = FXCollections.observableArrayList();

        this.token = "";
    }

    public Long getId() {
        if (this.id.get() != null && !this.id.get().isEmpty()) {
            return Long.valueOf(this.id.get());
        } else {
            return null;
        }
    }

    public void setId(Long id) {
        this.id.set(String.valueOf(id));
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastNames() {
        return lastNames.get();
    }

    public void setLastNames(String lastNames) {
        this.lastNames.set(lastNames);
    }

    public String getIdCard() {
        return idCard.get();
    }

    public void setIdCard(String idCard) {
        this.idCard.set(idCard);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getCellPhone() {
        return cellPhone.get();
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone.set(cellPhone);
    }

    public String getLanguage() {
        return language.get();
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public byte[] getPhoto() {
        return photo.get();
    }

    public void setPhoto(byte[] photo) {
        this.photo.set(photo);
    }

    public String getUser() {
        return user.get();
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
    
    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<RolesDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesDto> roles) {
        this.roles = roles;
    }

    public AreasDto getAreas() {
        return areas;
    }

    public void setAreas(AreasDto areas) {
        this.areas = areas;
    }

    public List<FollowsDto> getFollows() {
        return follows;
    }

    public void setFollows(List<FollowsDto> follows) {
        this.follows = follows;
    }

    public List<GestionsDto> getGestions() {
        return gestions;
    }

    public void setGestions(List<GestionsDto> gestions) {
        this.gestions = gestions;
    }

    public List<ApprovalsDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalsDto> approvals) {
        this.approvals = approvals;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
