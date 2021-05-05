package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Alarma.
 */
@Entity
@Table(name = "alarma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Alarma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "time_instant")
    private Instant timeInstant;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "procedimiento")
    private String procedimiento;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "verificar")
    private Boolean verificar;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "prioridad")
    private String prioridad;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alarma id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getTimeInstant() {
        return this.timeInstant;
    }

    public Alarma timeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
        return this;
    }

    public void setTimeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Alarma descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProcedimiento() {
        return this.procedimiento;
    }

    public Alarma procedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
        return this;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Alarma titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getVerificar() {
        return this.verificar;
    }

    public Alarma verificar(Boolean verificar) {
        this.verificar = verificar;
        return this;
    }

    public void setVerificar(Boolean verificar) {
        this.verificar = verificar;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Alarma observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPrioridad() {
        return this.prioridad;
    }

    public Alarma prioridad(String prioridad) {
        this.prioridad = prioridad;
        return this;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public User getUser() {
        return this.user;
    }

    public Alarma user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alarma)) {
            return false;
        }
        return id != null && id.equals(((Alarma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alarma{" +
            "id=" + getId() +
            ", timeInstant='" + getTimeInstant() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", procedimiento='" + getProcedimiento() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", verificar='" + getVerificar() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", prioridad='" + getPrioridad() + "'" +
            "}";
    }
}
