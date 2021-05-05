package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Programa.
 */
@Entity
@Table(name = "programa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Programa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "calorias_actividad")
    private Integer caloriasActividad;

    @Column(name = "pasos_actividad")
    private Integer pasosActividad;

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

    public Programa id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCaloriasActividad() {
        return this.caloriasActividad;
    }

    public Programa caloriasActividad(Integer caloriasActividad) {
        this.caloriasActividad = caloriasActividad;
        return this;
    }

    public void setCaloriasActividad(Integer caloriasActividad) {
        this.caloriasActividad = caloriasActividad;
    }

    public Integer getPasosActividad() {
        return this.pasosActividad;
    }

    public Programa pasosActividad(Integer pasosActividad) {
        this.pasosActividad = pasosActividad;
        return this;
    }

    public void setPasosActividad(Integer pasosActividad) {
        this.pasosActividad = pasosActividad;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Programa fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public Programa user(User user) {
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
        if (!(o instanceof Programa)) {
            return false;
        }
        return id != null && id.equals(((Programa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Programa{" +
            "id=" + getId() +
            ", caloriasActividad=" + getCaloriasActividad() +
            ", pasosActividad=" + getPasosActividad() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
