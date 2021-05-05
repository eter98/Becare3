package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PresionSanguinea.
 */
@Entity
@Table(name = "presion_sanguinea")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PresionSanguinea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "presion_sanguinea_sistolica")
    private Integer presionSanguineaSistolica;

    @Column(name = "presion_sanguinea_diastolica")
    private Integer presionSanguineaDiastolica;

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

    public PresionSanguinea id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPresionSanguineaSistolica() {
        return this.presionSanguineaSistolica;
    }

    public PresionSanguinea presionSanguineaSistolica(Integer presionSanguineaSistolica) {
        this.presionSanguineaSistolica = presionSanguineaSistolica;
        return this;
    }

    public void setPresionSanguineaSistolica(Integer presionSanguineaSistolica) {
        this.presionSanguineaSistolica = presionSanguineaSistolica;
    }

    public Integer getPresionSanguineaDiastolica() {
        return this.presionSanguineaDiastolica;
    }

    public PresionSanguinea presionSanguineaDiastolica(Integer presionSanguineaDiastolica) {
        this.presionSanguineaDiastolica = presionSanguineaDiastolica;
        return this;
    }

    public void setPresionSanguineaDiastolica(Integer presionSanguineaDiastolica) {
        this.presionSanguineaDiastolica = presionSanguineaDiastolica;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public PresionSanguinea fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public PresionSanguinea user(User user) {
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
        if (!(o instanceof PresionSanguinea)) {
            return false;
        }
        return id != null && id.equals(((PresionSanguinea) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresionSanguinea{" +
            "id=" + getId() +
            ", presionSanguineaSistolica=" + getPresionSanguineaSistolica() +
            ", presionSanguineaDiastolica=" + getPresionSanguineaDiastolica() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
