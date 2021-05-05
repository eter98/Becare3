package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FrecuenciaCardiaca.
 */
@Entity
@Table(name = "frecuencia_cardiaca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FrecuenciaCardiaca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

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

    public FrecuenciaCardiaca id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getFrecuenciaCardiaca() {
        return this.frecuenciaCardiaca;
    }

    public FrecuenciaCardiaca frecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        return this;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public FrecuenciaCardiaca fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public FrecuenciaCardiaca user(User user) {
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
        if (!(o instanceof FrecuenciaCardiaca)) {
            return false;
        }
        return id != null && id.equals(((FrecuenciaCardiaca) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrecuenciaCardiaca{" +
            "id=" + getId() +
            ", frecuenciaCardiaca=" + getFrecuenciaCardiaca() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
