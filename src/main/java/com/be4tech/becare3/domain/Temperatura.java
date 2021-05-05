package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Temperatura.
 */
@Entity
@Table(name = "temperatura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Temperatura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "temperatura")
    private Float temperatura;

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

    public Temperatura id(Long id) {
        this.id = id;
        return this;
    }

    public Float getTemperatura() {
        return this.temperatura;
    }

    public Temperatura temperatura(Float temperatura) {
        this.temperatura = temperatura;
        return this;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Temperatura fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public Temperatura user(User user) {
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
        if (!(o instanceof Temperatura)) {
            return false;
        }
        return id != null && id.equals(((Temperatura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Temperatura{" +
            "id=" + getId() +
            ", temperatura=" + getTemperatura() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
