package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pasos.
 */
@Entity
@Table(name = "pasos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pasos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nro_pasos")
    private Integer nroPasos;

    @Column(name = "time_instant")
    private Instant timeInstant;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pasos id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getNroPasos() {
        return this.nroPasos;
    }

    public Pasos nroPasos(Integer nroPasos) {
        this.nroPasos = nroPasos;
        return this;
    }

    public void setNroPasos(Integer nroPasos) {
        this.nroPasos = nroPasos;
    }

    public Instant getTimeInstant() {
        return this.timeInstant;
    }

    public Pasos timeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
        return this;
    }

    public void setTimeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
    }

    public User getUser() {
        return this.user;
    }

    public Pasos user(User user) {
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
        if (!(o instanceof Pasos)) {
            return false;
        }
        return id != null && id.equals(((Pasos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pasos{" +
            "id=" + getId() +
            ", nroPasos=" + getNroPasos() +
            ", timeInstant='" + getTimeInstant() + "'" +
            "}";
    }
}
