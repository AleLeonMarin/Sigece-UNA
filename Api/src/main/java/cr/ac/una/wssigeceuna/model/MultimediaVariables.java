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
@Table(name = "SIS_VARIABLES_MULTIMEDIA")
@NamedQueries({
        @NamedQuery(name = "MultimediaVariables.findAll", query = "SELECT s FROM MultimediaVariables s"),
        /*@NamedQuery(name = "SisVariablesMultimedia.findByMediaId", query = "SELECT s FROM SisVariablesMultimedia s WHERE s.mediaId = :mediaId"),
        @NamedQuery(name = "SisVariablesMultimedia.findByMediaTipo", query = "SELECT s FROM SisVariablesMultimedia s WHERE s.mediaTipo = :mediaTipo"),
        @NamedQuery(name = "SisVariablesMultimedia.findByMediaVersion", query = "SELECT s FROM SisVariablesMultimedia s WHERE s.mediaVersion = :mediaVersion") */})
public class MultimediaVariables implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sis_variables_multimedia_seq")
    @SequenceGenerator(name = "sis_variables_multimedia_seq", sequenceName = "SIS_VARIABLES_MULTIMEDIA_SEQ01", allocationSize = 1)
    @Column(name = "MEDIA_ID")
    private Long id;

    @Basic(optional = false)
    @Lob
    @Column(name = "MEDIA_URL")
    private String url;

    @Basic(optional = false)
    @Column(name = "MEDIA_TIPO")
    private String type;

    @Version
    @Column(name = "MEDIA_VERSION")
    private Long version;

    @JoinColumn(name = "MEDIA_VAR_ID", referencedColumnName = "VAR_ID")
    @ManyToOne(optional = false)
    private Variables variables;

    public MultimediaVariables() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMediaVersion() {
        return version;
    }

    public void setMediaVersion(Long version) {
        this.version = version;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MultimediaVariables)) {
            return false;
        }
         MultimediaVariables other = (MultimediaVariables) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MultimediaVariables{" + "id=" + id + '}';
    }
    
    

}
