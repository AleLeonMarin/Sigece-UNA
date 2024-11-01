package cr.ac.una.wssigeceuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({
        "id",
        "name",
        "lastNames",
        "idCard",
        "email",
        "password",
        "phone",
        "cellPhone",
        "language",
        "photo",
        "user",
        "password",
        "state",
        "status",
        "modified",
        "version",
        "role",
        "area",
        "chat",
        "message",
        "follows",
        "Gestions",
        "Approvals", })

@Schema(description = "Clase que contiene la informacion de los usuarios")
public class UsersDto implements Serializable {

    @Schema(description = "Id del usuario", example = "1")
    public Long id;

    @Schema(description = "Nombre del usuario", example = "Alejandro")
    public String name;

    @Schema(description = "Apellidos del usuario", example = "Leon Marin")
    public String lastNames;

    @Schema(description = "Cedula del usuario", example = "119060738")
    public String idCard;

    @Schema(description = "Correo del usuario", example = "aleleonmarin01@gmail.com")
    public String email;

    @Schema(description = "Telefono del usuario", example = "506 84307879")
    public String phone;

    @Schema(description = "Celular del usuario", example = "506 84307879")
    public String cellPhone;

    @Schema(description = "Idioma del usuario", example = "es")
    public String language;

    @Schema(description = "Foto del usuario", example = "foto")
    public byte[] photo;

    @Schema(description = "Usuario del usuario", example = "aleon")
    public String user;

    @Schema(description = "Clave del usuario", example = "aleon10")
    public String password;

    @Schema(description = "Estado del usuario", example = "Activo")
    public String state;

    @Schema(description = "Estado del usuario", example = "En Linea")
    public String status;

    @Schema(description = "Usuario modificado", example = "true")
    public Boolean modified;

    @Schema(description = "Version del usuario", example = "1")
    public Long version;

    public List<RolesDto> roles;

    @JsonbTransient
    private List<RolesDto> eliminatedRoles;

    public AreasDto areas;

    @JsonbTransient
    public List<ChatsDto> chats;

    @JsonbTransient
    public List<MessagesDto> messages;

    public List<FollowsDto> follows;

    public List<GestionsDto> gestions;

    public List<ApprovalsDto> approvals;

    public List<GestionsDto> approvers;

    private String token;

    public UsersDto() {
        this.modified = false;
        this.roles = new ArrayList<>();
        this.eliminatedRoles = new ArrayList<>();
        this.chats = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.follows = new ArrayList<>();
        this.gestions = new ArrayList<>();
        this.approvals = new ArrayList<>();
        this.approvers = new ArrayList<>();
    }

    public UsersDto(Users users) {
        this();
        this.id = users.getId();
        this.name = users.getName();
        this.lastNames = users.getLastNames();
        this.idCard = users.getIdCard();
        this.email = users.getEmail();
        this.phone = users.getPhone();
        this.cellPhone = users.getCellphone();
        this.language = users.getLanguage();
        this.photo = (byte[]) users.getPhoto();
        this.user = users.getUser();
        this.password = users.getPassword();
        this.state = users.getState();
        this.status = users.getStatus();
        this.version = users.getVersion();
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

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<RolesDto> getEliminatedRoles() {
        return eliminatedRoles;
    }

    public void setEliminatedRoles(List<RolesDto> eliminatedRoles) {
        this.eliminatedRoles = eliminatedRoles;
    }

    public AreasDto getAreas() {
        return areas;
    }

    public void setAreas(AreasDto areas) {
        this.areas = areas;
    }

    public List<ChatsDto> getChats() {
        return chats;
    }

    public void setChats(List<ChatsDto> chats) {
        this.chats = chats;
    }

    public List<MessagesDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesDto> messages) {
        this.messages = messages;
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

    public List<GestionsDto> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<GestionsDto> approvers) {
        this.approvers = approvers;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
