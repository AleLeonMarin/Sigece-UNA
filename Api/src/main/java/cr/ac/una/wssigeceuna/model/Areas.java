/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "SIS_AREAS")
@NamedQueries({
        @NamedQuery(name = "Areas.findAll", query = "SELECT s FROM Areas s", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "Areas.findById", query = "SELECT s FROM Areas s WHERE s.id = :id"),
/*
 * @NamedQuery(name = "SisAreas.findByArId", query =
 * "SELECT s FROM SisAreas s WHERE s.arId = :arId"),
 * 
 * @NamedQuery(name = "SisAreas.findByArNombre", query =
 * "SELECT s FROM SisAreas s WHERE s.arNombre = :arNombre"),
 * 
 * @NamedQuery(name = "SisAreas.findByArEstado", query =
 * "SELECT s FROM SisAreas s WHERE s.arEstado = :arEstado"),
 * 
 * @NamedQuery(name = "SisAreas.findByArVersion", query =
 * "SELECT s FROM SisAreas s WHERE s.arVersion = :arVersion")
 */ })
public class Areas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_AREAS_SEQUENCE", sequenceName = "SIS_AREAS_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_AREAS_SEQUENCE")
    @Column(name = "AR_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "AR_NOMBRE")
    private String name;

    @Basic(optional = false)
    @Column(name = "AR_ESTADO")
    private String state;

    @Version
    @Column(name = "AR_VERSION")
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "area")
    private List<Activities> activity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "area")
    private List<Users> users;

    public Areas() {
        activity = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Areas(Long id) {
        this();
        this.id = id;
    }

    public Areas(AreasDto area) {
        this();
        this.id = area.getId();
        update(area);
    }

    public void update(AreasDto area) {
        this.name = area.getName();
        this.state = area.getState();
        this.version = area.getVersion();
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Activities> getActivity() {
        return activity;
    }

    public void setActivity(List<Activities> activity) {
        this.activity = activity;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Areas)) {
            return false;
        }
        Areas other = (Areas) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Areas{" + "id=" + id + '}';
    }

}
