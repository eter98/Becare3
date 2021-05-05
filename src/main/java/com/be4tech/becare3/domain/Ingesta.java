package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ingesta.
 */
@Entity
@Table(name = "ingesta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ingesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "consumo_calorias")
    private Integer consumoCalorias;

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

    public Ingesta id(Long id) {
        this.id = id;
        return this;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Ingesta tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getConsumoCalorias() {
        return this.consumoCalorias;
    }

    public Ingesta consumoCalorias(Integer consumoCalorias) {
        this.consumoCalorias = consumoCalorias;
        return this;
    }

    public void setConsumoCalorias(Integer consumoCalorias) {
        this.consumoCalorias = consumoCalorias;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Ingesta fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public Ingesta user(User user) {
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
        if (!(o instanceof Ingesta)) {
            return false;
        }
        return id != null && id.equals(((Ingesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingesta{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", consumoCalorias=" + getConsumoCalorias() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
