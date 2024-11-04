package cr.ac.una.wssigeceuna.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SIS_ROLES")
@NamedQueries({
        @NamedQuery(name = "Roles.findAll", query = "SELECT s FROM Roles s")
})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_ROLES_SEQUENCE", sequenceName = "SIS_ROLES_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_ROLES_SEQUENCE")
    @Column(name = "ROL_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "ROL_NOMBRE")
    private String name;

    @Version
    @Column(name = "ROL_VERSION")
    private Long version;

    @JoinColumn(name = "ROL_SIS_ID", referencedColumnName = "SIS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Systems system;
    
    
    @JsonbTransient
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SIS_ROLES_USUARIOS", joinColumns = @JoinColumn(name = "SRU_ROL_ID", referencedColumnName = "ROL_ID"), inverseJoinColumns = @JoinColumn(name = "SRU_USER_ID", referencedColumnName = "USER_ID"))
    private List<Users> users = new ArrayList<>();


    public Roles() {
    }

    public Roles(Long id) {
        this();
        this.id = id;
    }

    public Roles(RolesDto rolesDto) {
        this();
        update(rolesDto);
    }

    public void update(RolesDto rolesDto) {
        this.id = rolesDto.getId();
        this.name = rolesDto.getName();
        this.version = rolesDto.getVersion();
        this.system = new Systems(rolesDto.getSystem());
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

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public Systems getSystem() {
        return system;
    }

    public void setSystem(Systems system) {
        this.system = system;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Roles))
            return false;
        Roles other = (Roles) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Roles{" + "id=" + id + '}';
    }
}
