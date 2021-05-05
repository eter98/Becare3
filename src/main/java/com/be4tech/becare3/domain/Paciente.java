package com.be4tech.becare3.domain;

import com.be4tech.becare3.domain.enumeration.Identificaciont;
import com.be4tech.becare3.domain.enumeration.Sexop;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion")
    private Identificaciont tipoIdentificacion;

    @Column(name = "identificacion")
    private Integer identificacion;

    @Column(name = "edad")
    private Integer edad;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexop sexo;

    @Column(name = "peso_kg")
    private Float pesoKG;

    @Column(name = "estatura_cm")
    private Integer estaturaCM;

    @Column(name = "oximetria_referencia")
    private Integer oximetriaReferencia;

    @Column(name = "temperatura_referencia")
    private Float temperaturaReferencia;

    @Column(name = "ritmo_cardiaco_referencia")
    private Integer ritmoCardiacoReferencia;

    @Column(name = "presion_sistolica_referencia")
    private Integer presionSistolicaReferencia;

    @Column(name = "presion_distolica_referencia")
    private Integer presionDistolicaReferencia;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "pasos_referencia")
    private Integer pasosReferencia;

    @Column(name = "calorias_referencia")
    private Integer caloriasReferencia;

    @Column(name = "meta_referencia")
    private String metaReferencia;

    @ManyToOne
    private Condicion condicion;

    @ManyToOne
    private IPS ips;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tratamieto tratamiento;

    @ManyToOne
    private Farmaceutica farmaceutica;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Paciente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Identificaciont getTipoIdentificacion() {
        return this.tipoIdentificacion;
    }

    public Paciente tipoIdentificacion(Identificaciont tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
        return this;
    }

    public void setTipoIdentificacion(Identificaciont tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Integer getIdentificacion() {
        return this.identificacion;
    }

    public Paciente identificacion(Integer identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public void setIdentificacion(Integer identificacion) {
        this.identificacion = identificacion;
    }

    public Integer getEdad() {
        return this.edad;
    }

    public Paciente edad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Sexop getSexo() {
        return this.sexo;
    }

    public Paciente sexo(Sexop sexo) {
        this.sexo = sexo;
        return this;
    }

    public void setSexo(Sexop sexo) {
        this.sexo = sexo;
    }

    public Float getPesoKG() {
        return this.pesoKG;
    }

    public Paciente pesoKG(Float pesoKG) {
        this.pesoKG = pesoKG;
        return this;
    }

    public void setPesoKG(Float pesoKG) {
        this.pesoKG = pesoKG;
    }

    public Integer getEstaturaCM() {
        return this.estaturaCM;
    }

    public Paciente estaturaCM(Integer estaturaCM) {
        this.estaturaCM = estaturaCM;
        return this;
    }

    public void setEstaturaCM(Integer estaturaCM) {
        this.estaturaCM = estaturaCM;
    }

    public Integer getOximetriaReferencia() {
        return this.oximetriaReferencia;
    }

    public Paciente oximetriaReferencia(Integer oximetriaReferencia) {
        this.oximetriaReferencia = oximetriaReferencia;
        return this;
    }

    public void setOximetriaReferencia(Integer oximetriaReferencia) {
        this.oximetriaReferencia = oximetriaReferencia;
    }

    public Float getTemperaturaReferencia() {
        return this.temperaturaReferencia;
    }

    public Paciente temperaturaReferencia(Float temperaturaReferencia) {
        this.temperaturaReferencia = temperaturaReferencia;
        return this;
    }

    public void setTemperaturaReferencia(Float temperaturaReferencia) {
        this.temperaturaReferencia = temperaturaReferencia;
    }

    public Integer getRitmoCardiacoReferencia() {
        return this.ritmoCardiacoReferencia;
    }

    public Paciente ritmoCardiacoReferencia(Integer ritmoCardiacoReferencia) {
        this.ritmoCardiacoReferencia = ritmoCardiacoReferencia;
        return this;
    }

    public void setRitmoCardiacoReferencia(Integer ritmoCardiacoReferencia) {
        this.ritmoCardiacoReferencia = ritmoCardiacoReferencia;
    }

    public Integer getPresionSistolicaReferencia() {
        return this.presionSistolicaReferencia;
    }

    public Paciente presionSistolicaReferencia(Integer presionSistolicaReferencia) {
        this.presionSistolicaReferencia = presionSistolicaReferencia;
        return this;
    }

    public void setPresionSistolicaReferencia(Integer presionSistolicaReferencia) {
        this.presionSistolicaReferencia = presionSistolicaReferencia;
    }

    public Integer getPresionDistolicaReferencia() {
        return this.presionDistolicaReferencia;
    }

    public Paciente presionDistolicaReferencia(Integer presionDistolicaReferencia) {
        this.presionDistolicaReferencia = presionDistolicaReferencia;
        return this;
    }

    public void setPresionDistolicaReferencia(Integer presionDistolicaReferencia) {
        this.presionDistolicaReferencia = presionDistolicaReferencia;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public Paciente comentarios(String comentarios) {
        this.comentarios = comentarios;
        return this;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Integer getPasosReferencia() {
        return this.pasosReferencia;
    }

    public Paciente pasosReferencia(Integer pasosReferencia) {
        this.pasosReferencia = pasosReferencia;
        return this;
    }

    public void setPasosReferencia(Integer pasosReferencia) {
        this.pasosReferencia = pasosReferencia;
    }

    public Integer getCaloriasReferencia() {
        return this.caloriasReferencia;
    }

    public Paciente caloriasReferencia(Integer caloriasReferencia) {
        this.caloriasReferencia = caloriasReferencia;
        return this;
    }

    public void setCaloriasReferencia(Integer caloriasReferencia) {
        this.caloriasReferencia = caloriasReferencia;
    }

    public String getMetaReferencia() {
        return this.metaReferencia;
    }

    public Paciente metaReferencia(String metaReferencia) {
        this.metaReferencia = metaReferencia;
        return this;
    }

    public void setMetaReferencia(String metaReferencia) {
        this.metaReferencia = metaReferencia;
    }

    public Condicion getCondicion() {
        return this.condicion;
    }

    public Paciente condicion(Condicion condicion) {
        this.setCondicion(condicion);
        return this;
    }

    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    public IPS getIps() {
        return this.ips;
    }

    public Paciente ips(IPS iPS) {
        this.setIps(iPS);
        return this;
    }

    public void setIps(IPS iPS) {
        this.ips = iPS;
    }

    public User getUser() {
        return this.user;
    }

    public Paciente user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tratamieto getTratamiento() {
        return this.tratamiento;
    }

    public Paciente tratamiento(Tratamieto tratamieto) {
        this.setTratamiento(tratamieto);
        return this;
    }

    public void setTratamiento(Tratamieto tratamieto) {
        this.tratamiento = tratamieto;
    }

    public Farmaceutica getFarmaceutica() {
        return this.farmaceutica;
    }

    public Paciente farmaceutica(Farmaceutica farmaceutica) {
        this.setFarmaceutica(farmaceutica);
        return this;
    }

    public void setFarmaceutica(Farmaceutica farmaceutica) {
        this.farmaceutica = farmaceutica;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return id != null && id.equals(((Paciente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipoIdentificacion='" + getTipoIdentificacion() + "'" +
            ", identificacion=" + getIdentificacion() +
            ", edad=" + getEdad() +
            ", sexo='" + getSexo() + "'" +
            ", pesoKG=" + getPesoKG() +
            ", estaturaCM=" + getEstaturaCM() +
            ", oximetriaReferencia=" + getOximetriaReferencia() +
            ", temperaturaReferencia=" + getTemperaturaReferencia() +
            ", ritmoCardiacoReferencia=" + getRitmoCardiacoReferencia() +
            ", presionSistolicaReferencia=" + getPresionSistolicaReferencia() +
            ", presionDistolicaReferencia=" + getPresionDistolicaReferencia() +
            ", comentarios='" + getComentarios() + "'" +
            ", pasosReferencia=" + getPasosReferencia() +
            ", caloriasReferencia=" + getCaloriasReferencia() +
            ", metaReferencia='" + getMetaReferencia() + "'" +
            "}";
    }
}
