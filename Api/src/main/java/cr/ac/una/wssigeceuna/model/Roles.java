/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
@Table(name = "SIS_ROLES")
@NamedQueries({
        @NamedQuery(name = "Roles.findAll", query = "SELECT s FROM Roles s"),
/*
 * @NamedQuery(name = "SisRoles.findByRolId", query =
 * "SELECT s FROM SisRoles s WHERE s.rolId = :rolId"),
 * 
 * @NamedQuery(name = "SisRoles.findByRolNombre", query =
 * "SELECT s FROM SisRoles s WHERE s.rolNombre = :rolNombre"),
 * 
 * @NamedQuery(name = "SisRoles.findByRolVersion", query =
 * "SELECT s FROM SisRoles s WHERE s.rolVersion = :rolVersion")
 */ })
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
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

    @JoinTable(name = "SIS_ROLES_USUARIOS", joinColumns = {
            @JoinColumn(name = "SRU_ROL_ID", referencedColumnName = "ROL_ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "SRU_USER_ID", referencedColumnName = "USER_ID") })

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Users> users;

    @JoinColumn(name = "ROL_SIS_ID", referencedColumnName = "SIS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Systems system;

    public Roles() {

        users = new ArrayList<>();
    }

    public Roles(Long id){
        this();
        this.id = id;

    }

    public Roles(RolesDto rolesDto) {
        this();
        this.id = rolesDto.getId();
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
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Roles{" + "id=" + id + '}';
    }
    
    

}
