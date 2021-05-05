package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Peso.
 */
@Entity
@Table(name = "peso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Peso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "peso_kg")
    private Integer pesoKG;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Peso id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPesoKG() {
        return this.pesoKG;
    }

    public Peso pesoKG(Integer pesoKG) {
        this.pesoKG = pesoKG;
        return this;
    }

    public void setPesoKG(Integer pesoKG) {
        this.pesoKG = pesoKG;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Peso descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Peso fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public Peso user(User user) {
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
        if (!(o instanceof Peso)) {
            return false;
        }
        return id != null && id.equals(((Peso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Peso{" +
            "id=" + getId() +
            ", pesoKG=" + getPesoKG() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
