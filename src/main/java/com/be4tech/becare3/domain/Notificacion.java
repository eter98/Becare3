package com.be4tech.becare3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notificacion.
 */
@Entity
@Table(name = "notificacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_actualizacion")
    private ZonedDateTime fechaActualizacion;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "tipo_notificacion")
    private Integer tipoNotificacion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private TokenDisp token;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notificacion id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Notificacion fechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTime getFechaActualizacion() {
        return this.fechaActualizacion;
    }

    public Notificacion fechaActualizacion(ZonedDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
        return this;
    }

    public void setFechaActualizacion(ZonedDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public Notificacion estado(Integer estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getTipoNotificacion() {
        return this.tipoNotificacion;
    }

    public Notificacion tipoNotificacion(Integer tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
        return this;
    }

    public void setTipoNotificacion(Integer tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public TokenDisp getToken() {
        return this.token;
    }

    public Notificacion token(TokenDisp tokenDisp) {
        this.setToken(tokenDisp);
        return this;
    }

    public void setToken(TokenDisp tokenDisp) {
        this.token = tokenDisp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacion)) {
            return false;
        }
        return id != null && id.equals(((Notificacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notificacion{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaActualizacion='" + getFechaActualizacion() + "'" +
            ", estado=" + getEstado() +
            ", tipoNotificacion=" + getTipoNotificacion() +
            "}";
    }
}
