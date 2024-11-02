/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_USUARIOS", schema = "SigeceUNA")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT s FROM Users s", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Users.findByUserPass", query = "SELECT s FROM Users s LEFT JOIN FETCH s.roles WHERE s.user = :user AND s.password = :password", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Users.findById", query = "SELECT s FROM Users s WHERE s.id = :id"),
    @NamedQuery(name = "Users.findByMail", query = "SELECT s FROM Users s WHERE s.email = :mail"),
    @NamedQuery(name = "Users.findByPass", query = "SELECT s FROM Users s WHERE s.password = :password", hints = @QueryHint(name = "eclipselink.refresh", value = "true")), /*
 * @NamedQuery(name = "SisUsuarios.findByUserApellidos", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userApellidos = :userApellidos"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserCedula", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userCedula = :userCedula"),
 * 
 * 
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserTelefono", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userTelefono = :userTelefono"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserCelular", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userCelular = :userCelular"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserIdioma", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userIdioma = :userIdioma"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserUsuario", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userUsuario = :userUsuario"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserClave", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userClave = :userClave"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserEstado", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userEstado = :userEstado"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserStatus", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userStatus = :userStatus"),
 * 
 * @NamedQuery(name = "SisUsuarios.findByUserVersion", query =
 * "SELECT s FROM SisUsuarios s WHERE s.userVersion = :userVersion")
 */})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_USUARIOS_SEQUENCE", sequenceName = "sigeceuna.SIS_USUARIOS_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_USUARIOS_SEQUENCE")
    @Column(name = "USER_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "USER_NOMBRE")
    private String name;

    @Basic(optional = false)
    @Column(name = "USER_APELLIDOS")
    private String lastNames;

    @Basic(optional = false)
    @Column(name = "USER_CEDULA")
    private String idCard;

    @Basic(optional = false)
    @Column(name = "USER_CORREO")
    private String email;

    @Basic(optional = false)
    @Column(name = "USER_TELEFONO")
    private String phone;

    @Basic(optional = false)
    @Column(name = "USER_CELULAR")
    private String cellphone;

    @Basic(optional = false)
    @Column(name = "USER_IDIOMA")
    private String language;

    @Basic(optional = false)
    @Lob
    @Column(name = "USER_FOTO")
    private byte[] photo;

    @Basic(optional = false)
    @Column(name = "USER_USUARIO")
    private String user;

    @Basic(optional = false)
    @Column(name = "USER_CLAVE")
    private String password;

    @Basic(optional = false)
    @Column(name = "USER_ESTADO")
    private String state;

    @Basic(optional = false)
    @Column(name = "USER_STATUS")
    private String status;

    @Version
    @Column(name = "USER_VERSION")
    private Long version;

    @JoinTable(name = "SIS_APROBADORES", joinColumns = {
        @JoinColumn(name = "SGU_USER_ID", referencedColumnName = "USER_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SGU_GES_ID", referencedColumnName = "GES_ID")})
    @ManyToMany
    private List<Gestions> approvers;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    private List<Roles> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receptor")
    private List<Chats> chatsReceptor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emisor")
    private List<Chats> chatsEmisor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Messages> messagesEmisor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<Follows> follows;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requester")
    private List<Gestions> gestionRequester;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assigned")
    private List<Gestions> gestionAssigned;

    @JoinColumn(name = "USER_AR_ID", referencedColumnName = "AR_ID")
    @ManyToOne(optional = false)
    private Areas area;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Approvals> approval;

    public Users() {

        roles = new ArrayList<>();
        chatsReceptor = new ArrayList<>();
        chatsEmisor = new ArrayList<>();
        messagesEmisor = new ArrayList<>();
        follows = new ArrayList<>();
        gestionRequester = new ArrayList<>();
        gestionAssigned = new ArrayList<>();
        approval = new ArrayList<>();
        approvers = new ArrayList<>();

    }

    public Users(Long id) {
        this();
        this.id = id;
    }

    public Users(UsersDto usersDto) {
        this();
        this.id = usersDto.getId();
        update(usersDto);
    }

    public void update(UsersDto usersDto) {
        this.name = usersDto.getName();
        this.lastNames = usersDto.getLastNames();
        this.idCard = usersDto.getIdCard();
        this.email = usersDto.getEmail();
        this.phone = usersDto.getPhone();
        this.cellphone = usersDto.getCellPhone();
        this.language = usersDto.getLanguage();
        this.photo = usersDto.getPhoto();
        this.user = usersDto.getUser();
        this.password = usersDto.getPassword();
        this.state = usersDto.getState();
        this.status = usersDto.getStatus();
        this.version = usersDto.getVersion();
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public List<Chats> getChatsReceptor() {
        return chatsReceptor;
    }

    public void setChatsReceptor(List<Chats> chatsReceptor) {
        this.chatsReceptor = chatsReceptor;
    }

    public List<Chats> getChatsEmisor() {
        return chatsEmisor;
    }

    public void setChatsEmisor(List<Chats> chatsEmisor) {
        this.chatsEmisor = chatsEmisor;
    }

    public List<Messages> getMessagesEmisor() {
        return messagesEmisor;
    }

    public void setMessagesEmisor(List<Messages> messagesEmisor) {
        this.messagesEmisor = messagesEmisor;
    }

    public List<Follows> getFollows() {
        return follows;
    }

    public void setFollows(List<Follows> follows) {
        this.follows = follows;
    }

    public List<Gestions> getGestionRequester() {
        return gestionRequester;
    }

    public void setGestionRequester(List<Gestions> gestionRequester) {
        this.gestionRequester = gestionRequester;
    }

    public List<Gestions> getGestionAssigned() {
        return gestionAssigned;
    }

    public void setGestionAssigned(List<Gestions> gestionAssigned) {
        this.gestionAssigned = gestionAssigned;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
    }

    public List<Approvals> getApproval() {
        return approval;
    }

    public void setApproval(List<Approvals> approval) {
        this.approval = approval;
    }

    public List<Gestions> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Gestions> approvers) {
        this.approvers = approvers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Users)) {
            return false;
        }
        Users other = (Users) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;

    }

    @Override
    public String toString() {
        return "cr.ac.una.wssigeceuna.model.SisUsuarios[ id=" + id + " ]";
    }

}
