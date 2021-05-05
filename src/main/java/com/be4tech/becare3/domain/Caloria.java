package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Caloria.
 */
@Entity
@Table(name = "caloria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Caloria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "calorias_activas")
    private Integer caloriasActivas;

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

    public Caloria id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCaloriasActivas() {
        return this.caloriasActivas;
    }

    public Caloria caloriasActivas(Integer caloriasActivas) {
        this.caloriasActivas = caloriasActivas;
        return this;
    }

    public void setCaloriasActivas(Integer caloriasActivas) {
        this.caloriasActivas = caloriasActivas;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Caloria descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Caloria fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public Caloria user(User user) {
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
        if (!(o instanceof Caloria)) {
            return false;
        }
        return id != null && id.equals(((Caloria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caloria{" +
            "id=" + getId() +
            ", caloriasActivas=" + getCaloriasActivas() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
