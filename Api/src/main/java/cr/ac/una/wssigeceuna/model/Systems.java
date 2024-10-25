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
@Table(name = "SIS_SISTEMAS")
@NamedQueries({
        @NamedQuery(name = "Systems.findAll", query = "SELECT s FROM Systems s"),
        @NamedQuery(name = "Systems.findById", query = "SELECT s FROM Systems s WHERE s.id = :id"),
/*
 * @NamedQuery(name = "SisSistemas.findBySisId", query =
 * "SELECT s FROM SisSistemas s WHERE s.sisId = :sisId"),
 * 
 * @NamedQuery(name = "SisSistemas.findBySisVersion", query =
 * "SELECT s FROM SisSistemas s WHERE s.sisVersion = :sisVersion")
 */ })
public class Systems implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GENERATOR_SISTEMAS_SEQUENCE", sequenceName = "SIS_SISTEMAS_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SISTEMAS_SEQUENCE")
    @Column(name = "SIS_ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "SIS_NOMBRE")
    private String name;
    @Version
    @Column(name = "SIS_VERSION")
    private Long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "system", fetch = FetchType.LAZY)
    private List<Roles> roles;

    public Systems() {

        roles = new ArrayList<>();
    }

    public Systems(Long id) {
        this();
        this.id = id;
    }

    public Systems(SystemsDto systemsDto) {
        this();
        this.id = systemsDto.getId();
        update(systemsDto);
    }

    public void update(SystemsDto systemsDto) {
        this.name = systemsDto.getName();
        this.version = systemsDto.getVersion();
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

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Systems)) {
            return false;
        }
        Systems other = (Systems) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Systems{" + "id=" + id + '}';
    }

}
