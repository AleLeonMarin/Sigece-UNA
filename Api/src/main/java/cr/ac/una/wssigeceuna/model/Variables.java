/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_VARIABLES")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Variables.findAll", query = "SELECT s FROM Variables s"),
        @NamedQuery(name = "Variables.findByVarId", query = "SELECT s FROM Variables s WHERE s.id = :id"),

/*
 * @NamedQuery(name = "SisVariables.findByVarNombre", query =
 * "SELECT s FROM SisVariables s WHERE s.varNombre = :varNombre"),
 * 
 * @NamedQuery(name = "SisVariables.findByTipo", query =
 * "SELECT s FROM SisVariables s WHERE s.tipo = :tipo"),
 * 
 * @NamedQuery(name = "SisVariables.findByVarVersion", query =
 * "SELECT s FROM SisVariables s WHERE s.varVersion = :varVersion")
 */ })
public class Variables implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "VAR_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "VAR_NOMBRE")
    private String name;

    @Basic(optional = false)
    @Column(name = "TIPO")
    private String type;

    @Lob
    @Column(name = "VAR_VALOR")
    private String value;

    @Version
    @Column(name = "VAR_VERSION")
    private Long version;

    @JoinColumn(name = "VAR_NOT_ID", referencedColumnName = "NOT_ID")
    @ManyToOne(optional = false)
    private Notifications notifications;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "variable")
    private List<ConditionalVariables> conditionalVariables;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "variables")
    private List<MultimediaVariables> multimediaVariables;

    public Variables() {
        conditionalVariables = new ArrayList<>();
        multimediaVariables = new ArrayList<>();
    }

    public Variables(Long id) {
        this();
        this.id = id;
    }

    public Variables(VariablesDto variablesDto) {
        this();
        this.id = variablesDto.getId();
    }

    public void update(VariablesDto variablesDto) {
        this.name = variablesDto.getName();
        this.type = variablesDto.getType();
        this.value = variablesDto.getValue();
        this.version = variablesDto.getVersion();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public List<ConditionalVariables> getConditionalVariables() {
        return conditionalVariables;
    }

    public void setConditionalVariables(List<ConditionalVariables> conditionalVariables) {
        this.conditionalVariables = conditionalVariables;
    }

    public List<MultimediaVariables> getMultimediaVariables() {
        return multimediaVariables;
    }

    public void setMultimediaVariables(List<MultimediaVariables> multimediaVariables) {
        this.multimediaVariables = multimediaVariables;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variables)) {
            return false;
        }
        Variables other = (Variables) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Variables{" + "id=" + id + '}';
    }

}
