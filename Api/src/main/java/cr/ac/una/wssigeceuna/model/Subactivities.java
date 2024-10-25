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
@Table(name = "SIS_SUBACTIVIDADES")
@NamedQueries({
        @NamedQuery(name = "Subactivities.findAll", query = "SELECT s FROM Subactivities s"),
/*
 * @NamedQuery(name = "SisSubactividades.findBySubId", query =
 * "SELECT s FROM SisSubactividades s WHERE s.subId = :subId"),
 * 
 * @NamedQuery(name = "SisSubactividades.findBySubNombre", query =
 * "SELECT s FROM SisSubactividades s WHERE s.subNombre = :subNombre"),
 * 
 * @NamedQuery(name = "SisSubactividades.findBySubVersion", query =
 * "SELECT s FROM SisSubactividades s WHERE s.subVersion = :subVersion")
 */ })
public class Subactivities implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_SUBACTIVITIES_SEQUENCE", sequenceName = "SIS_SUBACTIVIDADES_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SUBACTIVITIES_SEQUENCE")
    @Column(name = "SUB_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "SUB_NOMBRE")
    private String name;

    @Version
    @Column(name = "SUB_VERSION")
    private Long version;

    @JoinColumn(name = "SUB_ACT_ID", referencedColumnName = "ACT_ID")
    @ManyToOne(optional = false)
    private Activities activity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subactivities")
    private List<Gestions> gestion;

    public Subactivities() {
        gestion = new ArrayList<>();
    }

    public Subactivities(Long id) {
        this();
        this.id = id;
    }

    public Subactivities(SubactivitiesDto subactivitiesDto) {
        this();
        this.id = subactivitiesDto.getId();
        update(subactivitiesDto);
    }

    public void update(SubactivitiesDto subactivitiesDto) {
        this.name = subactivitiesDto.getName();
        this.version = subactivitiesDto.getVersion();
        this.activity = new Activities(subactivitiesDto.getActivity());
        this.gestion = new ArrayList<>();
        subactivitiesDto.getGestions().forEach(gestionDto -> {
            this.gestion.add(new Gestions(gestionDto));
        });
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

    public Activities getActivity() {
        return activity;
    }

    public void setActivity(Activities activity) {
        this.activity = activity;
    }

    public List<Gestions> getGestion() {
        return gestion;
    }

    public void setGestion(List<Gestions> gestion) {
        this.gestion = gestion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Subactivities)) {
            return false;
        }
        Subactivities other = (Subactivities) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Subactivities{" + "id=" + id + '}';
    }

}
