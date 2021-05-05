package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Oximetria.
 */
@Entity
@Table(name = "oximetria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Oximetria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "oximetria")
    private Integer oximetria;

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

    public Oximetria id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getOximetria() {
        return this.oximetria;
    }

    public Oximetria oximetria(Integer oximetria) {
        this.oximetria = oximetria;
        return this;
    }

    public void setOximetria(Integer oximetria) {
        this.oximetria = oximetria;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Oximetria fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public Oximetria user(User user) {
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
        if (!(o instanceof Oximetria)) {
            return false;
        }
        return id != null && id.equals(((Oximetria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Oximetria{" +
            "id=" + getId() +
            ", oximetria=" + getOximetria() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
