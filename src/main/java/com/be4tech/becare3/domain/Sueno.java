package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sueno.
 */
@Entity
@Table(name = "sueno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sueno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "superficial")
    private Integer superficial;

    @Column(name = "profundo")
    private Integer profundo;

    @Column(name = "despierto")
    private Integer despierto;

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

    public Sueno id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSuperficial() {
        return this.superficial;
    }

    public Sueno superficial(Integer superficial) {
        this.superficial = superficial;
        return this;
    }

    public void setSuperficial(Integer superficial) {
        this.superficial = superficial;
    }

    public Integer getProfundo() {
        return this.profundo;
    }

    public Sueno profundo(Integer profundo) {
        this.profundo = profundo;
        return this;
    }

    public void setProfundo(Integer profundo) {
        this.profundo = profundo;
    }

    public Integer getDespierto() {
        return this.despierto;
    }

    public Sueno despierto(Integer despierto) {
        this.despierto = despierto;
        return this;
    }

    public void setDespierto(Integer despierto) {
        this.despierto = despierto;
    }

    public Instant getTimeInstant() {
        return this.timeInstant;
    }

    public Sueno timeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
        return this;
    }

    public void setTimeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
    }

    public User getUser() {
        return this.user;
    }

    public Sueno user(User user) {
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
        if (!(o instanceof Sueno)) {
            return false;
        }
        return id != null && id.equals(((Sueno) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sueno{" +
            "id=" + getId() +
            ", superficial=" + getSuperficial() +
            ", profundo=" + getProfundo() +
            ", despierto=" + getDespierto() +
            ", timeInstant='" + getTimeInstant() + "'" +
            "}";
    }
}
