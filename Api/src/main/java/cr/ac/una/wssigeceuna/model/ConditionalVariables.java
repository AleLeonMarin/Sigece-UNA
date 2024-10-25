/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.wssigeceuna.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;

/**
 *
 * @author aletr
 */
@Entity
@Table(name = "SIS_VARIABLES_CONDICIONALES")
@NamedQueries({
        @NamedQuery(name = "ConditionalVariables.findAll", query = "SELECT s FROM ConditionalVariables s"),
       /*  @NamedQuery(name = "SisVariablesCondicionales.findByVconId", query = "SELECT s FROM SisVariablesCondicionales s WHERE s.vconId = :vconId"),
        @NamedQuery(name = "SisVariablesCondicionales.findByVcondValor", query = "SELECT s FROM SisVariablesCondicionales s WHERE s.vcondValor = :vcondValor"),
        @NamedQuery(name = "SisVariablesCondicionales.findByVconVersion", query = "SELECT s FROM SisVariablesCondicionales s WHERE s.vconVersion = :vconVersion")*/ })
public class ConditionalVariables implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_variables_condicionales_seq")
    @SequenceGenerator(name = "sis_variables_condicionales_seq", sequenceName = "SIS_VARIABLES_CONDICIONALES_SEQ01", allocationSize = 1)
    @Column(name = "VCON_ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "VCOND_VALOR")
    private String value;

    @Lob
    @Column(name = "VCOND_RESULTADO")
    private String result;

    @Version
    @Column(name = "VCON_VERSION")
    private Long version;

    @JoinColumn(name = "VCON_VAR_ID", referencedColumnName = "VAR_ID")
    @ManyToOne(optional = false)
    private Variables variable;

    public ConditionalVariables() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Variables getVariable() {
        return variable;
    }

    public void setVariable(Variables variable) {
        this.variable = variable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConditionalVariables)) {
            return false;
        }
        ConditionalVariables other = (ConditionalVariables) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConditionalVariables{" + "id=" + id + '}';
    }
    
    

}
