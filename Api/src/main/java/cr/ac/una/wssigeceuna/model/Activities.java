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
import jakarta.persistence.JoinColumn;
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
@Table(name = "SIS_ACTIVIDADES")
@NamedQueries({
        @NamedQuery(name = "Activities.findAll", query = "SELECT s FROM Activities s", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
        @NamedQuery(name = "Activities.findByID", query = "SELECT s FROM Activities s WHERE s.id = :id"),
/*
 * @NamedQuery(name = "SisActividades.findByActId", query =
 * "SELECT s FROM SisActividades s WHERE s.actId = :actId"),
 * 
 * @NamedQuery(name = "SisActividades.findByActNombre", query =
 * "SELECT s FROM SisActividades s WHERE s.actNombre = :actNombre"),
 * 
 * @NamedQuery(name = "SisActividades.findByActVersion", query =
 * "SELECT s FROM SisActividades s WHERE s.actVersion = :actVersion")
 */ })
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_ACTIVITIES_SEQUENCE", sequenceName = "SIS_ACTIVIDADES_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_ACTIVITIES_SEQUENCE")
    @Column(name = "ACT_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "ACT_NOMBRE")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private List<Subactivities> subactivities;

    @JoinColumn(name = "ACT_AR_ID", referencedColumnName = "AR_ID")
    @ManyToOne(optional = false)
    private Areas area;

    @Version
    @Column(name = "ACT_VERSION")
    private Long version;

    @OneToMany(mappedBy = "activity")
    private List<Gestions> gestions;

    public Activities() {
        subactivities = new ArrayList<>();
    }

    public Activities(Long id) {
        this();
        this.id = id;
    }

    public Activities(ActivitiesDto activitiesDto) {
        this();
        this.id = activitiesDto.getId();
        update(activitiesDto);
    }

    public void update(ActivitiesDto activitiesDto) {
        this.name = activitiesDto.getName();
        this.version = activitiesDto.getVersion();
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

    public List<Subactivities> getSubactivities() {
        return subactivities;
    }

    public void setSubactivities(List<Subactivities> subactivities) {
        this.subactivities = subactivities;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Activities)) {
            return false;
        }
        Activities other = (Activities) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Activities{" + "id=" + id + '}';
    }

}
